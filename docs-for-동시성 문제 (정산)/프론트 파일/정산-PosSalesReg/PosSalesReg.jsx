import React, { useRef, useState } from 'react'
import 'assets/style/common.scss'
import 'assets/style/contents.scss'
import 'assets/style/grid.scss'
import { useForm } from 'react-hook-form'
import Loading from 'components/common/loading/Loading'
import PosSalesRegSearch from 'components/cps/closing/closingmng/PosSalesRegSearch'
import PosSalesRegSalesDtl from 'components/cps/closing/closingmng/PosSalesRegSalesDtl'
import PosSalesRegCalcDtl from 'components/cps/closing/closingmng/PosSalesRegCalcDtl'
import PosSalesRegSendGdoh from 'components/cps/closing/closingmng/PosSalesRegSendGdoh'
import PosSalesRegSendSales from 'components/cps/closing/closingmng/PosSalesRegSendSales'
import PosSalesRegSalesPayment from 'components/cps/closing/closingmng/PosSalesRegSalesPayment'
import PosSalesRegSalesPaymentDtl from 'components/cps/closing/closingmng/PosSalesRegSalesPaymentDtl'
import { getUserInfoFromSessionStorage } from 'utils/user-info-storage-util'
import { useAlert, useConfirm } from 'hooks/popup/use-common-popup'
import { getMessage } from 'utils/message-util'
import { useTabulator } from 'hooks/common/use-tabulator'
import {
    fetchCalcDtlBySearchParams,
    fetchPaySalesLstBySearchParams,
    fetchPaymentLstBySearchParams,
    fetchPaymentDtlLstBySearchParams,
    fetchSendSalesBySearchParams,
    fetchSendGdohBySearchParams,
    fetchCloseYnBySearchParams,
    fetchInsertPosSales,
    fetchInsertOncrdtTaxPblLst,
    selectPosSapCompareCnt,
    deleteSalesSum
} from 'api/cps/closing/closingmng/PosSalesReg-fetch'
import { isEmptyStr } from 'utils/validation-util'
import { useFwTab } from 'hooks/common/use-fw-tab'
import moment from 'moment'
import { pdfOpen } from 'utils/pdf-utils'

export default function PosSalesReg() {
    const { tabs, fwMenuName, currentTabIndex, handleShortcuts } = useFwTab()
    const handleFileDownload = () => {
        pdfOpen(tabs[currentTabIndex].id)
    }
    const [isLoading, setIsLoading] = useState(false)
    const [calcDtl, setCalcDtl] = useState([])
    const [salesDtlLst, setSalesDtlLst] = useState([])
    const [paymentLst, setPaymentLst] = useState([])
    const [paymentDtlLst, setPaymentDtlLst] = useState([])
    const [sendSales, setSendSales] = useState([])
    const [sendGdoh, setSendGdoh] = useState([])
    const [compareCnt, setCompareCnt] = useState(null)

    const { alert } = useAlert()
    const { confirm } = useConfirm()
    const session = getUserInfoFromSessionStorage()

    const calcDtlGridRef = useRef(null)
    const salesDtlGridRef = useRef(null)
    const paymentGridRef = useRef(null)
    const paymentDtlGridRef = useRef(null)
    const sendSalesGridRef = useRef(null)
    const sendGdohGridRef = useRef(null)
    const calcDtlGridController = useTabulator({ ref: calcDtlGridRef })
    const salesDtlGridController = useTabulator({ ref: salesDtlGridRef })
    const paymentGridController = useTabulator({ ref: paymentGridRef })
    const paymentDtlGridController = useTabulator({ ref: paymentDtlGridRef })
    const sendSalesGridController = useTabulator({ ref: sendSalesGridRef })
    const sendGdohGridController = useTabulator({ ref: sendGdohGridRef })

    const initValues = {
        coId: session.coId,
        userId: session.userid,
        shopId: '',
        shopNm: '',
        steId: '',
        steNm: '',
        schSalesDt: moment().format('YYYYMMDD'),
        noSendSales: '0',
        noSendGdoh: '0',
    }

    const { handleSubmit, reset, control, watch, setValue, getValues, register } = useForm({
        // 초기값 지정
        defaultValues: initValues,
    })

    const params = {
        coId: session.coId,
        userId: session.userid,
    }

    const handleShortcutsFn = (id) => {
        handleShortcuts(id)
    }

    //POS매출등록 정산 상세내역 조회 콜백
    const searchCalcDtlCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setCalcDtl([])
            } else {
                data.shopId = getValues('shopId')
                data.steId = getValues('steId')
                data.salesDt = getValues('schSalesDt')
                setCalcDtl([data])
            }
        }
        setIsLoading(false)
    }

    //POS매출등록 매출 세부내역 조회 콜백
    const searchPaySalesLstCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setSalesDtlLst([])
            } else {
                setSalesDtlLst(data)
            }
        }
        // setIsLoading(false)
    }

    //POS매출등록 결제수단별 내역 조회 콜백
    const searchPaymentLstCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setPaymentLst([])
            } else {
                setPaymentLst(data)
            }
        }
        // setIsLoading(false)
    }

    //POS매출등록 결제수단별 상세내역 조회 콜백
    const searchPaymentDtlLstCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setPaymentDtlLst([])
            } else {
                setPaymentDtlLst(data)
            }
        }
        setIsLoading(false)
    }

    //POS매출등록 매출전송 조회 콜백
    const searchSendSalesCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setSendSales([])
            } else {
                setValue('noSendSales', data.noSendSales)
                setSendSales([data])
            }
        }
        // setIsLoading(false)
    }

    //POS매출등록 시재전송 조회 콜백
    const searchSendGdohCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setSendGdoh([])
            } else {
                setValue('noSendGdoh', data.noSendGdoh)
                setSendGdoh([data])
            }
        } else {
            setSendGdoh([])
        }
        // setIsLoading(false)
    }

    //POS매출등록 대사결과 조회 콜백
    const selectPosSapCompareCntCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setCompareCnt(0)
            } else {
                setCompareCnt(data)
            }
        } else {
            setCompareCnt(0)
        }
        // setIsLoading(false)
    }

    //POS 매출등록 INSERT 콜백함수
    const insertPosSalesCallback = (result, data) => {
        setIsLoading(false)
        if (result) {
            if (data === 0) {
                //저장후 다시 조회
                alert(getMessage('common_msg_001', { status: '저장' }), {
                    okCallBack() {
                        handleOnClickSubmit()
                    }
                })
            } else {
                alert('POS매출과 승인내역이 일치하지 않습니다.')
            }
        }
    }

    const searchCloseYnCallback = (result, data) => {
        if (result) {
            const calcData = calcDtlGridRef?.current?.getData()

            params.userId = getValues('userId')
            params.shopId = calcData[0].shopId
            params.steId = calcData[0].steId
            params.salesDt = calcData[0].salesDt
            if (data === null) {
                fetchInsertPosSales(params, insertPosSalesCallback)
            } else {
                setIsLoading(false)
                if (data.mngYn === 'N' || data.salesDdlYn === '1') {
                    alert('이미 마감되어 진행할 수 없습니다.')
                    return
                }
                if (data.monthSalesDdlYn === 'Y') {
                    alert('이미 월 마감되어 진행할 수 없습니다.')
                    return
                }
                if (data.oncrdtCustCnt > 0) {
                    alert('매출데이터 중 외상고객이 없는 자료가 있어 전송처리할 수 없습니다.')
                    return
                }
                if (data.taxBilYn === 'Y') {
                    alert('이미 발행된 세금계산서가 있습니다. 세금계산서 발행 취소 후 다시 전송하세요.')
                    return
                }
                if (data.salesLogCnt > 0) {
                    alert('매출 집계가 완료되지 않아서 진행할 수 없습니다.')
                    return
                }
                if (data.cancelRsnDescNullCnt > 0) {
                    alert('[POS 현금매출취소관리]에서 사유 입력 후 마감이 가능합니다.', {
                        okCallBack() {
                            handleShortcutsFn('!P!C-COM-FS-41010211')
                        }
                    })
                    return
                }
                // if (getValues('noSendSales') != '0') {
                setIsLoading(true)
                fetchInsertPosSales(params, insertPosSalesCallback)
                // } else {
                //     alert('미전송 내역이 없습니다.');
                //     return
                // }                
            }
        } else {
            setIsLoading(false)
        }
    }

    /**
     * 초기화 버튼 클릭 이벤트
     * @param {*} e
     */
    const handleOnClickReset = async (e) => {
        e.preventDefault()
        reset()
        setCalcDtl([])
        setSalesDtlLst([])
        setPaymentLst([])
        setPaymentDtlLst([])
        setSendSales([])
        setSendGdoh([])
        setCompareCnt(null)
    }

    //조회버튼 클릭
    const handleOnClickSubmit = () => {

        if (getValues('shopId') === '') {
            alert(getMessage('common_validation_020', { target: '점포코드' }))
            return
        }

        if (getValues('steId') === '') {
            alert(getMessage('common_validation_020', { target: '사이트코드' }))
            return
        }

        if (getValues('schSalesDt') === '') {
            alert(getMessage('common_validation_020', { target: '등록일자' }))
            return
        }

        const calcDtlData = calcDtlGridRef.current.getData()
        const salesDtlLstData = salesDtlGridRef.current.getData()

        setCalcDtl([])
        calcDtlGridRef?.current?.clearData()

        setSalesDtlLst([])
        salesDtlGridRef?.current?.clearData()

        setPaymentDtlLst([])
        paymentDtlGridRef?.current?.clearData()

        params.shopId = getValues('shopId')
        params.steId = getValues('steId')
        params.salesDt = getValues('schSalesDt')

        setIsLoading(true)
        fetchCalcDtlBySearchParams(params, searchCalcDtlCallback)
        fetchPaySalesLstBySearchParams(params, searchPaySalesLstCallback)
        fetchPaymentLstBySearchParams(params, searchPaymentLstCallback)
        fetchSendSalesBySearchParams(params, searchSendSalesCallback)
        fetchSendGdohBySearchParams(params, searchSendGdohCallback)
        selectPosSapCompareCnt(params, selectPosSapCompareCntCallback)
    }

    const handleOnClickSend = () => {
        const data = calcDtlGridRef?.current?.getData()

        if (isLoading) {
            alert('마감 중입니다.')
            return
        }

        if (data.length === 0) {
            alert('조회된 데이터가 없습니다. 조회 후 전송 하세요')
            return
        } else {
            if (data[0].shopId === '') {
                alert(getMessage('common_validation_020', { target: '점포코드' }))
                return
            }

            if (data[0].steId === '') {
                alert(getMessage('common_validation_020', { target: '사이트코드' }))
                return
            }

            if (data[0].salesDt === '') {
                alert(getMessage('common_validation_020', { target: '등록일자' }))
                return
            }

            confirm('마감 하시겠습니까?', {
                okCallBack() {
                    setIsLoading(true)
                    params.userId = getValues('userId')
                    params.steId = data[0].steId
                    params.salesDt = data[0].salesDt
                    fetchCloseYnBySearchParams(params, searchCloseYnCallback)
                }
            })
        }
    }

    /**
     * 결제수단별 매출 row 클릭 이벤트
     * @param {*} row
     * @returns
     */
    const handleRowSalesPaymentClick = (e, row) => {
        if (!row) return

        const selectPayCd = row.getData().payCd

        params.shopId = getValues('shopId')
        params.steId = getValues('steId')
        params.salesDt = getValues('schSalesDt')
        params.payCd = selectPayCd

        paymentDtlGridRef?.current?.columnManager.getColumnByField("purCorpCd").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("purCorpNm").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("curTypeCd").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("curTypeNm").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("easyPayTypeCd").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("easyPayTypeNm").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("custId").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("custNm").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("taxBilYn").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("rcvAmt").hide()
        paymentDtlGridRef?.current?.columnManager.getColumnByField("dutyfDivCd").hide()

        switch (selectPayCd) {
            case "201":
                paymentDtlGridRef?.current?.columnManager.getColumnByField("curTypeCd").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("curTypeNm").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("rcvAmt").show()
                break;
            case "203":
                paymentDtlGridRef?.current?.columnManager.getColumnByField("purCorpCd").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("purCorpNm").show()
                break;
            case "205":
                paymentDtlGridRef?.current?.columnManager.getColumnByField("dutyfDivCd").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("custId").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("custNm").show()
                paymentDtlGridRef?.current?.columnManager.getColumnByField("taxBilYn").show()
                break;
            case "208":
                break;
            //case "4":
            //    paymentDtlGridRef?.current?.columnManager.getColumnByField("easyPayTypeCd").show()
            //    paymentDtlGridRef?.current?.columnManager.getColumnByField("easyPayTypeNm").show()
            //    break;
        }
        paymentDtlGridRef?.current?.redraw()

        setIsLoading(true)
        fetchPaymentDtlLstBySearchParams(params, searchPaymentDtlLstCallback)
    }

    const handleOnClickResetBtn = (e) => {
        handleOnClickReset(e)
    }

    // 매출재집계 버튼
    const handleOnClickReSumBtn = (e) => {
        e.preventDefault()
        setIsLoading(true)
        const data = calcDtlGridRef.current.getData()


        params.coId = getValues('coId')
        params.steId = getValues('steId')
        params.salesDt = getValues('schSalesDt')

        if (!params.coId || !params.steId || !params.salesDt) {
            alert('사이트코드 및 매출일자는 필수 입력값입니다.');
            setIsLoading(false)
            return;
        }

        deleteSalesSum(params, deleteSalesSumCallback)
        setIsLoading(false)
    }

    const deleteSalesSumCallback = (result) => {
        setIsLoading(false)
        if (result) {
            alert("매출집계 초기화가 완료되었습니다. 자동으로 현재 기준으로 재집계되며, 반영에 수 분 소요될 수 있습니다.")
            handleOnClickSubmit() // 저장 후 재조회
        } else {
            alert("매출집계 초기화 중 오류가 발생했습니다. 관리자에게 문의해 주세요.")
        }
    }


    const insertOncrdtTaxPblLstCallback = (result) => {
        setIsLoading(false)
        if (result) {
            //저장후 다시 조회
            alert(getMessage('common_msg_001', { status: '저장' }), {
                okCallBack() {
                    handleOnClickSubmit()
                }
            })
        }
    }

    const paymentDtlSaveClick = () => {
        const data = paymentDtlGridRef.current.getSelectedData()

        if (data.length === 0) {
            alert(getMessage('common_validation_005'))
            return
        }

        for (const row of data) {
            row.coId = getValues('coId')
            row.shopId = getValues('shopId')
            row.steId = getValues('steId')
            row.salesDt = getValues('schSalesDt')
            row.userId = getValues('userId')
        }

        setIsLoading(true)
        fetchInsertOncrdtTaxPblLst(data, insertOncrdtTaxPblLstCallback)
    }

    return (
        <>
            {isLoading && <Loading />}
            <div className="component">
                <div className="title">
                    <h3 className="title-tooltip">
                        {fwMenuName}
                        <button className="tooltip-btn" onClick={handleFileDownload}>
                            <span className="tooltip-icon">Tooltip on</span>
                            <span className="tooltip-contents hide">매뉴얼 조회</span>
                        </button>
                    </h3>
                    <div>
                        <button className="btn outline-button" onClick={handleOnClickReSumBtn}>매출재집계</button>
                        <button className="btn outline-button" onClick={handleOnClickResetBtn}>초기화</button>
                        <button className="btn btn-gray" onClick={handleOnClickSubmit}>조회</button>
                        <button className="btn btn-gray" onClick={handleOnClickSend}>마감</button>
                    </div>
                </div>
                <PosSalesRegSearch
                    setValue={setValue}
                    watch={watch}
                    control={control}
                    register={register}
                    handleOnClickReset={handleOnClickReset}
                    handleSearchClick={handleOnClickSubmit}
                    shpIdSchNm={'shopId'}
                    steIdSchNm={'steId'}
                />
            </div>

            {/* 정산 상세내역 */}
            <div className="component">
                <PosSalesRegCalcDtl
                    data={calcDtl}
                    calcDtlGridRef={calcDtlGridRef}
                />
            </div>

            <div className="component-wrap-2">
                {/* 매출 전송 */}
                <div className="component">
                    <PosSalesRegSendSales
                        data={sendSales}
                        sendSalesGridRef={sendSalesGridRef}
                    />
                </div>

                {/* 시재 전송 */}
                <div className="component">
                    <PosSalesRegSendGdoh
                        data={sendGdoh}
                        sendGdohGridRef={sendGdohGridRef}
                        compareCnt={compareCnt}
                    />
                </div>
            </div>

            {/* 매출 세부내역 */}
            <div className="component h240">
                <PosSalesRegSalesDtl
                    data={salesDtlLst}
                    salesDtlGridRef={salesDtlGridRef}
                />
            </div>

            <div className="component-wrap-2">
                {/* 결제수단별 매출 */}
                <div className="component">
                    <PosSalesRegSalesPayment
                        data={paymentLst}
                        paymentGridRef={paymentGridRef}
                        handleRowClick={handleRowSalesPaymentClick}
                    />
                </div>

                {/* 결제수단별 매출 상세 */}
                <div className="component h240">
                    <PosSalesRegSalesPaymentDtl
                        data={paymentDtlLst}
                        paymentDtlGridRef={paymentDtlGridRef}
                        handleSaveBtn={paymentDtlSaveClick}
                    />
                </div>
            </div>
        </>
    )
}

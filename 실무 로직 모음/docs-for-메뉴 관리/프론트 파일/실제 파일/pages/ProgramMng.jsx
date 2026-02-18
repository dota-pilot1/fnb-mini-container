import React, { useRef, useState } from 'react'
import 'assets/style/common.scss'
import 'assets/style/contents.scss'
import 'assets/style/grid.scss'
import { useForm } from 'react-hook-form'
import Loading from 'components/common/loading/Loading'
import ProgramMngGrid from 'components/cps/system/systemmng/ProgramMngGrid'
import { getUserInfoFromSessionStorage } from 'utils/user-info-storage-util'
import { useAlert, useConfirm } from 'hooks/popup/use-common-popup'
import { getMessage } from 'utils/message-util'
import { useTabulator } from 'hooks/common/use-tabulator'
import {
    fetchPgmMngLstBySearchParams,
    fetchInsertPgmMngLst,
    fetchPgmMngLst,
} from 'api/cps/system/systemmng/ProgramMng-fetch'
import { isEmptyStr } from 'utils/validation-util'
import { useFwTab } from 'hooks/common/use-fw-tab'
import { pdfOpen } from 'utils/pdf-utils'

export default function ProgramMng() {
    const [isLoading, setIsLoading] = useState(false)
    const [pgmLst, setPgmLst] = useState([])

    const { alert } = useAlert()
    const { confirm } = useConfirm()
    const session = getUserInfoFromSessionStorage()

    const gridRef = useRef(null)
    const pgmMngGridController = useTabulator({ ref: gridRef })
    const { tabs, fwMenuName, currentTabIndex } = useFwTab()
    const handleFileDownload = () => {
        pdfOpen(tabs[currentTabIndex].id)
    }

    const initValues = {
        coId: session.coId,
        userId: session.userid,
    }
    const { handleSubmit, reset, control, watch, setValue, getValues, register } = useForm({
        // 초기값 지정
        defaultValues: initValues,
    })

    const params = {
        coId: session.coId,
        userId: session.userid,
    }

    //프로그램관리 목록 조회 콜백
    const searchPgmMngLstCallback = (result, data) => {
        if (result) {
            if (data === null) {
                setPgmLst([])
            } else {
                for (const row of data) {
                    const gridIndex = new Date().getTime().toString()
                    row.index = gridIndex + row.progNo
                    row.status = 'T'
                }
                setPgmLst(data)
            }
        }
        setIsLoading(false)
    }

    /**
     * 초기화 버튼 클릭 이벤트
     * @param {*} e
     */
    const handleOnClickReset = async (e) => {
        e.preventDefault()
        reset()
        setPgmLst([])
        gridRef?.current?.clearData()
    }

    //조회버튼 클릭
    const handleOnClickSubmit = () => {
        const list = gridRef.current.getData()
        if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
                if ((list[i].status === 'N' || list[i].status === 'M')) {
                    confirm('변경사항이 있습니다. 계속 진행하시겠습니까?', {
                        okCallBack() {
                            setIsLoading(true)
                            fetchPgmMngLstBySearchParams(params, searchPgmMngLstCallback)
                        },
                    })
                    break
                } else {
                    setIsLoading(true)
                    fetchPgmMngLstBySearchParams(params, searchPgmMngLstCallback)
                }
            }
        } else {
            setIsLoading(true)
            fetchPgmMngLstBySearchParams(params, searchPgmMngLstCallback)
        }
    }

    /**
     * 행추가
     * @param {*} row
     * @returns
     */
    const handleApplyAddRow = (e) => {

        if (gridRef?.current?.getData().length === 0) {
            alert('조회 후 이용해주십시오.')
            return
        }

        const gridIndex = new Date().getTime().toString()
        gridRef?.current?.addRow({
            'progCd': '',
            'progNm': '',
            'progLvl': '',
            'progNo': '',
            'progUrl': '',
            'progUrlArgs': '',
            'menuYn': 'Y',
            'index': gridIndex,
            'status': 'N',
        }, true)

        //행추가되면 기존 select된 행 해제하고 추가한 행 select
        gridRef?.current?.deselectRow()
        gridRef?.current?.selectRow(gridIndex)
        return
    }

    /**
     * 행삭제
     * @param {*} row
     * @returns
     */
    const handleApplyRemoveRow = (e) => {
        const selectedRows = gridRef?.current?.getSelectedRows();
        const selectedDatas = gridRef?.current?.getSelectedData();

        if (selectedRows.length > 0) {
            if (selectedDatas[0].status === 'N') {
                gridRef?.current?.deleteRow(selectedRows[0].getIndex())
            } else {
                //추가한 행만 삭제 가능
                alert(getMessage('menu_validation_006'))
            }
        } else {
            //선택된 행이 없음
            alert('삭제할 행을 선택해주세요.')
        }
    }

    /**
     * row 클릭 이벤트
     * @param {*} row
     * @returns
     */
    const handleRowClick = (e, row) => {
        if (!row) return
        // if (row.getData().status !== 'N') gridRef?.current?.clearData()
        gridRef?.current?.deselectRow()
        gridRef?.current?.selectRow(row)
    }

    //프로그램관리 INSERT 콜백함수
    const insertPgmMngLstCallback = (result, data) => {
        if (result) {
            setPgmLst([])
            gridRef?.current?.clearData()
            params.progCd = ''
            //저장후 다시 조회
            alert(getMessage('common_msg_001', { status: '저장' }), {
                okCallBack() {
                    handleOnClickSubmit()
                }
            })
        } else {
            alert("동일한 프로그램코드를 가진 데이터가 있어 저장에 실패하였습니다.")
            setIsLoading(false)
        }
    }

    /**
     * 프로그램관리 저장 버튼 클릭 이벤트
     * @param {*} e
     * @returns
     */
    const handleSaveBtn = async (e) => {
        e.preventDefault()
        if (!gridRef?.current) {
            alert('조회 후 이용해주십시오.')
            return
        }

        const list = gridRef.current.getData()
        let array = []
        for (let i = 0; i < list.length; i++) {
            if ((list[i].status === 'N' || list[i].status === 'M')) {

                if (isEmptyStr(list[i].progCd)) {
                    alert(getMessage('common_validation_020', { target: '프로그램코드' }))
                    return
                }

                if (isEmptyStr(list[i].progNm)) {
                    alert(getMessage('common_validation_020', { target: '프로그램명' }))
                    return
                }

                if (list[i].progLvl === '') {
                    alert(getMessage('common_validation_020', { target: '프로그램레벨' }))
                    return
                }

                if (isEmptyStr(list[i].progNo)) {
                    alert(getMessage('common_validation_020', { target: '프로그램내부순번' }))
                    return
                }

                const dupArr = list.filter(function (item1, idx1) {
                    return item1.progCd == list[i].progCd
                })

                if (dupArr.length > 1) {
                    alert(`중복된 자료가 있어 저장할 수 없습니다[(프로그램코드), ${i + 1}행]`)
                    return
                }

                if (list[i].status === 'N') {
                    let params2 = {}
                    params2.progCd = list[i].progCd
                    const cdLst = await fetchPgmMngLst(params2)
                    if (cdLst.length > 0) {
                        alert(`이미 등록된 자료가 있어 저장할 수 없습니다.[${list[i].progCd}]`)
                        return
                    }
                }

                list[i].coId = watch('coId')
                list[i].userId = watch('userId')
                const rowData = list[i]
                array.push(rowData)
            }
        }

        if (array.length === 0) {
            alert(getMessage('common_validation_005'))
            return
        }

        confirm(getMessage('common_msg_003', { status: '저장' }), {
            okCallBack() {
                //insert
                fetchInsertPgmMngLst(array, insertPgmMngLstCallback)
            }
        })
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
                        <button className="btn outline-button" onClick={handleOnClickReset}>초기화</button>
                        <button className="btn btn-gray" onClick={handleOnClickSubmit}>조회</button>
                    </div>
                </div>
            </div>
            <div className="component">
                <ProgramMngGrid
                    data={pgmLst}
                    gridRef={gridRef}
                    setIsLoading={setIsLoading}
                    handleApplyAddRow={handleApplyAddRow}
                    handleApplyRemoveRow={handleApplyRemoveRow}
                    handleRowClick={handleRowClick}
                    handleSaveBtn={handleSaveBtn}
                />
            </div>
        </>
    )
}

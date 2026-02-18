import CrdtCardAckGdohRegPopGrid from 'pages/cps/closing/closingmng/CrdtCardAckGdohRegPopGrid'
import CrdtCardAckGdohRegPopSearchForm from 'pages/cps/closing/closingmng/CrdtCardAckGdohRegPopSearchForm'
import CrdtCardAckGdohRegPopSaveForm from 'pages/cps/closing/closingmng/CrdtCardAckGdohRegPopSaveForm'
import { useEffect, useRef, useState } from 'react'
import 'assets/style/common.scss'
import 'assets/style/grid.scss'
import { getMessage } from 'utils/message-util'
import { useTabulator } from 'hooks/common/use-tabulator'
import { useForm } from 'react-hook-form'
import { useAlert, useConfirm } from 'hooks/popup/use-common-popup'
import Loading from 'components/common/loading/Loading'
import { useKeyEscClose } from 'utils/modal-util'
import { isObjectEmpty } from 'utils/validation-util'
import {
  fetchInsertCrdtCustWaccLst,
  fetchCrdtCardAckLst,
} from 'api/cps/closing/closingmng/CrdtCardAckGdohRegPop-fetch'
import { getSiteUserInfoFromSessionStorage, getUserInfoFromSessionStorage } from 'utils/user-info-storage-util'

/**
 * @author jangjaehyun
 * @date 2024.09.26
 * @description 신용카드 승인정보 시재 등록
 * @returns
 */

export default function CrdtCardAckGdohRegPop(props) {
  const session = { ...getUserInfoFromSessionStorage(), ...getSiteUserInfoFromSessionStorage() }

  const initValues = {
    coId: session.coId,
    userId: session.userid,
    shopId: props.shopId,
    shopNm: props.shopNm,
    cardAckDt: props.cardAckDt,
    ackTypeCd: '',
    ackMinAmt: null,
    ackMaxAmt: null,
    waccCd: '',
    custId: '',
    mchnId: '',
    selectAckAmt: '',
    crdtCrdAckNo: '',
    steId: props.steId,
  }

  /**
   * 그리드 컬럼 세팅
   */
  const { register, reset, control, watch, getValues, setValue } = useForm({
    // 초기값 지정
    defaultValues: initValues,
  })
  const [open, setOpen] = useState(true)
  // const [isLoading, setIsLoading] = useState(false)

  const CrdtCardAckGdohRegListGridRef = useRef(null)
  const CrdtCardAckGdohRegListTabulator = useTabulator({ ref: CrdtCardAckGdohRegListGridRef })

  const [confData, setConfData] = useState(null)
  const [confData2, setConfData2] = useState(null)
  const [unProcCnt, setUnProcCnt] = useState(0)
  const [unProcCancelCnt, setUnProcCancelCnt] = useState(0)

  const { alert } = useAlert()
  const { confirm } = useConfirm()

  const multiSelect = !props.multiSelectYn ? 1 : props.multiSelectYn

  const selectData = function (data) {

    let selectAmtSum = 0
    for (const row of CrdtCardAckGdohRegListTabulator.selectedData()) {
      selectAmtSum += row.crdtCrdAckAt
    }
    setValue('selectAckAmt', selectAmtSum)

    setConfData(data)
  }

  /**
   * 부모 페이지에 선택데이터 전송
   */
  useEffect(() => {
    if (confData2) {
      props.setIsLoading(true)
      props.onConfirm([confData2])
      handleClose()
    }
  }, [confData2])
  
  /**
   *  조회
   */
  const handleSearch = (param) => {
    if (param && param === 'U') {
      search()
    } else {
      const updData =  CrdtCardAckGdohRegListGridRef?.current?.getData().filter((mod) => mod.status === 'M')
      if (updData != undefined && !isObjectEmpty(updData)) {
        confirm('변경사항이 있습니다. 계속 진행하시겠습니까?', {
          okCallBack() {
            search()
          },
        })
      } else {
        search()
      }
    }
  }

  /**
   *  조회 실행
   */
  const search = () => {
    if (getValues('shopId') === '') {
      alert('점포코드는 필수 항목입니다.');
      return
    }

    if (getValues('CrdtCardAckDt') === '') {
      alert('승인일자는 필수 항목입니다.');
      return
    }

    setValue('selectAckAmt', 0)

    props.setIsLoading(true)
    CrdtCardAckGdohRegListTabulator.searchFromServer({
      coId: session.coId,
      userId: session.userid,
      shopId: getValues('shopId'),
      steId: getValues('steId'),
      cardAckDt: getValues('cardAckDt'),
      ackTypeCd: getValues('ackTypeCd'),
      ackMinAmt: getValues('ackMinAmt'),
      ackMaxAmt: getValues('ackMaxAmt'),
      mchnId: getValues('mchnId'),
      crdtCrdAckNo: getValues('crdtCrdAckNo')
    })

    fetchCrdtCardAckLst({
      coId: session.coId,
      userId: session.userid,
      shopId: getValues('shopId'),
      cardAckDt: getValues('cardAckDt'),
    }, (result, data) => {
      if (result) {
        const normalCnt = data.filter((item) => item.crdtCrdAckClCd === 'S' && item.salesGdohWaccCd === null).length
        const cancelCnt = data.filter((item) => item.crdtCrdAckClCd === 'H' && item.salesGdohWaccCd === null).length
        setUnProcCnt(normalCnt)
        setUnProcCancelCnt(cancelCnt)
      }
    })  
  }
  
  /**
   * 저장 후 콜백 함수
   */  
  const saveCrdtCustWaccCallback = (result, data) => {
    if (result) {
      //저장 후 다시 조회
      alert(getMessage('common_msg_001', { status: '저장' }), {
        okCallBack() {
          handleSearch('U') 
        },
      })
    }
  }

  /**
   *  적용
   */
  const handleApply = () => {
    if (props.isLoading) {
      alert('전송 중입니다.')
      return
    }

    if (getValues('custId') === '' || getValues('custId') === null) {
      alert('적용할 코드유형을 선택하시기 바랍니다.')
      return
    }
    if (getValues('waccCd') === '' || getValues('waccCd') === null) {
      alert('적용할 전기코드를 선택하시기 바랍니다.')
      return
    }
    for (const row of CrdtCardAckGdohRegListTabulator.selectedRows()) {
      if (row.getData().crdtCrdAckClCd === 'S' && getValues('waccNm').indexOf('취소') > 0) {
        alert(`전기코드 적용이 잘못되었습니다.  [${row.getData().rowNum} 행]`)
        return
      }
      if (row.getData().crdtCrdAckClCd === 'H' && getValues('waccNm').indexOf('취소') < 0) {
        alert(`전기코드 적용이 잘못되었습니다.  [${row.getData().rowNum} 행]`)
        return
      }

      row.update({
        'custId': getValues('custId'),
        'custNm': getValues('custNm'),
        'salesGdohWaccCd': getValues('waccCd'),
        'salesGdohWaccNm': getValues('waccNm'),
        'userId': session.userid,
        'status': 'M'
      })
    }
  }

  /**
   *  저장
   */
  const handleSave = (e) => {
    e.preventDefault()
    const saveData = CrdtCardAckGdohRegListTabulator.getData().filter((item) => item.status === 'M')

    if (props.isLoading) {
      alert('전송 중입니다.')
      return
    }

    if (saveData.length === 0) {
      alert(getMessage('common_validation_005'))
      return
    }

    confirm(getMessage('common_msg_003', { status: '저장' }), {
      okCallBack() {
        //insert
        fetchInsertCrdtCustWaccLst(saveData, saveCrdtCustWaccCallback)
      }
    })
  }

  /**
   *  시재마감 테이블에 저장
   */
  const handleSaveClick = () => {
    // 선택된 행들을 가져옵니다
    // const selectedRows = CrdtCardAckGdohRegListGridRef.current.getSelectedRows()
    // if (selectedRows.length === 0) {
    //   alert('적용할 대상을 선택하시기 바랍니다.')
    //   return
    // }

    // if (getValues('custId') === '' || getValues('custId') === null) {
    //   alert('고객은 필수 항목입니다.');
    //   return
    // }

    // if (getValues('waccCd') === '' || getValues('waccCd') === null) {
    //   alert('전기코드는 필수 항목입니다.');
    //   return
    // }

    // // 선택된 데이터를 추출합니다
    // const selectedGdNos = selectedRows.map((row) => row.getData())

    // // fetchSaveCrdtCardAck(params, saveCrdtCardAckCallback)
    // selectedGdNos[0].custId = getValues('custId')
    // selectedGdNos[0].salesGdohWaccCd = getValues('waccCd')
    const params = {
      coId: session.coId,
      userId: session.userid,
      shopId: props.shopId,
      cardAckDt: props.cardAckDt,
    }
    if (props.isLoading) {
      alert('전송 중입니다.')
      return
    }
    props.onConfirm(params)
    // props.onClose()
  }

  /**
   *   초기화
   */
  const handleReset = () => {
    // reset()
    setValue('searchShop', '')
    setUnProcCnt(0)
    setUnProcCancelCnt(0)
    CrdtCardAckGdohRegListTabulator.resetData() // 2023-11-01 초기화 조회조건만하도록 공통 적용으로인해 주석처리(tak)
  }

  /**
   * 팝업 닫기
   */
  const handleClose = () => {
    if (props.isLoading) {
      alert('전송 중입니다.')
      return
    }

    setOpen((prev) => !prev)
    props.onClose()
  }

  useKeyEscClose(handleClose)

  return (
    <>
      {props.isLoading && <Loading />}
      <div className={`popup-deemed ${open ? 'show' : 'hide'}`}>
        <div className="deemed"></div>

        <div className="popup grid-type breadcrumb col3">
          <div className="popup-head">
            <h3 className="popup-title">카드시재내역 등록</h3>
            <button type="button" className="btn icon close" onClick={() => handleClose()}>
              닫기
            </button>
          </div>
          <div className="popup-body">
            <div>
              <div className="search-top-group">
                <button className="btn outline-button" type="button" onClick={() => handleReset()}>
                  초기화
                </button>
                <button className="btn btn-gray" type="button" onClick={() => handleSearch()}>
                  조회
                </button>
              </div>
              <CrdtCardAckGdohRegPopSearchForm
                control={control}
                register={register}
                setValue={setValue}
                getValues={getValues}
                watch={watch}
                handleSearch={handleSearch}
                gridRef={CrdtCardAckGdohRegListGridRef}
              />
            </div>
            <div className="component h400">
              <CrdtCardAckGdohRegPopGrid
                CrdtCardAckGdohRegListGridController={CrdtCardAckGdohRegListTabulator}
                ref={CrdtCardAckGdohRegListGridRef}
                selectData={selectData}
                multiSelectYn={multiSelect || 1}
                setIsLoading={props.setIsLoading}
                handleApply={handleApply}
                handleSave={handleSave}
                unProcCnt={unProcCnt}
                unProcCancelCnt={unProcCancelCnt}
              />
            </div>
            <div className="component h100 mt24">
              <CrdtCardAckGdohRegPopSaveForm
                control={control}
                register={register}
                setValue={setValue}
                getValues={getValues}
                watch={watch}
                handleSearch={handleSearch}
              />
            </div>
          </div>
          <div className="popup-bottom">
            <div className="popup-bottom-btn">
              <button type="button" className="btn outline-button" onClick={() => handleClose()}>
                취소
              </button>
              <button type="button" className="btn" onClick={handleSaveClick}>
                전송
              </button>
            </div>
          </div>
        </div>
      </div >
    </>
  )
}

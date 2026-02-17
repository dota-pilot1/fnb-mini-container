import React, { useRef, useEffect, useState } from 'react'
import useDidMountEffect from 'pages/common/customHooks/CustomHooks'
import { useForm } from 'react-hook-form'
import { getUserInfoFromSessionStorage } from 'utils/user-info-storage-util'
import { useTabulator } from 'hooks/common/use-tabulator'
import Loading from 'components/common/loading/Loading'
import { getMessage } from 'utils/message-util'
import MnulCustSalesRegSearchForm from 'components/cps/closing/closingmng/MnulCustSalesRegSearchForm'
import MnulCustSalesRegGrid from 'components/cps/closing/closingmng/MnulCustSalesRegGrid'
import { ApiSelectMnulCustSalesRegList } from 'api/cps/closing/closingmng/MnulCustSalesReg-fetch'

export default function MnulCustSalesReg() {
  const { coId, userid } = getUserInfoFromSessionStorage()
  const initValues = { shopId: '', steId: '', prmtId: '' }

  const { register, reset, control, watch, setValue } = useForm({ defaultValues: initValues })
  const [searchParams, setSearchParams] = useState({}) // 목록 요청 파라메타
  const [salesRegLstData, setSalesRegLstData] = useState([]) // 수기매출등록 목록

  const [closeYn, setCloseYn] = useState('')

  const salesRegLstRef = useRef([])
  const salesRegLstTabulator = useTabulator({ ref: salesRegLstRef })

  /**
   * 조회 파라미터 셋팅
   * @param {*} value
   */
  const setSearchValue = (value) => {
    setSearchParams({
      ...value,
      coId: coId,
      regrId: userid,
    })
  }

  /**============================================================================================
   * 수기매출등록 목록 조회 api
   * ============================================================================================
   */
  const { postListRefetch, isListFetching } = ApiSelectMnulCustSalesRegList(searchParams, setSalesRegLstData, setCloseYn)

  /**
   *  초기화 버튼 클릭 및 조회시 그리드 Reset 이벤트
   * @param {*}
   */
  const handleReset = (arg) => {
    salesRegLstRef?.current?.clearData()
    setSalesRegLstData([])

    if (arg === 'ALL') {
      setSearchParams({})
    }
  }

  /**
   * 수기매출등록 목록 조회
   */
  useDidMountEffect(() => {
    if (Object.keys(searchParams).length > 0 && searchParams.isSearch) {
      handleReset()
      postListRefetch()
    }
  }, [searchParams])

  /**
   * 수기매출등록 목록 조회
   */
  const getSalesRegList = () => {
    if (!searchParams.steId || !searchParams.steNm) {
      alert(getMessage('common_validation_020', { target: '사이트코드' }), {
        okCallBack() {
          setFocus('steId')
        },
      })
      return
    }
    // 그리드 초기화
    handleReset()

    // 조회 호출
    if (Object.keys(searchParams).length === 0) {
      setSearchValue(searchParams)
    } else {
      postListRefetch()
    }
  }
  
  /**
   * 포커스 세팅
   */
  const setFocus = (arg) => {
    //포커스 이동
    document.getElementById(arg).focus()
  }

  return (
    <>
      {isListFetching ? <Loading /> : <></>}
      <MnulCustSalesRegSearchForm
        setSearchValue={setSearchValue}
        register={register}
        reset={reset}
        control={control}
        watch={watch}
        setValue={setValue}
        masterGridRef={salesRegLstRef}
        handleReset={handleReset}
        setFocus={setFocus}
      />
      <div className="component h100p">
        <MnulCustSalesRegGrid
          searchParams={searchParams}
          salesRegLstData={salesRegLstData}
          salesRegLstRef={salesRegLstRef}
          salesRegLstTabulator={salesRegLstTabulator}
          getSalesRegList={getSalesRegList}
          closeYn={closeYn}
        />
      </div>
    </>
  )
}

import React, { useRef, useState } from 'react'
import useDidMountEffect from 'pages/common/customHooks/CustomHooks'
import { useForm } from 'react-hook-form'
import { useConfirm } from 'hooks/popup/use-common-popup'
import { getUserInfoFromSessionStorage } from 'utils/user-info-storage-util'
import { getMessage } from 'utils/message-util'
import { isEmptyStr, isObjectEmpty } from 'utils/validation-util'
import Loading from 'components/common/loading/Loading'
import GdohRegCnclAprvSearchForm from 'components/cps/closing/closingmng/GdohRegCnclAprvSearchForm'
import GdohRegCnclAprvGrid from 'components/cps/closing/closingmng/GdohRegCnclAprvGrid'
import { ApiSelectGdohRegCnclAprvLst } from 'api/cps/closing/closingmng/gdohregcnclaprv-fetch'

export default function GdohRegCnclAprv() {
  const { coId, userid } = getUserInfoFromSessionStorage()
  const initValues = { shopId: '', steId: '', prmtId: '' }
  const { confirm } = useConfirm()

  const { register, reset, control, watch, setValue } = useForm({ defaultValues: initValues })
  const [searchParams, setSearchParams] = useState({}) // POS 행사 목록 요청 파라메타
  const [gdohAprvLstData, setGdohAprvLstData] = useState([]) // 행사 목록

  const gdohAprvLstRef = useRef([])

  /**
   * 시재 등록/취소 목록 조회 파라미터 셋팅
   * @param {*} value
   */
  const setSearchValue = (value) => {
    /*************************************
     * isSearch : TRUE : 조회처리
     * isSearch : FALSE : 조회처리 안함
     *************************************/
    if (value.isSearch) {
      const sDate = value.occrStartDt
      const eDate = value.occrTmntDt

      if (isEmptyStr(sDate) || isEmptyStr(eDate)) {
        alert(getMessage('common_validation_020', { target: '조회기간' }))
        return
      }

      const modifyMasterData = gdohAprvLstRef?.current?.getData().filter((mod) => mod.status !== 'R')

      /*************************************
       * isRetry : TRUE : 수정사항 관계없이 재조회
       * isRetry : FALSE : 수정사항 확인 후 재조회
       *************************************/
      if (isObjectEmpty(modifyMasterData) || value.isRetry) {
        setSearchParams({
          ...value,
          coId: coId,
          regrId: userid,
        })
      } else {
        confirm('변경사항이 있습니다. 계속 진행하시겠습니까?', {
          okCallBack() {
            setSearchParams({
              ...value,
              coId: coId,
              regrId: userid,
            })
          },
        })
      }
    } else {
      setSearchParams({
        ...value,
        coId: coId,
        userid: userid,
      })
    }
  }

  /**============================================================================================
   * 시재 등록/취소 목록 조회 api
   * ============================================================================================
   */
  const { postListRefetch, isListFetching } = ApiSelectGdohRegCnclAprvLst(searchParams, setGdohAprvLstData)

  /**
   *  초기화 버튼 클릭 및 조회시 그리드 Reset 이벤트
   * @param {*}
   */
  const handleReset = (arg) => {
    gdohAprvLstRef?.current?.clearData()
    setGdohAprvLstData([])

    if (arg === 'ALL') {
      setSearchParams({})
      setFocus('shpId')
      gdohAprvLstRef?.current?.clearSort()
    }
  }

  /**
   * 시재 등록/취소 목록 조회
   */
  useDidMountEffect(() => {
    if (Object.keys(searchParams).length > 0 && searchParams.isSearch) {
      handleReset()

      postListRefetch()
    }
  }, [searchParams])

  /**
   * 시재 등록/취소 조회
   */
  const getGdohAprvList = () => {
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
      <GdohRegCnclAprvSearchForm
        setSearchValue={setSearchValue}
        register={register}
        reset={reset}
        control={control}
        watch={watch}
        setValue={setValue}
        masterGridRef={gdohAprvLstRef}
        handleReset={handleReset}
        setFocus={setFocus}
      />
      <div className="component h100p">
        <GdohRegCnclAprvGrid
          setSearchValue={setSearchValue}
          searchParams={searchParams}
          gdohAprvLstData={gdohAprvLstData}
          gdohAprvLstRef={gdohAprvLstRef}
        />
      </div>
    </>
  )
}

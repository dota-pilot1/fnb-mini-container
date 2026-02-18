import axiosInstance from 'api/axios'
import { useCommonApiQuery } from 'hooks/common/use-common-api-query'
import { cloneDeep } from 'lodash'

/* 기본 API 주소 */
const API_DEFAULT = 'api/closing/closingmng/'
/* 수기매출등록 목록 조회 */
export const API_MNULCUSTSALESREG_LIST = API_DEFAULT + 'v1.0/selectMnulCustSalesRegList'
/* 수기매출등록 수정&저장 */
export const API_MNULCUSTSALESREG_SAVE = API_DEFAULT + 'v1.0/updateMnulCustSalesRegList'
export const API_MNULCUSTSALESREG_CLOSEYN = API_DEFAULT + 'v1.0/selectMnulCustSalesRegCloseYn'
/* 수기매출등록 매출확정 */
export const API_MNULCUSTSALESREG_CONFIRM = API_DEFAULT + 'v1.0/confirmMnulCustSalesRegList'
/* 수기매출등록 매출취소 */
export const API_MNULCUSTSALESREG_CANCEL = API_DEFAULT + 'v1.0/cancelMnulCustSalesRegList'

export const ApiSelectMnulCustSalesRegList = (searchUseForm, setData, setCloseYn) => {
  const { isFetching: isListFetching, refetch: postListRefetch } = useCommonApiQuery({
    options: {
      enabled: false,
      onSuccess: async (res) => {
        const resData = await cloneDeep(res.data)
        setData(resData)
        setCloseYn(res.lastPage)
      },
    },
    params: searchUseForm,
    apiConfig: 'post',
    url: API_MNULCUSTSALESREG_LIST,
    queryParamBoolean: true,
  })
  return { postListRefetch, isListFetching }
}

export const ApiUpdateMnulCustSalesRegList = (params) => {
  return axiosInstance
    .post(API_MNULCUSTSALESREG_SAVE, params)
    .then((response) => {
      const { data } = response.data
      return data
    })
    .catch((error) => {
      throw error
    })
}

export const ApiConfirmMnulCustSalesRegList = (params) => {
  return axiosInstance
    .post(API_MNULCUSTSALESREG_CONFIRM, params)
    .then((response) => {
      const { data } = response.data
      return data
    })
    .catch((error) => {
      throw error
    })
}

export const ApiCancelMnulCustSalesRegList = (params) => {
  return axiosInstance
    .post(API_MNULCUSTSALESREG_CANCEL, params)
    .then((response) => {
      const { data } = response.data
      return data
    })
    .catch((error) => {
      throw error
    })
}

//수기매출등록 마감여부 조회
export const fetchMnulCustSalesRegCloseYn = (params, callback) => {
  return axiosInstance.post(API_MNULCUSTSALESREG_CLOSEYN, params)
      .then((response) => {
          const { data } = response.data;
          return callback(true, data);
      })
      .catch((error) => {
          return callback(false, error);
      });
};

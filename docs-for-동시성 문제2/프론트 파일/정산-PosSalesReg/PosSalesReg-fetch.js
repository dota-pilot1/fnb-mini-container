import axiosInstance from 'api/axios';

const API_DEFAULT = "/api/closing/closingmng/"
const API_CALC_DTL = API_DEFAULT + 'v1.0/calcDtl'
const API_PAYSALES_LIST = API_DEFAULT + 'v1.0/paySalesLst'
const API_PAYMENT_LIST = API_DEFAULT + 'v1.0/paymentLst'
const API_PAYMENTDTL_LIST = API_DEFAULT + 'v1.0/paymentDtlLst'
const API_SENDSALES_LIST = API_DEFAULT + 'v1.0/sendSales'
const API_SENDGDOH_LIST = API_DEFAULT + 'v1.0/sendGdoh'
const API_INSERT_POSSALES = API_DEFAULT + 'v1.0/insertPosSales'
const API_CLOSEYN = API_DEFAULT + 'v1.0/closeYn'
const API_INSERT_ONCRDTTAXPBL = API_DEFAULT + 'v1.0/insertOncrdtTaxPblLst'
const API_COMPARE_CNT = API_DEFAULT + 'v1.0/selectPosSapCompareCnt'
const API_DELETE_SUM = API_DEFAULT + 'v1.0/deleteSalesSum'

//POS매출등록 정산 상세내역 조회
export const fetchCalcDtlBySearchParams = (params, callback) => {
    return axiosInstance.post(API_CALC_DTL, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 매출 세부내역 조회
export const fetchPaySalesLstBySearchParams = (params, callback) => {
    return axiosInstance.post(API_PAYSALES_LIST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 결제수단별 내역 조회
export const fetchPaymentLstBySearchParams = (params, callback) => {
    return axiosInstance.post(API_PAYMENT_LIST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 결제수단별 상세내역 조회
export const fetchPaymentDtlLstBySearchParams = (params, callback) => {
    return axiosInstance.post(API_PAYMENTDTL_LIST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 매출전송 내역 조회
export const fetchSendSalesBySearchParams = (params, callback) => {
    return axiosInstance.post(API_SENDSALES_LIST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 시재전송 내역 조회
export const fetchSendGdohBySearchParams = (params, callback) => {
    return axiosInstance.post(API_SENDGDOH_LIST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 저장
export const fetchInsertPosSales = (params, callback) => {
    return axiosInstance.post(API_INSERT_POSSALES, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 마감여부 조회
export const fetchCloseYnBySearchParams = (params, callback) => {
    return axiosInstance.post(API_CLOSEYN, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//세금계산서 발행 저장
export const fetchInsertOncrdtTaxPblLst = (params, callback) => {
    return axiosInstance.post(API_INSERT_ONCRDTTAXPBL, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//POS매출등록 대사결과
export const selectPosSapCompareCnt = (params, callback) => {
    return axiosInstance.post(API_COMPARE_CNT, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });

};


// 매출 재집계를 위한 기존 매출 합계 데이터 삭제
export const deleteSalesSum = (params, callback) => {
    return axiosInstance.post(API_DELETE_SUM, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });

};
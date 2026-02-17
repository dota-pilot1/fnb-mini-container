import axiosInstance from 'api/axios';

const API_DEFAULT = "/api/closing/closingmng/"
const API_CUST_LST = API_DEFAULT + 'v1.0/crdtCardAckCustLst'
const API_WACC_LST = API_DEFAULT + 'v1.0/crdtCardAckWaccLst'
const API_VAN_LST = API_DEFAULT + 'v1.0/crdtCardAckVanLst'
const API_SAVE_ACKLST = API_DEFAULT + 'v1.0/insertCrdtCardAckLst'
const API_SAVE_CUSTWACCLST = API_DEFAULT + 'v1.0/insertCrdtCustWaccLst'
const API_CRDTACK_LST = API_DEFAULT + 'v1.0/crdtCardAckGdohRegLst'


//신용카드 승인내역 목록 조회
export const fetchCrdtCardAckLst = (params, callback) => {
    return axiosInstance.post(API_CRDTACK_LST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//신용카드 승인내역 시재등록 고객 목록 조회
export const fetchCrdtCardAckCustLst = (params, callback) => {
    return axiosInstance.post(API_CUST_LST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//신용카드 승인내역 시재등록 전기코드 목록 조회
export const fetchCrdtCardAckWaccLst = (params, callback) => {
    return axiosInstance.post(API_WACC_LST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//신용카드 승인내역 시재등록 단말기 목록 조회
export const fetchCrdtCardAckVanLst = (params, callback) => {
    return axiosInstance.post(API_VAN_LST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//신용카드 승인내역 시재등록 저장
export const fetchInsertCrdtCardAckLst = (params, callback) => {
    return axiosInstance.post(API_SAVE_ACKLST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};

//신용카드 고객사 시재내역 저장
export const fetchInsertCrdtCustWaccLst = (params, callback) => {
    return axiosInstance.post(API_SAVE_CUSTWACCLST, params)
        .then((response) => {
            const { data } = response.data;
            return callback(true, data);
        })
        .catch((error) => {
            return callback(false, error);
        });
};
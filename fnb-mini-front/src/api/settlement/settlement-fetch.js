import axiosInstance from '@/lib/axios'

export const fetchShopList = () =>
  axiosInstance.get('/api/settlements/shops').then((res) => res.data.data)

export const fetchDailySales = (params) =>
  axiosInstance.get('/api/settlements/daily-sales', { params }).then((res) => res.data.data)

export const fetchSettlementList = (params) =>
  axiosInstance.get('/api/settlements', { params }).then((res) => res.data.data)

export const fetchSettle = (data) =>
  axiosInstance.post('/api/settlements/settle', data).then((res) => res.data)

export const fetchRetrySettlement = (id) =>
  axiosInstance.post(`/api/settlements/${id}/retry`).then((res) => res.data)

import axiosInstance from '@/lib/axios'

/**
 * PaginationTabulator에서 사용 할 axios
 * @typedef RequestConfig
 * @type {string} url
 * @type {object} config
 * @type {object} params
 * @param {RequestConfig} config
 * @returns
 */
export const fetchTabulatorData = ({ url, config, params } = {}) => {
  const { method = 'get' } = config || {}
  const methodLowerCase = method.toLocaleLowerCase()

  return axiosInstance.request({
    ...config,
    url,
    params: methodLowerCase === 'get' && params,
    data: ['put', 'post', 'delete', 'patch'].includes(methodLowerCase) && params,
  })
}

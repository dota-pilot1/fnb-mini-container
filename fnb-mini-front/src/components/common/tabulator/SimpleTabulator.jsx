import { RowStatus } from '@/constants/row-status-const'
import React, { forwardRef } from 'react'
import Tabulator from './Tabulator'
import '@/assets/style/grid_full.scss'

/**
 * Simple Tabulator HOC
 *
 * 페이징 없이 데이터를 따로 조회하여 그리드에 노출할 때 사용
 * Tabulator.info를 참고하여 옵션들을 props으로 이용 가능
 * events의 경우 events props로 이용 (ex. events={{ cellClick: userCustomCellClick }})
 *
 * @param {Tabulator} WrappedComponent
 * @returns
 */
const withSimpleTabulator = (WrappedComponent) => {
  const SimpleTabulator = (props, ref) => {
    const hasPropsIndex = props.index && props.index !== 'id'
    const addPropsDataCustomProperties = (data = []) => {
      return (
        data.map((d, i) => {
          const customData = {
            ...d,
            status: d.status || RowStatus.READ.value,
            statusName: RowStatus.READ.name,
          }
          if (!hasPropsIndex) {
            customData.id = (i + 1).toString()
          }
          return customData
        }) || []
      )
    }
    const redefineProps = () => {
      return {
        ...props,
        pagination: false,
        paginationMode: 'local',
        columnDefaults: {
          tooltip: false,
          headerTooltip: false,
        },
        data: addPropsDataCustomProperties(props.data),
        ajaxResponse(url, params, response) {
          const customResponse = {
            ...response,
            data: addPropsDataCustomProperties(response?.data),
          }
          if (typeof props?.ajaxResponse === 'function') {
            return props.ajaxResponse(url, params, customResponse)
          } else {
            return customResponse.data
          }
        },
      }
    }

    return <WrappedComponent {...redefineProps()} ref={ref} />
  }

  return forwardRef(SimpleTabulator)
}

export default withSimpleTabulator(Tabulator)

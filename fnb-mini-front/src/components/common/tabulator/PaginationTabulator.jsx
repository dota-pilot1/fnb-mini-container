import { RowStatus } from '@/constants/row-status-const'
import { forwardRef } from 'react'
import Tabulator from './Tabulator'

/**
 * Pagination Tabulator HOC
 *
 * 페이징이 필요한 그리드에 노출할 때 사용
 * Tabulator.info를 참고하여 옵션들을 props으로 이용 가능
 * events의 경우 events props로 이용 (ex. events={{ cellClick: userCustomCellClick }})
 *
 * @param {Tabulator} WrappedComponent
 * @returns
 */
const withPaginationTabulator = (WrappedComponent) => {
  const PaginationTabulator = (props, ref) => {
    const setPageButtons = () => {
      const table = ref.current
      const pageModule = table.modules.page
      const pageMax = table.getPageMax()
      const currentPage = table.getPage()
      const paginationButtonCount = table.options.paginationButtonCount
      let min =
        currentPage % paginationButtonCount === 0
          ? currentPage - (paginationButtonCount - 1)
          : Math.floor(currentPage / paginationButtonCount) * paginationButtonCount + 1

      while (pageModule.pagesElement.firstChild) {
        pageModule.pagesElement.removeChild(pageModule.pagesElement.firstChild)
      }

      if (currentPage - paginationButtonCount <= 0) {
        pageModule.firstBut.classList.add('hide')
        pageModule.prevBut.classList.add('hide')
        pageModule.firstBut.disabled = true
        pageModule.prevBut.disabled = true
      } else {
        pageModule.firstBut.classList.remove('hide')
        pageModule.prevBut.classList.remove('hide')
        pageModule.firstBut.disabled = false
        pageModule.prevBut.disabled = false
      }

      if (min + paginationButtonCount > pageMax) {
        pageModule.lastBut.classList.add('hide')
        pageModule.nextBut.classList.add('hide')
        pageModule.lastBut.disabled = true
        pageModule.nextBut.disabled = true
      } else {
        pageModule.lastBut.classList.remove('hide')
        pageModule.nextBut.classList.remove('hide')
        pageModule.lastBut.disabled = false
        pageModule.nextBut.disabled = false
      }

      pageModule.pagesElement.appendChild(generatePageInput(currentPage))
      pageModule.pagesElement.appendChild(generatePageButton(pageMax))

      pageModule.footerRedraw()
    }

    const generatePageInput = (value) => {
      const table = ref.current
      const input = document.createElement('input')
      input.setAttribute('type', 'text')
      input.setAttribute('value', value)
      input.style.width = '30px'
      input.style.textAlign = 'center'
      input.style.border = '1px solid #ccc'

      input.addEventListener('blur', (e) => {
        if (e.target.value) {
          table.setPage(parseInt(e.target.value))
        }
      })
      input.addEventListener('input', (e, a) => {
        const targetValue = e.target.value
        e.target.value = targetValue.replaceAll(/[^0-9]+$/g, '')
      })

      input.addEventListener('keydown', (e, a) => {
        if (e.key === 'Enter') {
          if (!e.target.value) {
            alert('페이지 번호를 입력해주세요.')
            return false
          }
          table.setPage(parseInt(e.target.value))
        }
      })

      return input
    }

    const generatePageButton = (page) => {
      const table = ref.current
      const pageModule = table.modules.page
      const button = document.createElement('button')

      button.setAttribute('type', 'button')
      button.setAttribute('role', 'button')

      pageModule.langBind('pagination|page_title', (value) => {
        button.setAttribute('aria-label', `${page} ${value}`)
        button.setAttribute('title', `${page} ${value}`)
      })

      button.setAttribute('data-page', page)
      button.textContent = page
      button.classList.add('tabulator-page')

      return button
    }

    const redefineProps = () => {
      const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || ''
      return {
        ...props,
        pagination: true,
        paginationMode: 'remote',
        paginationCounter: 'rowpage',
        ajaxURL: `${apiBaseUrl}${props.ajaxURL}`,
        columnDefaults: {
          tooltip: false,
          headerTooltip: false,
        },
        ajaxConfig: {
          method: `${props.ajaxConfig === null ? 'GET' : props.ajaxConfig}`,
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
          },
        },
        setPageButtons,
        defaultOptions: {
          paginationSize: 100,
          paginationButtonCount: 1,
          paginationSizeSelector: [50, 100, 150, 200, 250],
          dataReceiveParams: {
            last_page: 'lastPage',
            last_row: 'lastRow',
            data: 'data',
          },
          langs: {
            default: {
              pagination: {
                page_size: '',
                page_title: '페이지',
                first: `
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-bar-left" viewBox="0 0 16 16">
                      <path fill-rule="evenodd" d="M11.854 3.646a.5.5 0 0 1 0 .708L8.207 8l3.647 3.646a.5.5 0 0 1-.708.708l-4-4a.5.5 0 0 1 0-.708l4-4a.5.5 0 0 1 .708 0zM4.5 1a.5.5 0 0 0-.5.5v13a.5.5 0 0 0 1 0v-13a.5.5 0 0 0-.5-.5z"/>
                  </svg>
                `,
                first_title: '처음',
                last: `
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-bar-right" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M4.146 3.646a.5.5 0 0 0 0 .708L7.793 8l-3.647 3.646a.5.5 0 0 0 .708.708l4-4a.5.5 0 0 0 0-.708l-4-4a.5.5 0 0 0-.708 0zM11.5 1a.5.5 0 0 1 .5.5v13a.5.5 0 0 1-1 0v-13a.5.5 0 0 1 .5-.5z"/>
                  </svg>
                `,
                last_title: '마지막',
                prev: `
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" class="bi bi-chevron-left" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
                  </svg>
                `,
                prev_title: '이전',
                next: `
                 <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" class="bi bi-chevron-right" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
                </svg>
                `,
                next_title: '다음',
                all: '전체보기',
              },
            },
          },
          ajaxResponse(url, params, response) {
            const hasPropsIndex = props.index && props.index !== 'id'
            const customResponse = {
              ...response,
              data:
                response?.data?.map((d, i) => {
                  const customData = {
                    ...d,
                    status: RowStatus.READ.value,
                    statusName: RowStatus.READ.name,
                  }
                  if (!hasPropsIndex) {
                    customData.id = (i + 1).toString()
                  }
                  return customData
                }) || [],
            }
            if (typeof props?.ajaxResponse === 'function') {
              return props.ajaxResponse(url, params, customResponse)
            } else {
              return customResponse
            }
          },
        },
      }
    }

    return <WrappedComponent {...redefineProps()} ref={ref} />
  }

  return forwardRef(PaginationTabulator)
}

export default withPaginationTabulator(Tabulator)

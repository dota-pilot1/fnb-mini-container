import axiosInstance from 'api/axios'
import { useEffect } from 'react'
import ReactGA from 'react-ga4'
import { useNavigate } from 'react-router-dom'
import { setMenusAuthAtSessionStorage, setUserInfoAtSessionStorage } from 'utils/user-info-storage-util'
import { useAlert } from 'hooks/popup/use-common-popup'
import { useLocation } from 'react-router-dom'
import { isEmptyStr } from 'utils/validation-util'
import { getCookie } from 'utils/cookie-util'

export default function Auth() {
  const navigate = useNavigate()
  const { alert } = useAlert()

  // 사용자 로그인
  const userLogin = () => {
    axiosInstance
      .get('/user/login', {
        headers: { Logincookie: sessionStorage.getItem('loginCookie') },
      })
      .then((result) => {
        localStorage.setItem('accessToken', result.data.accessToken)
        sessionStorage.setItem('refreshToken', result.data.refreshToken)
        userInfo()
        getCmmnCodeList()
      })
      .catch((e) => {
        console.log('login error >> ', e)
        alert('로그인 오류가 발생했습니다. 관리자에게 문의하세요.')
      })
  }

  // 사용자 로그인
  const ssoLogin = () => {
    axiosInstance
      .get('/sso/login', {
        headers: { Logincookie: sessionStorage.getItem('loginCookie') },
      })
      .then((result) => {
        localStorage.setItem('accessToken', result.data.accessToken)
        sessionStorage.setItem('refreshToken', result.data.refreshToken)
        userInfo()
        getCmmnCodeList()
      })
      .catch((e) => {
        console.log('login error >> ', e)
        alert('로그인 오류가 발생했습니다. 관리자에게 문의하세요.')
      })
  }

  // 사용자 정보 셋팅
  const userInfo = () => {
    axiosInstance
      .post('/api/userInfo/v1.0', {
        headers: { LoginCookie: sessionStorage.getItem('loginCookie') },
      })
      .then((user) => {
        // 사이트 없는경우 redirect
        // if (user?.data?.userInfo?.siteList?.length === 0) {
        //   if (window.confirm('해당 계정에 사이트정보가 없습니다. 관리자에게 문의하세요.')) {
        //     localStorage.removeItem('accessToken')
        //     sessionStorage.removeItem('refreshToken')
        //     sessionStorage.removeItem('loginCookie')
        //     window.close()
        //   }
        //   return
        // } else

        if (user?.data?.menuList?.length === 0) {
          if (window.confirm('해당 계정에 메뉴정보가 없습니다. 관리자에게 문의하세요.')) {
            localStorage.removeItem('accessToken')
            sessionStorage.removeItem('refreshToken')
            sessionStorage.removeItem('loginCookie')
            window.close()
          }
          return
        }

        setUserInfoAtSessionStorage(user?.data?.userInfo)

        // 메뉴 localStroage 셋팅
        localStorage.setItem('sessionMenuList', JSON.stringify(user?.data?.menuList))

        if (user?.progAuthList?.length < 1) {
          alert('메뉴정보 권한이 없습니다. 관리자에게 메뉴권한을 신청하세요.', {
            okCallBack() {
              window.close()
            },
          })
          return
        }

        // session storage 셋팅
        setMenusAuthAtSessionStorage(user?.data?.progAuthList)

        // GA 셋팅
        ReactGA.initialize([
          {
            trackingId: process.env.REACT_APP_GOOGLE_ANALYTICS_TRACKING_ID,
            gtagOptions: {
              debug_mode: true,
              user_id: user?.data?.userInfo?.userid,
            },
          },
        ])

        ReactGA.set({ userId: user?.data?.userInfo?.userid })

        navigate('/main')
      })
  }

  // 공통코드 조회 후 세션스토리지에 저장
  const getCmmnCodeList = () => {
    axiosInstance
      .post('/api/common/code/v1.0/allClCmCodeList', {
        headers: { Logincookie: sessionStorage.getItem('loginCookie') },
      })
      .then((result) => {
        const codes = {}

        result.data.forEach((item) => {
          if (!contains_key(codes, item.commClCd)) {
            codes[item.commClCd] = []
          }
          codes[item.commClCd].push({
            CODE_CD: item.commCd,
            CODE_NM: item.commCdNm,
            CHARTR_REF1: item.chartrRef1,
            CHARTR_REF2: item.chartrRef2,
            CHARTR_REF3: item.chartrRef3,
            CHARTR_REF4: item.chartrRef4,
            DFL_YN: item.dfltYn,
          })
        })
        sessionStorage.setItem('CMMN_CODE_LIST', JSON.stringify(codes))
      })
      .catch((e) => {
        //console.log('get cmmn code error >> ', e)
      })
  }

  // 맵에 키가 존재하는지 여부를 반환한다
  const contains_key = (map, key) => {
    let result = false
    for (const l_key in map) {
      if (key === l_key) {
        result = true
        break
      }
    }
    return result
  }

  useEffect(() => {
    userLogin()
  }, [])

  return <>{/* <div>인증처리중입니다.....</div> */}</>
}

import { createBrowserRouter } from 'react-router-dom'
import App from 'App'
// import Login from 'pages/Login'
import Health from 'pages/Health'
import Auth from 'pages/Auth'

import exampleRouter from './example/index.js'

const originRouter = [
  {
    path: '/',
    element: <Auth />,
  },
  // {
  //   path: '/login',
  //   element: <Login />,
  // },
  {
    path: '/main',
    element: <App />,
  },
  // {
  //   path: '/auth',
  //   element: <Auth />,
  // },
  {
    path: '/health',
    element: <Health />,
  },
]

//example 페이지 router 추가
exampleRouter.forEach((item) => {
  originRouter.push(item);
})

const router = createBrowserRouter(originRouter)

export default router

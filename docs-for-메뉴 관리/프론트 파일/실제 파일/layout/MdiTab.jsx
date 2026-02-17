import { useFwTab } from 'hooks/common/use-fw-tab'
import { useEffect, useRef, useState } from 'react'
import { useResetRecoilState } from 'recoil'
import { dsBdsKnowShortCuts } from 'store/atoms/cmmty/know/menuKnow-popup-atom'

export default function MdiTab() {
  const element = useRef()
  const [open, setOpen] = useState(false)
  const {
    tabs,
    currentTabIndex,
    handleChangeTab,
    handleCloseTab,
    handleNextTab,
    handlePrevTab,
    handleClearTab,
    handleClickHome,
  } = useFwTab()

  // atom 디폴트값으로 값 변경 (바로가기 이동시 recoil 값 제거)
  const resetDsBdsKnowShortCutsAtom = useResetRecoilState(dsBdsKnowShortCuts)

  useEffect(() => {
    if (tabs.length === 0) {
      setOpen((prev) => false)
    }
  }, [tabs])

  const handleTabList = (e, index, menuName) => {
    e.stopPropagation()
    handleChangeTab(index, menuName)
    setOpen((prev) => !prev)
    return false
  }

  const handleAtomReset = () => {
    resetDsBdsKnowShortCutsAtom()
  }

  const handleCloseTabFunction = (index) => {
    handleCloseTab(index)
    handleAtomReset()
  }

  const handleClearTabFunction = () => {
    handleClearTab()
    handleAtomReset()
  }

  // 열린탭 닫기
  useEffect(() => {
    const handler = (e) => {
      if (element.current && !element.current.contains(e.target)) {
        setOpen(false)
      }
    }
    window.addEventListener('click', handler)
    return () => {
      window.removeEventListener('click', handler)
    }
  })

  return (
    <>
      <div className="mdi-tab">
        <ul className="mdi-btn-wrap">
          <li className="home">
            <button className="btn mdi-btn" onClick={handleClickHome}>
              HOME
            </button>
          </li>
          {tabs &&
            tabs.map((item, index) => (
              <li className={item.index === currentTabIndex ? 'active' : ''} key={item.id}>
                <button className="btn mdi-btn" onClick={() => handleChangeTab(item.index, item.menuName)}>
                  {item.menuName}
                </button>
                <button className="btn-x-icon" onClick={() => handleCloseTabFunction(item.index)}>
                  삭제
                </button>
              </li>
            ))}
        </ul>
        <div className="btn-control-area">
          <button className="btn prev" onClick={handlePrevTab}>
            앞으로 이동
          </button>
          <button className="btn next" onClick={handleNextTab}>
            뒤로 이동
          </button>
          <div className="dropdown" ref={element}>
            <button className="btn all" onClick={() => setOpen((prev) => !prev)}>
              전체보기
            </button>
            <div className={`dropdown-area ${open ? `` : `hide`}`}>
              <ul>
                {tabs &&
                  tabs.map((item) => (
                    <li key={item.id} className={item.index === currentTabIndex ? 'active' : ''}>
                      <button onClick={(e) => handleTabList(e, item.index, item.menuName)}>{item.menuName}</button>
                      <button className="btn-x-icon" onClick={() => handleCloseTabFunction(item.index)}>
                        X
                      </button>
                    </li>
                  ))}
                {tabs.length === 0 && (
                  <li>
                    <span className="no-data">현재 열려있는 화면이 없습니다.</span>
                    <button className="btn-x-icon">close</button>
                  </li>
                )}
              </ul>
            </div>
          </div>
          <button className="btn dlt" onClick={() => handleClearTabFunction()}>
            삭제
          </button>
        </div>
      </div>
    </>
  )
}

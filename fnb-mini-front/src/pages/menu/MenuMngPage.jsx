import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import { fetchMenuList, fetchSaveMenu, fetchDeleteMenu } from '@/api/menu/menu-fetch'

/**
 * 메뉴 관리 페이지
 *
 * 실무: pages/cps/system/systemmng/ProgramMng.jsx
 * - Tabulator 그리드로 메뉴 목록 표시
 * - 메뉴 추가/수정/삭제
 *
 * Mini: 심플 테이블 + 상세 폼 (좌우 2단)
 */
export default function MenuMngPage() {
  const queryClient = useQueryClient()
  const [selected, setSelected] = useState(null)
  const [isNew, setIsNew] = useState(false)
  const [form, setForm] = useState(emptyForm())

  const { data: apiRes, isFetching } = useQuery({
    queryKey: ['menuList'],
    queryFn: fetchMenuList,
  })
  const menuList = apiRes?.data ?? []

  const saveMutation = useMutation({
    mutationFn: fetchSaveMenu,
    onSuccess: (res) => {
      toast.success(res.message || '저장되었습니다.')
      queryClient.invalidateQueries({ queryKey: ['menuList'] })
      handleCancel()
    },
    onError: (e) => {
      toast.error(e.response?.data?.message || '저장 중 오류가 발생했습니다.')
    },
  })

  const deleteMutation = useMutation({
    mutationFn: fetchDeleteMenu,
    onSuccess: (res) => {
      toast.success(res.message || '삭제되었습니다.')
      queryClient.invalidateQueries({ queryKey: ['menuList'] })
      handleCancel()
    },
    onError: (e) => {
      toast.error(e.response?.data?.message || '삭제 중 오류가 발생했습니다.')
    },
  })

  // 행 클릭 → 상세 폼에 로드
  const handleRowClick = (menu) => {
    setSelected(menu)
    setIsNew(false)
    setForm({
      id:            menu.id,
      parentId:      menu.parentId ?? '',
      depth:         menu.depth,
      menuName:      menu.menuName,
      componentName: menu.componentName ?? '',
      sortOrder:     menu.sortOrder,
      useYn:         menu.useYn,
    })
  }

  // 신규
  const handleNew = () => {
    setSelected(null)
    setIsNew(true)
    setForm(emptyForm())
  }

  // 취소
  const handleCancel = () => {
    setSelected(null)
    setIsNew(false)
    setForm(emptyForm())
  }

  // 저장
  const handleSave = () => {
    if (!form.menuName.trim()) {
      toast.error('메뉴 이름을 입력하세요.')
      return
    }
    saveMutation.mutate({
      ...form,
      id:       isNew ? null : form.id,
      parentId: form.parentId === '' ? null : Number(form.parentId),
      depth:    Number(form.depth),
      sortOrder: Number(form.sortOrder),
    })
  }

  // 삭제
  const handleDelete = () => {
    if (!selected) return
    if (!confirm(`"${selected.menuName}" 을(를) 삭제하시겠습니까?`)) return
    deleteMutation.mutate(selected.id)
  }

  const loading = isFetching || saveMutation.isPending || deleteMutation.isPending

  return (
    <div className="p-4 space-y-4 max-w-[1400px] mx-auto">
      {/* 타이틀 */}
      <div className="bg-white rounded border p-4">
        <h2 className="text-base font-semibold">메뉴 관리</h2>
      </div>

      {/* 좌우 2단 */}
      <div className="grid grid-cols-[1fr_420px] gap-4 items-start">

        {/* 좌: 목록 테이블 */}
        <div className="bg-white rounded border p-4 space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium">
              메뉴 목록 총 {menuList.length}건
            </span>
            <button
              onClick={handleNew}
              className="px-3 py-1 text-sm bg-slate-700 text-white rounded hover:bg-slate-800"
            >
              신규
            </button>
          </div>

          <table className="w-full text-sm border-collapse">
            <thead>
              <tr className="bg-gray-50">
                <th className="border px-2 py-1.5 text-left w-12">No</th>
                <th className="border px-2 py-1.5 text-left w-16">depth</th>
                <th className="border px-2 py-1.5 text-left">메뉴명</th>
                <th className="border px-2 py-1.5 text-left">컴포넌트</th>
                <th className="border px-2 py-1.5 text-center w-16">순서</th>
                <th className="border px-2 py-1.5 text-center w-14">사용</th>
              </tr>
            </thead>
            <tbody>
              {menuList.length === 0 ? (
                <tr>
                  <td colSpan={6} className="border px-2 py-8 text-center text-gray-400">
                    조회된 데이터가 없습니다.
                  </td>
                </tr>
              ) : (
                menuList.map((menu, idx) => (
                  <tr
                    key={menu.id}
                    onClick={() => handleRowClick(menu)}
                    className={`cursor-pointer hover:bg-blue-50 ${
                      selected?.id === menu.id ? 'bg-blue-100' : ''
                    }`}
                  >
                    <td className="border px-2 py-1.5 text-gray-500">{idx + 1}</td>
                    <td className="border px-2 py-1.5">
                      <span className={`text-xs px-1.5 py-0.5 rounded ${depthBadge(menu.depth)}`}>
                        {depthLabel(menu.depth)}
                      </span>
                    </td>
                    <td className="border px-2 py-1.5" style={{ paddingLeft: depthIndent(menu.depth) }}>
                      {menu.menuName}
                    </td>
                    <td className="border px-2 py-1.5 text-gray-500 text-xs">
                      {menu.componentName ?? '-'}
                    </td>
                    <td className="border px-2 py-1.5 text-center">{menu.sortOrder}</td>
                    <td className="border px-2 py-1.5 text-center">
                      <span className={menu.useYn === 'Y' ? 'text-green-600' : 'text-red-400'}>
                        {menu.useYn}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* 우: 상세 폼 */}
        <div className="bg-white rounded border p-4">
          {!selected && !isNew ? (
            <p className="text-sm text-gray-400 text-center py-12">
              목록에서 메뉴를 선택하거나 [신규] 버튼을 클릭하세요.
            </p>
          ) : (
            <div className="space-y-3">
              <h3 className="text-sm font-semibold border-b pb-2">
                {isNew ? '신규 메뉴' : '메뉴 수정'}
              </h3>

              <Field label="상위 메뉴 ID (parentId)">
                <input
                  type="number"
                  value={form.parentId}
                  onChange={(e) => setForm({ ...form, parentId: e.target.value })}
                  placeholder="비워두면 depth 3 (헤더)"
                  className="input-field"
                />
              </Field>

              <Field label="depth *">
                <select
                  value={form.depth}
                  onChange={(e) => setForm({ ...form, depth: e.target.value })}
                  className="input-field"
                >
                  <option value={3}>3 - 헤더</option>
                  <option value={4}>4 - 사이드 카테고리</option>
                  <option value={5}>5 - 실제 메뉴 (탭)</option>
                </select>
              </Field>

              <Field label="메뉴명 *">
                <input
                  type="text"
                  value={form.menuName}
                  onChange={(e) => setForm({ ...form, menuName: e.target.value })}
                  placeholder="메뉴 이름"
                  className="input-field"
                />
              </Field>

              <Field label="컴포넌트명 (depth 5만)">
                <input
                  type="text"
                  value={form.componentName}
                  onChange={(e) => setForm({ ...form, componentName: e.target.value })}
                  placeholder="예: BrandPage"
                  className="input-field"
                />
              </Field>

              <Field label="순서">
                <input
                  type="number"
                  value={form.sortOrder}
                  onChange={(e) => setForm({ ...form, sortOrder: e.target.value })}
                  className="input-field"
                />
              </Field>

              <Field label="사용여부">
                <select
                  value={form.useYn}
                  onChange={(e) => setForm({ ...form, useYn: e.target.value })}
                  className="input-field"
                >
                  <option value="Y">Y - 사용</option>
                  <option value="N">N - 미사용</option>
                </select>
              </Field>

              <div className="flex gap-2 pt-2">
                <button
                  onClick={handleSave}
                  disabled={saveMutation.isPending}
                  className="flex-1 py-1.5 text-sm bg-slate-700 text-white rounded hover:bg-slate-800 disabled:opacity-50"
                >
                  저장
                </button>
                {!isNew && (
                  <button
                    onClick={handleDelete}
                    disabled={deleteMutation.isPending}
                    className="px-4 py-1.5 text-sm bg-red-500 text-white rounded hover:bg-red-600 disabled:opacity-50"
                  >
                    삭제
                  </button>
                )}
                <button
                  onClick={handleCancel}
                  className="px-4 py-1.5 text-sm border rounded hover:bg-gray-50"
                >
                  취소
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {loading && (
        <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg px-6 py-4 shadow-lg">
            <p className="text-sm">처리 중...</p>
          </div>
        </div>
      )}
    </div>
  )
}

// ── helpers ───────────────────────────────────────────
function emptyForm() {
  return { id: null, parentId: '', depth: 3, menuName: '', componentName: '', sortOrder: 1, useYn: 'Y' }
}

function depthLabel(depth) {
  return { 3: '헤더', 4: '사이드', 5: '메뉴' }[depth] ?? depth
}

function depthBadge(depth) {
  return {
    3: 'bg-blue-100 text-blue-700',
    4: 'bg-orange-100 text-orange-700',
    5: 'bg-green-100 text-green-700',
  }[depth] ?? 'bg-gray-100 text-gray-600'
}

function depthIndent(depth) {
  return { 3: '8px', 4: '20px', 5: '36px' }[depth] ?? '8px'
}

function Field({ label, children }) {
  return (
    <div className="space-y-1">
      <label className="text-xs font-medium text-gray-600">{label}</label>
      {children}
    </div>
  )
}

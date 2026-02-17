import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'

/**
 * 브랜드 검색폼
 *
 * 실무: components/cps/base/brandmng/BrandRtrvSearchForm.jsx
 * - className: search-form grid3, form-line, form-wrap, in-title, form-input
 * - react-hook-form register 사용
 * - 초기화/조회 버튼
 *
 * Mini: Tailwind + shadcn 으로 동일 레이아웃 구현
 */
export default function BrandSearchForm({ register, onSearch, onReset }) {
  return (
    <div className="border rounded-lg bg-white p-4 mb-4">
      {/* 타이틀 영역 - 실무: .title > h3 + buttons */}
      <div className="flex justify-between items-center mb-3">
        <h3 className="text-lg font-semibold">브랜드 관리</h3>
        <div className="flex gap-2">
          <Button variant="outline" type="button" onClick={onReset}>
            초기화
          </Button>
          <Button variant="secondary" type="button" onClick={onSearch}>
            조회
          </Button>
        </div>
      </div>

      {/* 검색폼 - 실무: .search-form.grid3 > .form-line > .form-wrap */}
      <div className="grid grid-cols-4 gap-4 items-end">
        <div className="flex flex-col gap-1">
          <Label htmlFor="brandCode">브랜드코드</Label>
          <Input id="brandCode" placeholder="브랜드코드" {...register('brandCode')} />
        </div>
        <div className="flex flex-col gap-1">
          <Label htmlFor="brandName">브랜드명</Label>
          <Input id="brandName" placeholder="브랜드명" {...register('brandName')} />
        </div>
        <div className="flex flex-col gap-1">
          <Label htmlFor="useYn">사용여부</Label>
          <select
            id="useYn"
            className="h-9 w-full rounded-md border border-input bg-transparent px-3 text-sm"
            {...register('useYn')}
          >
            <option value="">전체</option>
            <option value="Y">Y</option>
            <option value="N">N</option>
          </select>
        </div>
        <div className="flex flex-col gap-1">
          <Label htmlFor="syncStatus">동기화상태</Label>
          <select
            id="syncStatus"
            className="h-9 w-full rounded-md border border-input bg-transparent px-3 text-sm"
            {...register('syncStatus')}
          >
            <option value="">전체</option>
            <option value="NONE">NONE</option>
            <option value="PENDING">PENDING</option>
            <option value="SUCCESS">SUCCESS</option>
            <option value="FAILED">FAILED</option>
          </select>
        </div>
      </div>
    </div>
  )
}

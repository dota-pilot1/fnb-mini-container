import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'

/**
 * 브랜드 상세정보
 *
 * 실무: CLAUDE.md의 상세폼 테이블 스타일 (th/td)
 * - thStyle: #f5f5f5 배경, 13px, nowrap
 * - tdStyle: 6px 10px, border
 * - 2열 레이아웃: th-td-th-td
 * - 필수항목: 라벨 뒤에 * (빨간색)
 *
 * Mini: 동일한 th/td 테이블 패턴 + shadcn Badge
 */

const syncStatusVariant = {
  NONE: 'secondary',
  PENDING: 'outline',
  SUCCESS: 'default',
  FAILED: 'destructive',
}

const thStyle = {
  padding: '8px 12px',
  backgroundColor: '#f5f5f5',
  border: '1px solid #ddd',
  textAlign: 'left',
  fontWeight: 'normal',
  fontSize: '13px',
  whiteSpace: 'nowrap',
  width: '120px',
}

const tdStyle = {
  padding: '6px 10px',
  border: '1px solid #ddd',
}

export default function BrandDetail({ brand, onSave, onRetry, isNew }) {
  const { register, handleSubmit, reset, watch } = useForm()

  useEffect(() => {
    if (brand) {
      reset(brand)
    } else if (isNew) {
      reset({
        brandCode: '',
        brandName: '',
        brandNameEn: '',
        brandDesc: '',
        useYn: 'Y',
        syncStatus: 'NONE',
        syncRetryCount: 0,
        version: 0,
      })
    }
  }, [brand, isNew, reset])

  const syncStatus = watch('syncStatus')
  const currentVersion = watch('version')

  const onSubmit = (data) => {
    const status = isNew ? 'C' : 'U'
    onSave({ ...data, status })
  }

  // 아무것도 선택 안 된 상태
  if (!brand && !isNew) {
    return (
      <div className="border rounded-lg bg-white p-4 flex items-center justify-center min-h-[300px]">
        <p className="text-muted-foreground">
          목록에서 브랜드를 선택하거나 [신규] 버튼을 클릭하세요.
        </p>
      </div>
    )
  }

  return (
    <div className="border rounded-lg bg-white p-4">
      {/* 타이틀 - 실무: .title > h3 + buttons */}
      <div className="flex justify-between items-center mb-3">
        <h4 className="font-medium">상세정보</h4>
        <div className="flex gap-2">
          {syncStatus === 'FAILED' && (
            <Button
              variant="destructive"
              size="sm"
              type="button"
              onClick={() => onRetry(brand?.id)}
            >
              재시도
            </Button>
          )}
          <Button variant="secondary" size="sm" type="button" onClick={handleSubmit(onSubmit)}>
            저장
          </Button>
        </div>
      </div>

      {/* 상세폼 - 실무: search-detail > table (th/td) */}
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <tbody>
          <tr>
            <th style={thStyle}>
              브랜드코드 <span style={{ color: 'red' }}>*</span>
            </th>
            <td style={tdStyle}>
              <Input
                {...register('brandCode', { required: true })}
                disabled={!isNew}
                className="h-8"
              />
            </td>
            <th style={thStyle}>사용여부</th>
            <td style={tdStyle}>
              <select
                {...register('useYn')}
                className="h-8 w-full rounded-md border border-input bg-transparent px-3 text-sm"
              >
                <option value="Y">Y</option>
                <option value="N">N</option>
              </select>
            </td>
          </tr>
          <tr>
            <th style={thStyle}>
              브랜드명 <span style={{ color: 'red' }}>*</span>
            </th>
            <td style={tdStyle}>
              <Input
                {...register('brandName', { required: true })}
                className="h-8"
              />
            </td>
            <th style={thStyle}>브랜드명(EN)</th>
            <td style={tdStyle}>
              <Input {...register('brandNameEn')} className="h-8" />
            </td>
          </tr>
          <tr>
            <th style={thStyle}>브랜드 설명</th>
            <td colSpan={3} style={tdStyle}>
              <Input {...register('brandDesc')} className="h-8" />
            </td>
          </tr>
          <tr>
            <th style={thStyle}>동기화 상태</th>
            <td style={tdStyle}>
              <Badge variant={syncStatusVariant[syncStatus] || 'secondary'}>
                {syncStatus || 'NONE'}
              </Badge>
            </td>
            <th style={thStyle}>재시도 횟수</th>
            <td style={tdStyle}>
              <span className="text-sm">{brand?.syncRetryCount || 0}회 / 3회</span>
            </td>
          </tr>
          {brand?.lastSyncError && (
            <tr>
              <th style={thStyle}>마지막 오류</th>
              <td colSpan={3} style={{ ...tdStyle, color: '#ef4444', fontSize: '12px' }}>
                {brand.lastSyncError}
              </td>
            </tr>
          )}
          {!isNew && (
            <tr>
              <th style={thStyle}>버전</th>
              <td style={tdStyle}>
                <span className="text-sm text-muted-foreground">v{currentVersion || 0}</span>
                <input type="hidden" {...register('version', { valueAsNumber: true })} />
                <input type="hidden" {...register('id', { valueAsNumber: true })} />
              </td>
              <th style={thStyle}>등록일시</th>
              <td style={tdStyle}>
                <span className="text-sm text-muted-foreground">
                  {brand?.regDttm || '-'}
                </span>
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

import { cn } from '@/lib/utils'

interface HobbyChipProps {
  icon: string
  name: string
  active?: boolean
  onClick?: () => void
  className?: string
  showRemove?: boolean
  onRemove?: () => void
}

export default function HobbyChip({
  icon,
  name,
  active,
  onClick,
  className,
  showRemove,
  onRemove,
}: HobbyChipProps) {
  return (
    <button
      onClick={onClick}
      className={cn('chip', active && 'active', className)}
    >
      <span>{icon}</span>
      <span>{name}</span>
      {showRemove && (
        <span
          onClick={(e) => {
            e.stopPropagation()
            onRemove?.()
          }}
          className="ml-1 hover:opacity-70"
        >
          ×
        </span>
      )}
    </button>
  )
}

import { cn } from '@/lib/utils'

interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg'
  className?: string
}

const sizeClasses = {
  sm: 'w-4 h-4 border-2',
  md: 'w-6 h-6 border-2',
  lg: 'w-8 h-8 border-3',
}

export default function LoadingSpinner({ size = 'md', className }: LoadingSpinnerProps) {
  return (
    <div
      className={cn(
        'animate-spin rounded-full border-secondary border-t-primary dark:border-t-primary-dark',
        sizeClasses[size],
        className
      )}
    />
  )
}

export function PageLoader() {
  return (
    <div className="flex items-center justify-center min-h-[200px]">
      <LoadingSpinner size="lg" />
    </div>
  )
}

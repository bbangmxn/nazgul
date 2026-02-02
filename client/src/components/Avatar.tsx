import { cn, getInitials } from '@/lib/utils'

interface AvatarProps {
  src?: string
  name: string
  size?: 'sm' | 'md' | 'lg' | 'xl'
  className?: string
}

const sizeClasses = {
  sm: 'w-8 h-8 text-xs',
  md: 'w-10 h-10 text-sm',
  lg: 'w-12 h-12 text-base',
  xl: 'w-20 h-20 text-2xl',
}

export default function Avatar({ src, name, size = 'md', className }: AvatarProps) {
  if (src) {
    return (
      <img
        src={src}
        alt={name}
        className={cn('avatar object-cover', sizeClasses[size], className)}
      />
    )
  }

  return (
    <div className={cn('avatar', sizeClasses[size], className)}>
      {getInitials(name)}
    </div>
  )
}

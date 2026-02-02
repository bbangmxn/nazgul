import { clsx, type ClassValue } from 'clsx'
import { formatDistanceToNow } from 'date-fns'
import { ko } from 'date-fns/locale'

export function cn(...inputs: ClassValue[]) {
  return clsx(inputs)
}

export function timeAgo(date: string) {
  return formatDistanceToNow(new Date(date), { addSuffix: true, locale: ko })
}

export function getInitials(name: string) {
  return name.charAt(0).toUpperCase()
}

export function formatNumber(num: number): string {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

export const skillLevelLabels = ['', '입문', '초급', '중급', '고급', '전문가']

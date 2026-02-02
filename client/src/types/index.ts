export interface User {
  id: number
  email: string
  nickname: string
  bio?: string
  profileImage?: string
  followingCount: number
  followersCount: number
  createdAt: string
}

export interface UserSummary {
  id: number
  nickname: string
  profileImage?: string
}

export interface Hobby {
  id: number
  name: string
  description?: string
  icon: string
  category: string
  userCount?: number
}

export interface UserHobby extends Hobby {
  skillLevel: number
}

export interface Post {
  id: number
  author: UserSummary
  content: string
  imageUrl?: string
  hobby?: {
    id: number
    name: string
    icon: string
  }
  likeCount: number
  commentCount: number
  liked: boolean
  createdAt: string
  updatedAt: string
}

export interface Comment {
  id: number
  author: UserSummary
  content: string
  parentId?: number
  replies?: Comment[]
  createdAt: string
  updatedAt: string
}

export interface LoginResponse {
  accessToken: string
  tokenType: string
  user: User
}

export interface Page<T> {
  content: T[]
  totalPages: number
  totalElements: number
  size: number
  number: number
  first: boolean
  last: boolean
  empty: boolean
}

export type HobbyCategory = 
  | 'SPORTS' 
  | 'MUSIC' 
  | 'ARTS' 
  | 'GAMES' 
  | 'OUTDOOR' 
  | 'CULTURE' 
  | 'FOOD' 
  | 'TECH' 
  | 'PETS'

export const HOBBY_CATEGORIES: { key: HobbyCategory; label: string; emoji: string }[] = [
  { key: 'SPORTS', label: '스포츠', emoji: '⚽' },
  { key: 'MUSIC', label: '음악', emoji: '🎵' },
  { key: 'ARTS', label: '예술', emoji: '🎨' },
  { key: 'GAMES', label: '게임', emoji: '🎮' },
  { key: 'OUTDOOR', label: '아웃도어', emoji: '🏕️' },
  { key: 'CULTURE', label: '문화', emoji: '📚' },
  { key: 'FOOD', label: '음식', emoji: '🍽️' },
  { key: 'TECH', label: '테크', emoji: '💻' },
  { key: 'PETS', label: '반려동물', emoji: '🐕' },
]

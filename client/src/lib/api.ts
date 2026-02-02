import axios from 'axios'
import type { User, LoginResponse, Hobby, UserHobby, Post, Comment, Page, UserSummary } from '@/types'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor for auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth
export const authApi = {
  signup: (data: { email: string; password: string; nickname: string }) =>
    api.post<User>('/auth/signup', data),
  
  login: (data: { email: string; password: string }) =>
    api.post<LoginResponse>('/auth/login', data),
}

// User
export const userApi = {
  getMe: () => api.get<User>('/users/me'),
  
  getUser: (userId: number) => api.get<User>(`/users/${userId}`),
  
  updateMe: (data: { nickname?: string; bio?: string; profileImage?: string }) =>
    api.put<User>('/users/me', data),
  
  follow: (userId: number) => api.post(`/users/${userId}/follow`),
  
  unfollow: (userId: number) => api.delete(`/users/${userId}/follow`),
  
  getFollowers: (userId: number) => api.get<UserSummary[]>(`/users/${userId}/followers`),
  
  getFollowing: (userId: number) => api.get<UserSummary[]>(`/users/${userId}/following`),
  
  getRecommended: () => api.get<UserSummary[]>('/users/recommended'),
  
  search: (keyword: string) => api.get<UserSummary[]>('/users/search', { params: { keyword } }),
}

// Hobby
export const hobbyApi = {
  getAll: () => api.get<Hobby[]>('/hobbies'),
  
  getByCategory: (category: string) => api.get<Hobby[]>(`/hobbies/category/${category}`),
  
  search: (keyword: string) => api.get<Hobby[]>('/hobbies/search', { params: { keyword } }),
  
  getMyHobbies: () => api.get<UserHobby[]>('/hobbies/my'),
  
  getUserHobbies: (userId: number) => api.get<UserHobby[]>(`/hobbies/user/${userId}`),
  
  addMyHobby: (data: { hobbyId: number; skillLevel?: number }) =>
    api.post<UserHobby>('/hobbies/my', data),
  
  removeMyHobby: (hobbyId: number) => api.delete(`/hobbies/my/${hobbyId}`),
  
  updateSkillLevel: (hobbyId: number, level: number) =>
    api.patch<UserHobby>(`/hobbies/my/${hobbyId}/skill`, null, { params: { level } }),
}

// Post
export const postApi = {
  create: (data: { content: string; hobbyId?: number; imageUrl?: string }) =>
    api.post<Post>('/posts', data),
  
  get: (postId: number) => api.get<Post>(`/posts/${postId}`),
  
  getFeed: (page = 0, size = 20) =>
    api.get<Page<Post>>('/posts/feed', { params: { page, size } }),
  
  getByHobby: (hobbyId: number, page = 0, size = 20) =>
    api.get<Page<Post>>(`/posts/hobby/${hobbyId}`, { params: { page, size } }),
  
  getRecommended: (page = 0, size = 20) =>
    api.get<Page<Post>>('/posts/recommended', { params: { page, size } }),
  
  getPopular: (page = 0, size = 20) =>
    api.get<Page<Post>>('/posts/popular', { params: { page, size } }),
  
  update: (postId: number, data: { content?: string; imageUrl?: string }) =>
    api.put<Post>(`/posts/${postId}`, data),
  
  delete: (postId: number) => api.delete(`/posts/${postId}`),
  
  like: (postId: number) => api.post(`/posts/${postId}/like`),
  
  unlike: (postId: number) => api.delete(`/posts/${postId}/like`),
}

// Comment
export const commentApi = {
  create: (postId: number, data: { content: string; parentId?: number }) =>
    api.post<Comment>(`/posts/${postId}/comments`, data),
  
  getByPost: (postId: number, page = 0, size = 20) =>
    api.get<Page<Comment>>(`/posts/${postId}/comments`, { params: { page, size } }),
  
  update: (postId: number, commentId: number, data: { content: string }) =>
    api.put<Comment>(`/posts/${postId}/comments/${commentId}`, data),
  
  delete: (postId: number, commentId: number) =>
    api.delete(`/posts/${postId}/comments/${commentId}`),
}

export default api

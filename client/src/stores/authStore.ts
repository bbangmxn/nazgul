import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User } from '@/types'
import { authApi, userApi } from '@/lib/api'

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  
  login: (email: string, password: string) => Promise<void>
  signup: (email: string, password: string, nickname: string) => Promise<void>
  logout: () => void
  updateUser: (data: Partial<User>) => void
  fetchUser: () => Promise<void>
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,

      login: async (email, password) => {
        set({ isLoading: true })
        try {
          const { data } = await authApi.login({ email, password })
          localStorage.setItem('token', data.accessToken)
          set({
            user: data.user,
            token: data.accessToken,
            isAuthenticated: true,
            isLoading: false,
          })
        } catch (error) {
          set({ isLoading: false })
          throw error
        }
      },

      signup: async (email, password, nickname) => {
        set({ isLoading: true })
        try {
          await authApi.signup({ email, password, nickname })
          // Auto login after signup
          await get().login(email, password)
        } catch (error) {
          set({ isLoading: false })
          throw error
        }
      },

      logout: () => {
        localStorage.removeItem('token')
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        })
      },

      updateUser: (data) => {
        set((state) => ({
          user: state.user ? { ...state.user, ...data } : null,
        }))
      },

      fetchUser: async () => {
        try {
          const { data } = await userApi.getMe()
          set({ user: data })
        } catch {
          get().logout()
        }
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

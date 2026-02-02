import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'
import Layout from '@/components/Layout'
import HomePage from '@/pages/HomePage'
import ExplorePage from '@/pages/ExplorePage'
import ProfilePage from '@/pages/ProfilePage'
import LoginPage from '@/pages/LoginPage'
import SignupPage from '@/pages/SignupPage'
import HobbiesPage from '@/pages/HobbiesPage'
import PostDetailPage from '@/pages/PostDetailPage'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  return !isAuthenticated ? <>{children}</> : <Navigate to="/" />
}

export default function App() {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
      <Route path="/signup" element={<PublicRoute><SignupPage /></PublicRoute>} />

      {/* Protected routes */}
      <Route path="/" element={<PrivateRoute><Layout /></PrivateRoute>}>
        <Route index element={<HomePage />} />
        <Route path="explore" element={<ExplorePage />} />
        <Route path="hobbies" element={<HobbiesPage />} />
        <Route path="profile/:userId?" element={<ProfilePage />} />
        <Route path="post/:postId" element={<PostDetailPage />} />
      </Route>
    </Routes>
  )
}

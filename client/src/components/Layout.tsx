import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { Home, Compass, User, LogOut, Sparkles } from 'lucide-react'
import { useAuthStore } from '@/stores/authStore'
import { cn } from '@/lib/utils'

export default function Layout() {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { to: '/', icon: Home, label: '홈' },
    { to: '/explore', icon: Compass, label: '탐색' },
    { to: '/hobbies', icon: Sparkles, label: '취미' },
    { to: '/profile', icon: User, label: '프로필' },
  ]

  return (
    <div className="min-h-screen bg-background dark:bg-background-dark">
      {/* Desktop sidebar */}
      <aside className="fixed left-0 top-0 z-40 hidden h-screen w-20 flex-col border-r border-border dark:border-border-dark lg:flex xl:w-64">
        <div className="flex h-16 items-center justify-center xl:justify-start xl:px-6">
          <span className="text-2xl font-bold">N</span>
          <span className="ml-2 hidden text-xl font-semibold xl:block">Nazgul</span>
        </div>

        <nav className="flex-1 space-y-2 px-3 py-4">
          {navItems.map(({ to, icon: Icon, label }) => (
            <NavLink
              key={to}
              to={to}
              end={to === '/'}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-4 rounded-xl px-4 py-3 transition-colors',
                  isActive
                    ? 'bg-surface dark:bg-surface-dark font-semibold'
                    : 'hover:bg-surface/50 dark:hover:bg-surface-dark/50'
                )
              }
            >
              <Icon className="h-6 w-6" />
              <span className="hidden xl:block">{label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="border-t border-border dark:border-border-dark p-4">
          <button
            onClick={handleLogout}
            className="flex w-full items-center gap-4 rounded-xl px-4 py-3 text-secondary hover:bg-surface/50 dark:hover:bg-surface-dark/50 transition-colors"
          >
            <LogOut className="h-6 w-6" />
            <span className="hidden xl:block">로그아웃</span>
          </button>
        </div>
      </aside>

      {/* Main content */}
      <main className="lg:ml-20 xl:ml-64">
        <div className="mx-auto max-w-2xl">
          <Outlet />
        </div>
      </main>

      {/* Mobile bottom nav */}
      <nav className="fixed bottom-0 left-0 right-0 z-40 border-t border-border dark:border-border-dark bg-background dark:bg-background-dark lg:hidden">
        <div className="flex h-16 items-center justify-around">
          {navItems.map(({ to, icon: Icon, label }) => (
            <NavLink
              key={to}
              to={to}
              end={to === '/'}
              className={({ isActive }) =>
                cn(
                  'flex flex-col items-center gap-1 p-2 transition-colors',
                  isActive ? 'text-primary dark:text-primary-dark' : 'text-secondary'
                )
              }
            >
              <Icon className="h-6 w-6" />
              <span className="text-2xs">{label}</span>
            </NavLink>
          ))}
        </div>
      </nav>
    </div>
  )
}

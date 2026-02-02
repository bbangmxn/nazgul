import { useState, useEffect } from 'react'
import { Search } from 'lucide-react'
import { postApi, userApi } from '@/lib/api'
import type { Post, UserSummary } from '@/types'
import PostCard from '@/components/PostCard'
import Avatar from '@/components/Avatar'
import { PageLoader } from '@/components/LoadingSpinner'
import { Link } from 'react-router-dom'

type Tab = 'popular' | 'recommended' | 'users'

export default function ExplorePage() {
  const [tab, setTab] = useState<Tab>('popular')
  const [posts, setPosts] = useState<Post[]>([])
  const [users, setUsers] = useState<UserSummary[]>([])
  const [searchQuery, setSearchQuery] = useState('')
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    setIsLoading(true)
    
    if (tab === 'popular') {
      postApi.getPopular().then(({ data }) => {
        setPosts(data.content)
        setIsLoading(false)
      })
    } else if (tab === 'recommended') {
      postApi.getRecommended().then(({ data }) => {
        setPosts(data.content)
        setIsLoading(false)
      })
    } else {
      userApi.getRecommended().then(({ data }) => {
        setUsers(data)
        setIsLoading(false)
      })
    }
  }, [tab])

  const handleSearch = async () => {
    if (!searchQuery.trim()) return
    setIsLoading(true)
    
    try {
      const { data } = await userApi.search(searchQuery)
      setUsers(data)
      setTab('users')
    } finally {
      setIsLoading(false)
    }
  }

  const handleUpdatePost = (updatedPost: Post) => {
    setPosts((prev) =>
      prev.map((p) => (p.id === updatedPost.id ? updatedPost : p))
    )
  }

  return (
    <div className="pb-20 lg:pb-0">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-background/80 dark:bg-background-dark/80 backdrop-blur-lg border-b border-border dark:border-border-dark">
        <div className="px-4 h-14 flex items-center">
          <h1 className="text-xl font-bold">탐색</h1>
        </div>

        {/* Search */}
        <div className="px-4 pb-3">
          <div className="relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-secondary" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              placeholder="사용자 검색..."
              className="input pl-12"
            />
          </div>
        </div>

        {/* Tabs */}
        <div className="flex px-4 gap-1">
          {[
            { key: 'popular', label: '인기' },
            { key: 'recommended', label: '추천' },
            { key: 'users', label: '사용자' },
          ].map(({ key, label }) => (
            <button
              key={key}
              onClick={() => setTab(key as Tab)}
              className={`flex-1 py-3 text-sm font-medium border-b-2 transition-colors ${
                tab === key
                  ? 'border-primary dark:border-primary-dark'
                  : 'border-transparent text-secondary hover:text-primary dark:hover:text-primary-dark'
              }`}
            >
              {label}
            </button>
          ))}
        </div>
      </header>

      {/* Content */}
      {isLoading ? (
        <PageLoader />
      ) : tab === 'users' ? (
        <div className="divide-y divide-border dark:divide-border-dark">
          {users.length === 0 ? (
            <div className="py-20 text-center text-secondary">
              사용자가 없습니다
            </div>
          ) : (
            users.map((user) => (
              <Link
                key={user.id}
                to={`/profile/${user.id}`}
                className="flex items-center gap-3 px-4 py-4 hover:bg-surface/50 dark:hover:bg-surface-dark/50 transition-colors"
              >
                <Avatar src={user.profileImage} name={user.nickname} size="lg" />
                <div>
                  <p className="font-semibold">{user.nickname}</p>
                </div>
              </Link>
            ))
          )}
        </div>
      ) : posts.length === 0 ? (
        <div className="py-20 text-center text-secondary">
          게시물이 없습니다
        </div>
      ) : (
        posts.map((post) => (
          <PostCard
            key={post.id}
            post={post}
            onUpdate={handleUpdatePost}
          />
        ))
      )}
    </div>
  )
}

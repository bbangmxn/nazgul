import { useState, useEffect, useCallback } from 'react'
import { postApi } from '@/lib/api'
import type { Post } from '@/types'
import PostCard from '@/components/PostCard'
import PostComposer from '@/components/PostComposer'
import { PageLoader } from '@/components/LoadingSpinner'

export default function HomePage() {
  const [posts, setPosts] = useState<Post[]>([])
  const [page, setPage] = useState(0)
  const [hasMore, setHasMore] = useState(true)
  const [isLoading, setIsLoading] = useState(true)

  const fetchPosts = useCallback(async (pageNum: number, append = false) => {
    try {
      const { data } = await postApi.getFeed(pageNum)
      setPosts((prev) => (append ? [...prev, ...data.content] : data.content))
      setHasMore(!data.last)
    } catch (error) {
      console.error('Failed to fetch posts:', error)
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchPosts(0)
  }, [fetchPosts])

  const handleNewPost = (post: Post) => {
    setPosts((prev) => [post, ...prev])
  }

  const handleUpdatePost = (updatedPost: Post) => {
    setPosts((prev) =>
      prev.map((p) => (p.id === updatedPost.id ? updatedPost : p))
    )
  }

  const loadMore = () => {
    if (!hasMore) return
    const nextPage = page + 1
    setPage(nextPage)
    fetchPosts(nextPage, true)
  }

  return (
    <div className="pb-20 lg:pb-0">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-background/80 dark:bg-background-dark/80 backdrop-blur-lg border-b border-border dark:border-border-dark">
        <div className="px-4 h-14 flex items-center">
          <h1 className="text-xl font-bold">홈</h1>
        </div>
      </header>

      {/* Composer */}
      <PostComposer onPost={handleNewPost} />

      {/* Feed */}
      {isLoading ? (
        <PageLoader />
      ) : posts.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-20 text-center">
          <p className="text-4xl mb-4">🌱</p>
          <h3 className="text-lg font-semibold mb-2">아직 게시물이 없어요</h3>
          <p className="text-secondary text-sm">
            취미를 추가하고 첫 게시물을 작성해보세요!
          </p>
        </div>
      ) : (
        <>
          {posts.map((post) => (
            <PostCard
              key={post.id}
              post={post}
              onUpdate={handleUpdatePost}
            />
          ))}
          
          {hasMore && (
            <div className="flex justify-center py-6">
              <button onClick={loadMore} className="btn-secondary">
                더 보기
              </button>
            </div>
          )}
        </>
      )}
    </div>
  )
}

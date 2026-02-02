import { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { ArrowLeft, Heart, Send } from 'lucide-react'
import { postApi, commentApi } from '@/lib/api'
import { useAuthStore } from '@/stores/authStore'
import type { Post, Comment } from '@/types'
import Avatar from '@/components/Avatar'
import { PageLoader } from '@/components/LoadingSpinner'
import { cn, timeAgo, formatNumber } from '@/lib/utils'

export default function PostDetailPage() {
  const { postId } = useParams()
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const [post, setPost] = useState<Post | null>(null)
  const [comments, setComments] = useState<Comment[]>([])
  const [newComment, setNewComment] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isLiking, setIsLiking] = useState(false)
  const [isCommenting, setIsCommenting] = useState(false)

  useEffect(() => {
    if (!postId) return

    setIsLoading(true)
    Promise.all([
      postApi.get(parseInt(postId)),
      commentApi.getByPost(parseInt(postId)),
    ]).then(([postRes, commentsRes]) => {
      setPost(postRes.data)
      setComments(commentsRes.data.content)
      setIsLoading(false)
    })
  }, [postId])

  const handleLike = async () => {
    if (!post || isLiking) return
    setIsLiking(true)

    try {
      if (post.liked) {
        await postApi.unlike(post.id)
        setPost({ ...post, liked: false, likeCount: post.likeCount - 1 })
      } else {
        await postApi.like(post.id)
        setPost({ ...post, liked: true, likeCount: post.likeCount + 1 })
      }
    } finally {
      setIsLiking(false)
    }
  }

  const handleComment = async () => {
    if (!post || !newComment.trim() || isCommenting) return
    setIsCommenting(true)

    try {
      const { data } = await commentApi.create(post.id, { content: newComment.trim() })
      setComments([...comments, data])
      setNewComment('')
      setPost({ ...post, commentCount: post.commentCount + 1 })
    } finally {
      setIsCommenting(false)
    }
  }

  if (isLoading || !post) return <PageLoader />

  return (
    <div className="pb-20 lg:pb-0">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-background/80 dark:bg-background-dark/80 backdrop-blur-lg border-b border-border dark:border-border-dark">
        <div className="px-4 h-14 flex items-center gap-4">
          <button onClick={() => navigate(-1)} className="btn-ghost">
            <ArrowLeft className="w-5 h-5" />
          </button>
          <h1 className="text-xl font-bold">게시물</h1>
        </div>
      </header>

      {/* Post */}
      <article className="p-4 border-b border-border dark:border-border-dark">
        {/* Author */}
        <div className="flex items-center gap-3">
          <Link to={`/profile/${post.author.id}`}>
            <Avatar src={post.author.profileImage} name={post.author.nickname} size="lg" />
          </Link>
          <div>
            <Link to={`/profile/${post.author.id}`} className="font-semibold hover:underline">
              {post.author.nickname}
            </Link>
            <p className="text-sm text-secondary">{timeAgo(post.createdAt)}</p>
          </div>
        </div>

        {/* Content */}
        <p className="mt-4 text-lg whitespace-pre-wrap">{post.content}</p>

        {/* Image */}
        {post.imageUrl && (
          <div className="mt-4 rounded-xl overflow-hidden border border-border dark:border-border-dark">
            <img src={post.imageUrl} alt="" className="w-full" />
          </div>
        )}

        {/* Hobby tag */}
        {post.hobby && (
          <div className="mt-4">
            <span className="chip">
              <span>{post.hobby.icon}</span>
              <span>{post.hobby.name}</span>
            </span>
          </div>
        )}

        {/* Stats */}
        <div className="flex items-center gap-6 mt-4 pt-4 border-t border-border dark:border-border-dark">
          <button
            onClick={handleLike}
            disabled={isLiking}
            className={cn(
              'flex items-center gap-2',
              post.liked ? 'text-red-500' : 'text-secondary hover:text-red-500'
            )}
          >
            <Heart className={cn('w-6 h-6', post.liked && 'fill-current')} />
            <span className="font-semibold">{formatNumber(post.likeCount)}</span>
          </button>
        </div>
      </article>

      {/* Comments */}
      <section>
        <h3 className="px-4 py-3 text-sm font-semibold text-secondary">
          댓글 {comments.length}개
        </h3>

        {comments.map((comment) => (
          <div key={comment.id} className="px-4 py-3 flex gap-3 border-b border-border dark:border-border-dark">
            <Link to={`/profile/${comment.author.id}`}>
              <Avatar src={comment.author.profileImage} name={comment.author.nickname} size="sm" />
            </Link>
            <div className="flex-1">
              <div className="flex items-center gap-2">
                <Link to={`/profile/${comment.author.id}`} className="font-semibold text-sm hover:underline">
                  {comment.author.nickname}
                </Link>
                <span className="text-xs text-secondary">{timeAgo(comment.createdAt)}</span>
              </div>
              <p className="mt-1 text-sm">{comment.content}</p>
            </div>
          </div>
        ))}
      </section>

      {/* Comment input */}
      <div className="fixed bottom-16 lg:bottom-0 left-0 right-0 lg:left-20 xl:left-64 bg-background dark:bg-background-dark border-t border-border dark:border-border-dark p-4">
        <div className="max-w-2xl mx-auto flex items-center gap-3">
          {user && <Avatar src={user.profileImage} name={user.nickname} size="sm" />}
          <input
            type="text"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleComment()}
            placeholder="댓글 달기..."
            className="input flex-1"
          />
          <button
            onClick={handleComment}
            disabled={!newComment.trim() || isCommenting}
            className="btn-ghost text-accent disabled:text-secondary"
          >
            <Send className="w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  )
}

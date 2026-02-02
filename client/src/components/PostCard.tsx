import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Heart, MessageCircle, MoreHorizontal } from 'lucide-react'
import type { Post } from '@/types'
import { postApi } from '@/lib/api'
import { cn, timeAgo, formatNumber } from '@/lib/utils'
import Avatar from './Avatar'

interface PostCardProps {
  post: Post
  onUpdate?: (post: Post) => void
  onDelete?: (postId: number) => void
}

export default function PostCard({ post, onUpdate }: PostCardProps) {
  const [isLiking, setIsLiking] = useState(false)

  const handleLike = async (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    
    if (isLiking) return
    setIsLiking(true)

    try {
      if (post.liked) {
        await postApi.unlike(post.id)
        onUpdate?.({
          ...post,
          liked: false,
          likeCount: post.likeCount - 1,
        })
      } else {
        await postApi.like(post.id)
        onUpdate?.({
          ...post,
          liked: true,
          likeCount: post.likeCount + 1,
        })
      }
    } catch (error) {
      console.error('Failed to toggle like:', error)
    } finally {
      setIsLiking(false)
    }
  }

  return (
    <Link to={`/post/${post.id}`} className="card block">
      <article className="px-4 py-4">
        {/* Header */}
        <div className="flex items-start gap-3">
          <Link 
            to={`/profile/${post.author.id}`}
            onClick={(e) => e.stopPropagation()}
          >
            <Avatar 
              src={post.author.profileImage} 
              name={post.author.nickname} 
              size="md"
            />
          </Link>

          <div className="flex-1 min-w-0">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Link
                  to={`/profile/${post.author.id}`}
                  onClick={(e) => e.stopPropagation()}
                  className="font-semibold hover:underline"
                >
                  {post.author.nickname}
                </Link>
                <span className="text-secondary text-sm">
                  {timeAgo(post.createdAt)}
                </span>
              </div>
              <button 
                className="btn-ghost text-secondary"
                onClick={(e) => e.stopPropagation()}
              >
                <MoreHorizontal className="w-5 h-5" />
              </button>
            </div>

            {/* Content */}
            <p className="mt-2 whitespace-pre-wrap break-words">
              {post.content}
            </p>

            {/* Image */}
            {post.imageUrl && (
              <div className="mt-3 rounded-xl overflow-hidden border border-border dark:border-border-dark">
                <img
                  src={post.imageUrl}
                  alt=""
                  className="w-full object-cover max-h-96"
                />
              </div>
            )}

            {/* Hobby tag */}
            {post.hobby && (
              <div className="mt-3">
                <span className="chip text-sm">
                  <span>{post.hobby.icon}</span>
                  <span>{post.hobby.name}</span>
                </span>
              </div>
            )}

            {/* Actions */}
            <div className="mt-3 flex items-center gap-6">
              <button
                onClick={handleLike}
                disabled={isLiking}
                className={cn(
                  'flex items-center gap-2 transition-colors',
                  post.liked ? 'text-red-500' : 'text-secondary hover:text-red-500'
                )}
              >
                <Heart
                  className={cn('w-5 h-5', post.liked && 'fill-current')}
                />
                <span className="text-sm">{formatNumber(post.likeCount)}</span>
              </button>

              <div className="flex items-center gap-2 text-secondary">
                <MessageCircle className="w-5 h-5" />
                <span className="text-sm">{formatNumber(post.commentCount)}</span>
              </div>
            </div>
          </div>
        </div>
      </article>
    </Link>
  )
}

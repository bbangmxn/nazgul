import { useState } from 'react'
import { Image, X } from 'lucide-react'
import { useAuthStore } from '@/stores/authStore'
import { postApi, hobbyApi } from '@/lib/api'
import type { Post, UserHobby } from '@/types'
import Avatar from './Avatar'
import { useEffect } from 'react'

interface PostComposerProps {
  onPost?: (post: Post) => void
}

export default function PostComposer({ onPost }: PostComposerProps) {
  const { user } = useAuthStore()
  const [content, setContent] = useState('')
  const [selectedHobby, setSelectedHobby] = useState<number | null>(null)
  const [hobbies, setHobbies] = useState<UserHobby[]>([])
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [showHobbies, setShowHobbies] = useState(false)

  useEffect(() => {
    hobbyApi.getMyHobbies().then(({ data }) => setHobbies(data))
  }, [])

  const handleSubmit = async () => {
    if (!content.trim() || isSubmitting) return

    setIsSubmitting(true)
    try {
      const { data } = await postApi.create({
        content: content.trim(),
        hobbyId: selectedHobby || undefined,
      })
      setContent('')
      setSelectedHobby(null)
      onPost?.(data)
    } catch (error) {
      console.error('Failed to create post:', error)
    } finally {
      setIsSubmitting(false)
    }
  }

  if (!user) return null

  const selectedHobbyData = hobbies.find((h) => h.id === selectedHobby)

  return (
    <div className="border-b border-border dark:border-border-dark p-4">
      <div className="flex gap-3">
        <Avatar src={user.profileImage} name={user.nickname} size="md" />
        
        <div className="flex-1">
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="무슨 일이 있나요?"
            className="w-full resize-none bg-transparent text-lg placeholder:text-secondary focus:outline-none"
            rows={3}
          />

          {/* Selected hobby */}
          {selectedHobbyData && (
            <div className="flex items-center gap-2 mt-2">
              <span className="chip active">
                <span>{selectedHobbyData.icon}</span>
                <span>{selectedHobbyData.name}</span>
                <button
                  onClick={() => setSelectedHobby(null)}
                  className="ml-1 hover:text-white/80"
                >
                  <X className="w-3 h-3" />
                </button>
              </span>
            </div>
          )}

          {/* Hobby selector */}
          {showHobbies && !selectedHobby && (
            <div className="flex flex-wrap gap-2 mt-3 animate-fade-in">
              {hobbies.map((hobby) => (
                <button
                  key={hobby.id}
                  onClick={() => {
                    setSelectedHobby(hobby.id)
                    setShowHobbies(false)
                  }}
                  className="chip"
                >
                  <span>{hobby.icon}</span>
                  <span>{hobby.name}</span>
                </button>
              ))}
            </div>
          )}

          {/* Actions */}
          <div className="flex items-center justify-between mt-4 pt-3 border-t border-border dark:border-border-dark">
            <div className="flex items-center gap-2">
              <button className="btn-ghost text-accent">
                <Image className="w-5 h-5" />
              </button>
              <button
                onClick={() => setShowHobbies(!showHobbies)}
                className={`btn-ghost ${showHobbies ? 'text-accent' : 'text-secondary'}`}
              >
                <span className="text-lg">🏷️</span>
              </button>
            </div>

            <button
              onClick={handleSubmit}
              disabled={!content.trim() || isSubmitting}
              className="btn-primary px-5 py-2"
            >
              {isSubmitting ? '게시 중...' : '게시'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

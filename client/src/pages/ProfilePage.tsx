import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { Settings, UserPlus, UserMinus } from 'lucide-react'
import { userApi, hobbyApi, postApi } from '@/lib/api'
import { useAuthStore } from '@/stores/authStore'
import type { User, UserHobby, Post } from '@/types'
import Avatar from '@/components/Avatar'
import PostCard from '@/components/PostCard'
import { PageLoader } from '@/components/LoadingSpinner'
import { formatNumber, skillLevelLabels } from '@/lib/utils'

export default function ProfilePage() {
  const { userId } = useParams()
  const { user: currentUser, updateUser } = useAuthStore()
  const [user, setUser] = useState<User | null>(null)
  const [hobbies, setHobbies] = useState<UserHobby[]>([])
  const [posts, setPosts] = useState<Post[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isFollowing, setIsFollowing] = useState(false)
  const [isEditing, setIsEditing] = useState(false)
  const [editForm, setEditForm] = useState({ nickname: '', bio: '' })

  const targetId = userId ? parseInt(userId) : currentUser?.id
  const isOwnProfile = targetId === currentUser?.id

  useEffect(() => {
    if (!targetId) return

    setIsLoading(true)
    Promise.all([
      userApi.getUser(targetId),
      hobbyApi.getUserHobbies(targetId),
    ]).then(([userRes, hobbiesRes]) => {
      setUser(userRes.data)
      setHobbies(hobbiesRes.data)
      setEditForm({ nickname: userRes.data.nickname, bio: userRes.data.bio || '' })
      setIsLoading(false)
    })

    // Check if following (for other users)
    if (!isOwnProfile && currentUser) {
      userApi.getFollowing(currentUser.id).then(({ data }) => {
        setIsFollowing(data.some((u) => u.id === targetId))
      })
    }
  }, [targetId, isOwnProfile, currentUser])

  const handleFollow = async () => {
    if (!targetId) return

    try {
      if (isFollowing) {
        await userApi.unfollow(targetId)
        setIsFollowing(false)
        setUser((prev) =>
          prev ? { ...prev, followersCount: prev.followersCount - 1 } : null
        )
      } else {
        await userApi.follow(targetId)
        setIsFollowing(true)
        setUser((prev) =>
          prev ? { ...prev, followersCount: prev.followersCount + 1 } : null
        )
      }
    } catch (error) {
      console.error('Failed to toggle follow:', error)
    }
  }

  const handleSaveProfile = async () => {
    try {
      const { data } = await userApi.updateMe(editForm)
      setUser(data)
      updateUser(data)
      setIsEditing(false)
    } catch (error) {
      console.error('Failed to update profile:', error)
    }
  }

  if (isLoading || !user) return <PageLoader />

  return (
    <div className="pb-20 lg:pb-0">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-background/80 dark:bg-background-dark/80 backdrop-blur-lg border-b border-border dark:border-border-dark">
        <div className="px-4 h-14 flex items-center justify-between">
          <h1 className="text-xl font-bold">{user.nickname}</h1>
          {isOwnProfile && (
            <button
              onClick={() => setIsEditing(!isEditing)}
              className="btn-ghost"
            >
              <Settings className="w-5 h-5" />
            </button>
          )}
        </div>
      </header>

      {/* Profile header */}
      <section className="p-4 border-b border-border dark:border-border-dark">
        <div className="flex items-start gap-4">
          <Avatar src={user.profileImage} name={user.nickname} size="xl" />
          
          <div className="flex-1">
            {isEditing ? (
              <div className="space-y-3">
                <input
                  type="text"
                  value={editForm.nickname}
                  onChange={(e) => setEditForm({ ...editForm, nickname: e.target.value })}
                  className="input"
                  placeholder="닉네임"
                />
                <textarea
                  value={editForm.bio}
                  onChange={(e) => setEditForm({ ...editForm, bio: e.target.value })}
                  className="input resize-none"
                  placeholder="자기소개"
                  rows={2}
                />
                <div className="flex gap-2">
                  <button onClick={handleSaveProfile} className="btn-primary">
                    저장
                  </button>
                  <button onClick={() => setIsEditing(false)} className="btn-secondary">
                    취소
                  </button>
                </div>
              </div>
            ) : (
              <>
                <h2 className="text-lg font-bold">{user.nickname}</h2>
                {user.bio && (
                  <p className="mt-1 text-secondary text-sm">{user.bio}</p>
                )}
                
                {/* Stats */}
                <div className="flex gap-6 mt-3 text-sm">
                  <div>
                    <span className="font-semibold">{formatNumber(user.followersCount)}</span>
                    <span className="text-secondary ml-1">팔로워</span>
                  </div>
                  <div>
                    <span className="font-semibold">{formatNumber(user.followingCount)}</span>
                    <span className="text-secondary ml-1">팔로잉</span>
                  </div>
                </div>

                {/* Follow button */}
                {!isOwnProfile && (
                  <button
                    onClick={handleFollow}
                    className={`mt-4 ${isFollowing ? 'btn-secondary' : 'btn-primary'}`}
                  >
                    {isFollowing ? (
                      <>
                        <UserMinus className="w-4 h-4 mr-2" />
                        팔로잉
                      </>
                    ) : (
                      <>
                        <UserPlus className="w-4 h-4 mr-2" />
                        팔로우
                      </>
                    )}
                  </button>
                )}
              </>
            )}
          </div>
        </div>
      </section>

      {/* Hobbies */}
      {hobbies.length > 0 && (
        <section className="p-4 border-b border-border dark:border-border-dark">
          <h3 className="text-sm font-semibold text-secondary mb-3">취미</h3>
          <div className="flex flex-wrap gap-2">
            {hobbies.map((hobby) => (
              <div key={hobby.id} className="chip">
                <span>{hobby.icon}</span>
                <span>{hobby.name}</span>
                <span className="text-xs text-secondary">
                  {skillLevelLabels[hobby.skillLevel]}
                </span>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Posts placeholder */}
      <section className="p-4">
        <h3 className="text-sm font-semibold text-secondary mb-3">게시물</h3>
        <div className="py-10 text-center text-secondary">
          게시물이 없습니다
        </div>
      </section>
    </div>
  )
}

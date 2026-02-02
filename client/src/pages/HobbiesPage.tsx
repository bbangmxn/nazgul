import { useState, useEffect } from 'react'
import { Check, Plus } from 'lucide-react'
import { hobbyApi } from '@/lib/api'
import type { Hobby, UserHobby } from '@/types'
import { HOBBY_CATEGORIES } from '@/types'
import { PageLoader } from '@/components/LoadingSpinner'
import { cn, skillLevelLabels } from '@/lib/utils'

export default function HobbiesPage() {
  const [allHobbies, setAllHobbies] = useState<Hobby[]>([])
  const [myHobbies, setMyHobbies] = useState<UserHobby[]>([])
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    Promise.all([hobbyApi.getAll(), hobbyApi.getMyHobbies()]).then(
      ([allRes, myRes]) => {
        setAllHobbies(allRes.data)
        setMyHobbies(myRes.data)
        setIsLoading(false)
      }
    )
  }, [])

  const myHobbyIds = new Set(myHobbies.map((h) => h.id))

  const filteredHobbies = selectedCategory
    ? allHobbies.filter((h) => h.category === selectedCategory)
    : allHobbies

  const toggleHobby = async (hobby: Hobby) => {
    if (myHobbyIds.has(hobby.id)) {
      // Remove
      try {
        await hobbyApi.removeMyHobby(hobby.id)
        setMyHobbies((prev) => prev.filter((h) => h.id !== hobby.id))
      } catch (error) {
        console.error('Failed to remove hobby:', error)
      }
    } else {
      // Add
      try {
        const { data } = await hobbyApi.addMyHobby({ hobbyId: hobby.id, skillLevel: 1 })
        setMyHobbies((prev) => [...prev, data])
      } catch (error) {
        console.error('Failed to add hobby:', error)
      }
    }
  }

  const updateSkillLevel = async (hobbyId: number, level: number) => {
    try {
      await hobbyApi.updateSkillLevel(hobbyId, level)
      setMyHobbies((prev) =>
        prev.map((h) => (h.id === hobbyId ? { ...h, skillLevel: level } : h))
      )
    } catch (error) {
      console.error('Failed to update skill level:', error)
    }
  }

  if (isLoading) return <PageLoader />

  return (
    <div className="pb-20 lg:pb-0">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-background/80 dark:bg-background-dark/80 backdrop-blur-lg border-b border-border dark:border-border-dark">
        <div className="px-4 h-14 flex items-center">
          <h1 className="text-xl font-bold">내 취미</h1>
        </div>
      </header>

      {/* My Hobbies */}
      {myHobbies.length > 0 && (
        <section className="p-4 border-b border-border dark:border-border-dark">
          <h2 className="text-sm font-semibold text-secondary mb-3">
            선택한 취미 ({myHobbies.length})
          </h2>
          <div className="space-y-3">
            {myHobbies.map((hobby) => (
              <div
                key={hobby.id}
                className="flex items-center justify-between p-3 rounded-xl bg-surface dark:bg-surface-dark"
              >
                <div className="flex items-center gap-3">
                  <span className="text-2xl">{hobby.icon}</span>
                  <div>
                    <p className="font-medium">{hobby.name}</p>
                    <p className="text-xs text-secondary">
                      {skillLevelLabels[hobby.skillLevel]}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  {/* Skill level selector */}
                  <div className="flex gap-1">
                    {[1, 2, 3, 4, 5].map((level) => (
                      <button
                        key={level}
                        onClick={() => updateSkillLevel(hobby.id, level)}
                        className={cn(
                          'w-2 h-2 rounded-full transition-colors',
                          level <= hobby.skillLevel
                            ? 'bg-accent'
                            : 'bg-border dark:bg-border-dark'
                        )}
                      />
                    ))}
                  </div>
                  <button
                    onClick={() => toggleHobby(hobby)}
                    className="btn-ghost text-red-500"
                  >
                    ×
                  </button>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Category filter */}
      <section className="p-4 border-b border-border dark:border-border-dark overflow-x-auto">
        <div className="flex gap-2 min-w-max">
          <button
            onClick={() => setSelectedCategory(null)}
            className={cn('chip', !selectedCategory && 'active')}
          >
            전체
          </button>
          {HOBBY_CATEGORIES.map(({ key, label, emoji }) => (
            <button
              key={key}
              onClick={() => setSelectedCategory(key)}
              className={cn('chip', selectedCategory === key && 'active')}
            >
              <span>{emoji}</span>
              <span>{label}</span>
            </button>
          ))}
        </div>
      </section>

      {/* All Hobbies */}
      <section className="p-4">
        <h2 className="text-sm font-semibold text-secondary mb-3">
          {selectedCategory
            ? HOBBY_CATEGORIES.find((c) => c.key === selectedCategory)?.label
            : '모든 취미'}
        </h2>
        <div className="grid grid-cols-2 gap-3">
          {filteredHobbies.map((hobby) => {
            const isSelected = myHobbyIds.has(hobby.id)
            return (
              <button
                key={hobby.id}
                onClick={() => toggleHobby(hobby)}
                className={cn(
                  'flex items-center gap-3 p-4 rounded-xl border transition-all',
                  isSelected
                    ? 'border-accent bg-accent/10'
                    : 'border-border dark:border-border-dark hover:border-secondary'
                )}
              >
                <span className="text-2xl">{hobby.icon}</span>
                <span className="font-medium flex-1 text-left">{hobby.name}</span>
                {isSelected ? (
                  <Check className="w-5 h-5 text-accent" />
                ) : (
                  <Plus className="w-5 h-5 text-secondary" />
                )}
              </button>
            )
          })}
        </div>
      </section>
    </div>
  )
}

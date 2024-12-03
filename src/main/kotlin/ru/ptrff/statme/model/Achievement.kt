package ru.ptrff.statme.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "achievement")
class Achievement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var title: String,

    @ManyToOne
    @JoinColumn(
        name = "game_id",
        nullable = false
    )
    var game: Game,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_achievements",
        joinColumns = [JoinColumn(name = "achievement_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableList<User> = mutableListOf()
)

interface AchievementRepository : JpaRepository<Achievement, Long> {
}

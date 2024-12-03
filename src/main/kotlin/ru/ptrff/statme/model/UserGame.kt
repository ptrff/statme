package ru.ptrff.statme.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "user_games")
data class UserGame(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    var progress: Float = 0f
)

interface UserGameRepository : JpaRepository<UserGame, Long>{
    fun findByUserAndGame(user: User, game: Game): UserGame?
    fun findByUser(user: User): List<UserGame>
}

package ru.ptrff.statme.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "game")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var title: String,
    var photo: String? = null,
    var description: String? = null,

    @OneToMany(
        mappedBy = "game",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var achievements: MutableList<Achievement> = mutableListOf(),

    @OneToMany(
        mappedBy = "game",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var userGames: MutableList<UserGame> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "company_id")
    val company: User,
)

interface GameRepository : JpaRepository<Game, Long> {
    fun findByTitleStartsWithIgnoreCaseOrderByTitle(
        title: String
    ): List<Game>

    fun findGamesByCompanyId(companyId: Long): List<Game>

}

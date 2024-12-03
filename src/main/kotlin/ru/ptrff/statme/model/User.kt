package ru.ptrff.statme.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "profile")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var email: String,
    private var username: String,
    private val password: String,
    @Enumerated(EnumType.STRING) val role: Role,
    var photo: String? = null,
    var status: String? = null,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var userGames: MutableList<UserGame> = mutableListOf(),

    @ManyToMany(
        fetch = FetchType.EAGER,
        mappedBy = "users",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
    )
    var achievements: MutableList<Achievement> = mutableListOf(),

    @OneToMany(
        mappedBy = "company",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var companyGames: MutableList<Game> = mutableListOf()

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword() = password
    override fun getUsername() = username
    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = role != Role.NOT_APPROVED_COMPANY
}

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUsernameStartsWithIgnoreCaseAndRoleOrderByUsername(
        username: String,
        role: Role = Role.USER
    ): List<User>

    fun findAllByRoleOrderByAchievements(role: Role = Role.USER): List<User>
}

enum class Role {
    USER,
    ADMIN,
    NOT_APPROVED_COMPANY,
    COMPANY
}
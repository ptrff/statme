package ru.ptrff.statme.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.ptrff.statme.dto.GameResponse
import ru.ptrff.statme.dto.ProfileResponse
import ru.ptrff.statme.dto.ProfileSearchResponse
import ru.ptrff.statme.model.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max
import kotlin.math.min

@Service
class ProfileService(
    val repository: UserRepository,
    val userGameRepository: UserGameRepository,
    val gameRepository: GameRepository
) {

    fun getProfile(email: String?): User {
        if (email == null) {
            return getCurrentUser()
        }

        repository.findByEmail(email)?.let {
            return it
        }

        throw Exception("Profile not found")
    }

    fun getProfileById(id: Long): User {
        repository.findById(id).getOrNull()?.let {
            return it
        }

        throw Exception("Profile not found")
    }

    fun getGames(email: String?): List<GameResponse> {
        val user = email?.let {
            repository.findByEmail(it)
        } ?: getCurrentUser()

        val games = if (user.role == Role.COMPANY) {
            gameRepository.findGamesByCompanyId(user.id!!)
        } else {
            val userGames = userGameRepository.findByUser(user)
            userGames.map { it.game }
        }

        return List(games.size) {
            GameResponse(
                id = games[it].id ?: 0,
                title = games[it].title,
                description = games[it].description,
                photo = games[it].photo,
                companyId = games[it].company.id ?: 0
            )
        }

    }

    fun getAchievements(email: String?): List<Achievement> {
        val user = email?.let {
            repository.findByEmail(it)
        } ?: getCurrentUser()

        return user.achievements
    }

    fun findByUsername(username: String): List<ProfileSearchResponse> {
        val users =
            repository.findByUsernameStartsWithIgnoreCaseAndRoleOrderByUsername(
                username
            )
        return List(min(5, users.size)) {
            ProfileSearchResponse(users[it].username, users[it].email)
        }
    }

    fun getTop(): List<ProfileResponse> {
        val top = repository.findAllByRoleOrderByAchievements()
        return List(max(0, min(10, top.size))) {
            ProfileResponse(
                id = top[it].id ?: 0,
                email = top[it].email,
                username = top[it].username,
                photo = top[it].photo,
                status = top[it].status,
                role = top[it].role.toString()
            )
        }
    }
}

fun getCurrentUser(): User {
    val e = SecurityContextHolder.getContext().authentication.principal
    if (e is User) {
        return e
    }
    throw Exception("Not logged in")
}
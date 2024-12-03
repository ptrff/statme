package ru.ptrff.statme.service

import org.springframework.stereotype.Service
import ru.ptrff.statme.dto.ProfileEditRequest
import ru.ptrff.statme.model.*
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileEditService(
    val repository: UserRepository,
    val gamesRepository: GameRepository,
    val userGameRepository: UserGameRepository,
    val achievementRepository: AchievementRepository
) {

    fun changeUsername(newUsername: String) {
        val user = getCurrentUser()
        user.username = newUsername
        repository.save(user)
    }

    fun changeStatus(newStatus: String) {
        val user = getCurrentUser()
        user.status = newStatus
        repository.save(user)
    }

    fun changePhoto(newPhoto: String) {
        val user = getCurrentUser()
        user.photo = newPhoto
        repository.save(user)
    }

    fun changeAll(profileEditRequest: ProfileEditRequest) {
        val user = getCurrentUser()
        user.username = profileEditRequest.username
        user.status = profileEditRequest.status
        user.photo = profileEditRequest.photo
        repository.save(user)
    }

    fun addGame(gameId: Long) {
        val user = getCurrentUser()
        if (user.role != Role.USER) {
            throw IllegalStateException("Only user can add games")
        }

        val game = gamesRepository.findById(gameId).getOrNull()
            ?: throw IllegalStateException("Game not found")

        userGameRepository.findByUserAndGame(user, game)?.let {
            throw Exception("Game already added")
        }


        val userGame = UserGame(user = user, game = game)
        userGameRepository.save(userGame)

        user.userGames.add(userGame)
        game.userGames.add(userGame)
        repository.save(user)
        gamesRepository.save(game)
    }

    fun removeGame(gameId: Long) {
        val user = getCurrentUser()
        val game = gamesRepository.findById(gameId).orElseThrow {
            IllegalArgumentException("Game with ID $gameId not found")
        }

        val userGame = userGameRepository.findByUserAndGame(user, game)
            ?: throw Exception("Game not added")

        userGameRepository.delete(userGame)
    }

    fun addAchievement(userId: Long, achievementId: Long) {
        val user = repository.findById(userId).getOrNull()
            ?: throw IllegalStateException("User not found")

        if (user.role != Role.USER) {
            throw IllegalStateException("Only users can have achievements")
        }

        val achievement =
            achievementRepository.findById(achievementId).getOrNull()
                ?: throw IllegalStateException("Achievement not found")

        userGameRepository.findByUserAndGame(user, achievement.game)?.let {
            user.achievements.add(achievement)
            achievement.users.add(user)
            repository.save(user)
            achievementRepository.save(achievement)
            return
        }

        throw Exception("Game not added")
    }

    fun removeAchievement(userId: Long, achievementId: Long) {
        val user = repository.findById(userId).getOrNull()
            ?: throw IllegalStateException("User not found")

        if (user.role != Role.USER) {
            throw IllegalStateException("Only users can have achievements")
        }

        val achievement =
            achievementRepository.findById(achievementId).getOrNull()
                ?: throw IllegalStateException("Achievement not found")

        user.achievements.remove(achievement)
        repository.save(user)
    }
}
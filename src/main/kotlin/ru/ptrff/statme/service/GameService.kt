package ru.ptrff.statme.service

import org.springframework.stereotype.Service
import ru.ptrff.statme.dto.*
import ru.ptrff.statme.model.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

@Service
class GameService(
    val repository: GameRepository,
    val userGameRepository: UserGameRepository,
    val achievementRepository: AchievementRepository
) {

    fun getGame(gameId: Long): GameResponse {
        val game = repository.findById(gameId).getOrNull()
            ?: throw IllegalStateException("Game not found")
        return GameResponse(
            id = game.id ?: 0,
            title = game.title,
            description = game.description,
            photo = game.photo,
            companyId = game.company.id ?: 0
        )
    }

    fun findByTitle(title: String): List<GameResponse> {
        val games =
            repository.findByTitleStartsWithIgnoreCaseOrderByTitle(
                title
            )
        return List(min(5, games.size)) {
            GameResponse(
                games[it].id ?: 0,
                games[it].title,
                games[it].description,
                games[it].company.id ?: 0,
                games[it].photo
            )
        }
    }

    fun createGame(createGameRequest: CreateGameRequest) {
        val user = getCurrentUser()
        if (user.role != Role.COMPANY) {
            throw IllegalStateException("Only company can create games")
        }

        val game = Game(
            title = createGameRequest.title,
            photo = createGameRequest.photo,
            description = createGameRequest.description,
            company = user
        )
        repository.save(game)

        val userGame = UserGame(user = user, game = game)
        userGameRepository.save(userGame)

        game.userGames.add(userGame)
        repository.save(game)
    }

    fun deleteGame(gameId: Long) {
        val user = getCurrentUser()
        if (user.role != Role.COMPANY) {
            throw IllegalStateException("Only company can delete games")
        }
        val game = repository.findById(gameId).getOrNull()
            ?: throw IllegalStateException("Game not found")

        if (game.company.id != user.id) {
            throw IllegalStateException("That is not your game")
        }
        repository.delete(game)
    }

    fun addAchievement(achievementRequest: CreateAchievementRequest){
        val user = getCurrentUser()
        if (user.role != Role.COMPANY) {
            throw IllegalStateException("Only company can delete games")
        }
        val game = repository.findById(achievementRequest.gameId).getOrNull()
            ?: throw IllegalStateException("Game not found")

        if (game.company.id != user.id) {
            throw IllegalStateException("That is not your game")
        }

        val achievement = Achievement(
            title = achievementRequest.title,
            game = game
        )
        achievementRepository.save(achievement)
    }

    fun deleteAchievement(id: Long){
        val user = getCurrentUser()
        if (user.role != Role.COMPANY) {
            throw IllegalStateException("Only company can delete achievements")
        }

        val achievement = achievementRepository.findById(id).getOrNull()
            ?: throw IllegalStateException("Achievement not found")

        if(achievement.game.company.id != user.id){
            throw IllegalStateException("That is not your game")
        }

        achievementRepository.delete(achievement)
    }

    fun getAchievements(gameId: Long): List<AchievementResponse> {
        val game = repository.findById(gameId).getOrNull()
            ?: throw IllegalStateException("Game not found")

        return List(game.achievements.size){
            AchievementResponse(
                id = game.achievements[it].id ?: 0,
                title = game.achievements[it].title
            )
        }
    }
}

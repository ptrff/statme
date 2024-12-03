package ru.ptrff.statme.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ptrff.statme.dto.CreateAchievementRequest
import ru.ptrff.statme.dto.CreateGameRequest
import ru.ptrff.statme.dto.MessageResponse
import ru.ptrff.statme.service.GameService

@RestController
@RequestMapping("/api/game")
class GameController(
    val gameService: GameService
) {

    @GetMapping("/get")
    fun getGame(
        @RequestParam id: Long
    ): Any {
        return try {
            gameService.getGame(id)
        } catch (e: Exception) {
            MessageResponse(false, "Game getting error ${e.message}")
        }
    }

    @PostMapping("/create")
    fun createGame(
        @RequestBody createGameRequest: CreateGameRequest
    ): Any {
        return try {
            gameService.createGame(createGameRequest)
            MessageResponse(true, "Game created")
        } catch (e: Exception) {
            MessageResponse(false, "Game creating error ${e.message}")
        }
    }

    @DeleteMapping("/delete")
    fun deleteGame(
        @RequestParam id: Long
    ): Any {
        return try {
            gameService.deleteGame(id)
            MessageResponse(true, "Game deleted")
        } catch (e: Exception) {
            MessageResponse(false, "Game deleting error ${e.message}")
        }
    }

    @PostMapping("/achievement/add")
    fun addAchievement(
        @RequestBody achievementRequest: CreateAchievementRequest
    ): Any {
        return try {
            gameService.addAchievement(achievementRequest)
            MessageResponse(true, "Achievement added")
        } catch (e: Exception) {
            MessageResponse(false, "Achievement add error ${e.message}")

        }
    }

    @DeleteMapping("/achievement/delete")
    fun deleteAchievement(
        @RequestParam id: Long
    ): Any {
        return try {
            gameService.deleteAchievement(id)
            MessageResponse(true, "Achievement deleted")
        } catch (e: Exception) {
            MessageResponse(false, "Achievement delete error ${e.message}")
        }
    }

    @GetMapping("/achievement/get")
    fun getAchievement(
        @RequestParam id: Long
    ): Any {
        return try {
            ResponseEntity.ok(gameService.getAchievements(id))
        } catch (e: Exception) {
            MessageResponse(false, "Achievements get error ${e.message}")
        }
    }
}
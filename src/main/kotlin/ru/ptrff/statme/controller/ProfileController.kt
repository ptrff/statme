package ru.ptrff.statme.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.ptrff.statme.dto.MessageResponse
import ru.ptrff.statme.dto.ProfileResponse
import ru.ptrff.statme.model.Game
import ru.ptrff.statme.service.ProfileService

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    val profileService: ProfileService
) {

    @GetMapping("/get")
    fun getProfile(
        @RequestParam email: String?
    ): Any {
        return try {
            val user = profileService.getProfile(email)
            ProfileResponse(
                id = user.id?:0,
                email = user.email,
                username = user.username,
                photo = user.photo,
                status = user.status,
                role = user.role.toString()
            )
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/getById")
    fun getProfileId(
        @RequestParam id: Long
    ): Any {
        return try {
            val user = profileService.getProfileById(id)
            ProfileResponse(
                id = user.id?:0,
                email = user.email,
                username = user.username,
                photo = user.photo,
                status = user.status,
                role = user.role.toString()
            )
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/games")
    fun getGames(
        @RequestParam email: String?
    ): Any {
        return try {
            profileService.getGames(email)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/achievements")
    fun getAchievements(
        @RequestParam email: String?
    ): Any {
        return try {
            profileService.getAchievements(email)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/top")
    fun getTop(): Any {
        return try {
            profileService.getTop()
        } catch (e: Exception) {
            MessageResponse(false, "Top get error ${e.message}")
        }
    }

}
package ru.ptrff.statme.controller


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ptrff.statme.dto.MessageResponse
import ru.ptrff.statme.dto.ProfileEditRequest
import ru.ptrff.statme.service.ProfileEditService

@RestController
@RequestMapping("/api/profile/edit")
class ProfileEditController(
    val profileEditService: ProfileEditService
) {
    @PutMapping("/nickname")
    fun updateNickname(@RequestParam username: String): ResponseEntity<String> {
        return try {
            profileEditService.changeUsername(username)
            ResponseEntity.ok("Nickname updated successfully.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error updating nickname: ${e.message}")
        }
    }

    @PutMapping("/status")
    fun updateStatus(@RequestParam status: String): ResponseEntity<String> {
        return try {
            profileEditService.changeStatus(status)
            ResponseEntity.ok("Status updated successfully.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error updating status: ${e.message}")
        }
    }

    @PutMapping("/photo")
    fun updatePhoto(
        @RequestParam photo: String
    ): MessageResponse {
        return try {
            profileEditService.changePhoto(photo)
            MessageResponse(true, "Photo updated successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error updating photo: ${e.message}")
        }
    }

    @PostMapping
    fun editAll(
        @RequestBody profileEditRequest: ProfileEditRequest
    ): MessageResponse {
        return try {
            profileEditService.changeAll(profileEditRequest)
            MessageResponse(true, "Data updated successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error updating data: ${e.message}")
        }
    }

    @PostMapping("/game/add")
    fun addGame(
        @RequestParam id: Long
    ): MessageResponse {
        return try {
            profileEditService.addGame(id)
            MessageResponse(true, "Game added successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error adding game: ${e.message}")
        }
    }

    @DeleteMapping("/game/delete")
    fun removeGame(
        @RequestParam id: Long
    ): MessageResponse {
        return try {
            profileEditService.removeGame(id)
            MessageResponse(true, "Game removed successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error removing game: ${e.message}")
        }
    }

    @PostMapping("/achievement/add")
    fun addAchievement(
        @RequestParam userId: Long,
        @RequestParam achievementId: Long
    ): MessageResponse {
        return try {
            profileEditService.addAchievement(userId, achievementId)
            MessageResponse(true, "Achievement added successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error adding achievement: ${e.message}")
        }
    }

    @DeleteMapping("/achievement/delete")
    fun removeAchievement(
        @RequestParam userId: Long,
        @RequestParam achievementId: Long
    ): MessageResponse {
        return try {
            profileEditService.removeAchievement(userId, achievementId)
            MessageResponse(true, "Achievement removed successfully.")
        } catch (e: Exception) {
            MessageResponse(false, "Error removing achievement: ${e.message}")
        }
    }
}
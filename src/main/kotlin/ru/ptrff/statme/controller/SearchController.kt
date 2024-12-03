package ru.ptrff.statme.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.ptrff.statme.dto.GameResponse
import ru.ptrff.statme.dto.ProfileSearchResponse
import ru.ptrff.statme.service.GameService
import ru.ptrff.statme.service.ProfileService

@RestController
@RequestMapping("/api/search")
class SearchController(
    val profileService: ProfileService,
    val gameService: GameService
) {

    @GetMapping("/users")
    fun userByUsername(
        @RequestParam username: String
    ): List<ProfileSearchResponse> {
        if (username.isEmpty()) {
            return emptyList()
        }
        return profileService.findByUsername(username)
    }

    @GetMapping("/games")
    fun gameByTitle(
        @RequestParam title: String
    ): List<GameResponse> {
        if (title.isEmpty()) {
            return emptyList()
        }
        return gameService.findByTitle(title)
    }
}
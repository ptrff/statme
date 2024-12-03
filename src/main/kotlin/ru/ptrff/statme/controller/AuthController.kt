package ru.ptrff.statme.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ptrff.statme.dto.AuthRequest
import ru.ptrff.statme.dto.RegisterRequest
import ru.ptrff.statme.model.Role
import ru.ptrff.statme.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val service: AuthService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): Any {
        return ResponseEntity.ok(service.register(request, Role.USER))
    }

    @PostMapping("/login")
    fun authenticate(
        @RequestBody request: AuthRequest
    ): Any {
        return ResponseEntity.ok(service.authenticate(request))
    }

    @PostMapping("/company/register")
    fun registerCompany(
        @RequestBody request: RegisterRequest
    ): Any {
        return ResponseEntity.ok(
            service.register(
                request,
                Role.NOT_APPROVED_COMPANY
            )
        )
    }
}
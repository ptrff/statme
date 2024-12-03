package ru.ptrff.statme.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.ptrff.statme.model.Role
import ru.ptrff.statme.model.User
import ru.ptrff.statme.model.UserRepository
import ru.ptrff.statme.dto.AuthRequest
import ru.ptrff.statme.dto.AuthResponse
import ru.ptrff.statme.dto.RegisterRequest
import ru.ptrff.statme.dto.RegisterResponse

@Service
class AuthService(
    val repository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest, role: Role): Any {
        if (!isValidEmail(request.email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Email is not valid")
        }

        repository.findByEmail(request.email)?.let {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User with this email already exists")
        }

        val user = User(
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = role
        )
        repository.save(user)
        val jwt = jwtService.generateToken(user)
        return RegisterResponse(jwt)
    }

    fun authenticate(request: AuthRequest): Any {
        if (!isValidEmail(request.email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Email is not valid")
        }

        repository.findByEmail(request.email)?.let {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.email,
                    request.password
                )
            )

            val jwt = jwtService.generateToken(it)
            return AuthResponse(jwt)
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("User not found")
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})$".toRegex()
    return email.matches(emailRegex)
}
package ru.ptrff.statme.dto

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)

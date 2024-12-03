package ru.ptrff.statme.dto

data class ProfileResponse(
    val id: Long,
    val email: String,
    val username: String,
    val photo: String?,
    val status: String?,
    val role: String
)

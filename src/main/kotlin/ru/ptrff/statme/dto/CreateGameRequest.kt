package ru.ptrff.statme.dto

data class CreateGameRequest(
    val title: String,
    val description: String?,
    val photo: String?
)

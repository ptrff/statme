package ru.ptrff.statme.dto

data class GameResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val companyId: Long,
    val photo: String?
)

package ru.ptrff.statme.dto

data class ProfileEditRequest(
    val username: String,
    val photo: String?,
    val status: String?
)

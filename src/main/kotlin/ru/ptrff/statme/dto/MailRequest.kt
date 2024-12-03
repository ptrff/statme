package ru.ptrff.statme.dto

data class MailRequest (
    val to: String,
    val subject: String,
    val text: String
)
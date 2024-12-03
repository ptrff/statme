package ru.ptrff.statme.utils

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service
class MyMailSender(
    private val mailSender: JavaMailSenderImpl
) {
    fun send(emailTo: String, subject: String, message: String) {
        val mailMessage = SimpleMailMessage()
        mailMessage.from = mailSender.username
        mailMessage.setTo(emailTo)
        mailMessage.subject = subject
        mailMessage.text = message
        mailSender.send(mailMessage)
    }
}

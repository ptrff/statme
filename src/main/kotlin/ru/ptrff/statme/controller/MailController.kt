package ru.ptrff.statme.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ptrff.statme.utils.MyMailSender
import ru.ptrff.statme.config.MailConfig
import ru.ptrff.statme.dto.MailRequest

@RestController
@RequestMapping("/api/mail")
class MailController(
    val mailConfig: MailConfig
) {

    @RequestMapping("/send")
    fun test(
        @RequestBody request: MailRequest
    ) : Any {
        val myMailSender = MyMailSender(mailConfig.getMailSender())
        myMailSender.send(
            request.to,
            request.subject,
            request.text
        )
        return ResponseEntity.ok("OK")
    }
}
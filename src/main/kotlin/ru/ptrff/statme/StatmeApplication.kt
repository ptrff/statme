package ru.ptrff.statme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StatmeApplication

fun main(args: Array<String>) {
    runApplication<StatmeApplication>(*args)
}

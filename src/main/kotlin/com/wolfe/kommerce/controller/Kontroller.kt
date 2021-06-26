package com.wolfe.kommerce.controller

import com.wolfe.kommerce.event.TriggerReadEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Kontroller(val eventPublisher: ApplicationEventPublisher) {

    @Value("\${lockRead:true}")
    lateinit var lockRead: String

    @GetMapping("/")
    fun emitReadEvent(): Map<String, String> {
        println(lockRead)
        if (lockRead.toBoolean())
            return mapOf(Pair("Message", "Reader Was Locked"))
        eventPublisher.publishEvent(TriggerReadEvent(this))
        return mapOf(Pair("Message", "Reader was evoked"))
    }
}
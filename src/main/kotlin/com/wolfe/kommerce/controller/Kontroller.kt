package com.wolfe.kommerce.controller

import com.wolfe.kommerce.event.TriggerReadEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
public class Kontroller(val eventPublisher: ApplicationEventPublisher){

    @GetMapping("/")
    fun kotMap():Map<String,String> {
        eventPublisher.publishEvent(TriggerReadEvent(this))
        return mapOf(Pair("You","Made it"))}
}
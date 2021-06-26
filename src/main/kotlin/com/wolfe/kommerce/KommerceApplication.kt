package com.wolfe.kommerce

import com.wolfe.kommerce.service.CsvExtractor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.io.File

@Slf4j
@EnableAsync
//@EnableWebFlux
@SpringBootApplication
class KommerceApplication
fun main(args: Array<String>) {
    runApplication<KommerceApplication>(*args)
}

//@Configuration
//class WebFluxConfig : WebFluxConfigurer

package com.wolfe.kommerce

import com.wolfe.kommerce.service.CsvExtractor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Slf4j
@SpringBootApplication
class KommerceApplication

fun main(args: Array<String>) {

    runApplication<KommerceApplication>(*args)
}

@Component
class Th {

    @Value("\${my.key}")
    lateinit var key: String

    @EventListener
    public fun d(c: ContextRefreshedEvent) {
        println(key)
//        val file = File("./thinglite.csv")
//        CsvExtractor().csvToModel(file)
    }

}

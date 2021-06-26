package com.wolfe.kommerce.controller

import com.wolfe.kommerce.model.LoanEntity
import com.wolfe.kommerce.service.LoanService
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
class LoanKontroller(private val loanService: LoanService) {

    @GetMapping("/loans")
    fun getLoans(@RequestParam state: String, @RequestParam page: Int) = loanService.getLoanState(state,PageRequest.of(page,100))

    @GetMapping(value = ["/loan-flux"],produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLoanFlux(@RequestParam amount: Int) : Flux<out LoanEntity> = loanService.emitLoans(amount).delayElements(Duration.ofMillis(1000L))
}
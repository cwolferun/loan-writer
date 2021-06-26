package com.wolfe.kommerce.service

import com.wolfe.kommerce.model.LoanEntity
import com.wolfe.kommerce.repository.LoanRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random

@Service
class LoanService(private val loanRepository: LoanRepository) {

    fun getLoanState(state: String, pageable: Pageable) = loanRepository.findLoanEntitiesByBankState(state, pageable)

    fun emitLoans(amount: Int): Flux<out LoanEntity> {
        val fromStream: Flux<LoanEntity> = Flux.create {
            for (x in 1..amount) {
                val randLong = Random.nextLong(1L,1_500_000L)
                println("fetching id ${randLong}")
//                Thread.sleep(1000)
                it.next(loanRepository.findById(randLong).get())
            }
            it.complete()
        }
        println("Flux has been returned")
        return fromStream
    }
}
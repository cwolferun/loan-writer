package com.wolfe.kommerce.repository

import com.wolfe.kommerce.model.LoanEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LoanRepository : JpaRepository<LoanEntity, Long> {
    fun findLoanEntitiesByBankState(state: String, pageable: Pageable): List<LoanEntity>
}
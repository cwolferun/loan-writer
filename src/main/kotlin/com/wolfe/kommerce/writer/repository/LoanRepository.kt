package com.wolfe.kommerce.writer.repository

import com.wolfe.kommerce.writer.model.LoanEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LoanRepository : JpaRepository<LoanEntity, Long>
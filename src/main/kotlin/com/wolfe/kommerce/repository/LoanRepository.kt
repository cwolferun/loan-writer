package com.wolfe.kommerce.repository

import com.wolfe.kommerce.model.LoanEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LoanRepository : JpaRepository<LoanEntity,Long>
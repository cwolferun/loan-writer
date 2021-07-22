package com.wolfe.kommerce.service

import com.sun.source.tree.BinaryTree
import com.wolfe.kommerce.model.Loan
import com.wolfe.kommerce.model.LoanEntity
import com.wolfe.kommerce.repository.LoanRepository
import org.modelmapper.ModelMapper
import org.modelmapper.TypeMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.CharBuffer
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

@Service
public class CsvExtractor(val loanRepository: LoanRepository) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val mapper: TypeMap<Loan, LoanEntity> = ModelMapper().typeMap(Loan::class.java, LoanEntity::class.java);
    private val executorService: ExecutorService = Executors.newFixedThreadPool(8)

    fun csvRowToEntity(file: File) {
        val loanEntities = mutableListOf<LoanEntity>()
        val long = AtomicLong(0)

        file.readLines().parallelStream().forEach { line ->
            if (!line.startsWith("approval_date")) {
                var insideQuote = false
                val topRow = mutableListOf<String?>()
                var buffer = CharBuffer.allocate(256)
                line.toCharArray().forEach { char ->
                    if (char == ',' && !insideQuote) {
                        val word = String(buffer.array().copyOfRange(0, buffer.position()))
                        if ("" != word)
                            topRow.add(word)
                        else topRow.add(null)
                        buffer = CharBuffer.allocate(256)
                    } else if (char == '"') {
                        insideQuote = !insideQuote
                    } else
                        buffer.put(char)
                }
                topRow.add(String(buffer.subSequence(0, buffer.position()).array().copyOfRange(0, buffer.position())))
                val dates = mutableListOf<LocalDate?>()
                dates.add(topRow[0]?.let { LocalDate.parse(it) })
                dates.add(topRow[2]?.let { LocalDate.parse(it) })
                dates.add(topRow[14]?.let { LocalDate.parse(it) })
                dates.add(topRow[17]?.let { LocalDate.parse(it) })
                dates.add(topRow[27]?.let { LocalDate.parse(it) })
                val loan = Loan(long.incrementAndGet(), dates[0], topRow[1]?.toInt(), dates[1], topRow[3], topRow[4], topRow[5], topRow[6], topRow[7],
                        topRow[8], topRow[9], topRow[10], topRow[11], topRow[12], topRow[13], dates[2], topRow[15]?.toInt(),
                        topRow[16], dates[3], topRow[18], topRow[19], topRow[20]?.toLong(), topRow[21]?.toLong(), topRow[22]?.toDouble(), topRow[23]?.toInt(),
                        topRow[24], topRow[25], topRow[26], dates[4], topRow[28], topRow[29], topRow[30], topRow[31],
                        topRow[32], topRow[33]?.toLong(), topRow[34]?.toInt(), topRow[35])

                val loanEntity = mapper.map(loan)
                loanEntities.add(loanEntity)
            }
        }
            val listOfLists = mutableListOf<MutableList<LoanEntity>>(mutableListOf())
            var listNumber = 0
            loanEntities.forEach {
                listOfLists[listNumber].add(it)
                if (listOfLists[listNumber].size == 5000) {
                    log.info("Filled list number {}",listNumber+1)
                    listOfLists.add(mutableListOf())
                    listNumber++
                }
            }


            listOfLists.forEach { executorService.submit { loanRepository.saveAll(it) } }
    }
}
package com.wolfe.kommerce.service

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
                dates.add(topRow.get(0)?.let { LocalDate.parse(it) })
                dates.add(topRow.get(2)?.let { LocalDate.parse(it) })
                dates.add(topRow.get(14)?.let { LocalDate.parse(it) })
                dates.add(topRow.get(17)?.let { LocalDate.parse(it) })
                dates.add(topRow.get(27)?.let { LocalDate.parse(it) })

                val loan = Loan(long.incrementAndGet(), dates.get(0), topRow.get(1)?.toInt(), dates.get(1), topRow.get(3), topRow.get(4), topRow.get(5), topRow.get(6), topRow.get(7),
                        topRow.get(8), topRow.get(9), topRow.get(10), topRow.get(11), topRow.get(12), topRow.get(13), dates.get(2), topRow.get(15)?.toInt(),
                        topRow.get(16), dates.get(3), topRow.get(18), topRow.get(19), topRow.get(20)?.toLong(), topRow.get(21)?.toLong(), topRow.get(22)?.toDouble(), topRow.get(23)?.toInt(),
                        topRow.get(24), topRow.get(25), topRow.get(26), dates.get(4), topRow.get(28), topRow.get(29), topRow.get(30), topRow.get(31),
                        topRow.get(32), topRow.get(33)?.toLong(), topRow.get(34)?.toInt(), topRow.get(35))

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
package com.wolfe.kommerce.service

import com.wolfe.kommerce.model.Loan
import java.io.File
import java.nio.CharBuffer

public class CsvExtractor {

    fun csvToModel(file: File) {
        file.forEachLine {
            if (!it.startsWith("approval_date")) {
                var insideQuote = false
                val topRow = mutableListOf<String?>()
                var buffer = CharBuffer.allocate(256)
                it.toCharArray().forEach {
                    if (it.equals(',')&& !insideQuote) {
                        val word = String(buffer.array())
                        if ("" != word)
                            topRow.add(word)
                        else topRow.add(null)

                        buffer = CharBuffer.allocate(256)
                    } else if (it.equals('"')) {
                        insideQuote = !insideQuote
                    } else
                        buffer.put(it)
                }
                topRow.add(String(buffer.array()))

                println(topRow)
                val loan = Loan(topRow.get(0), topRow.get(1), topRow.get(2), topRow.get(3), topRow.get(4), topRow.get(5), topRow.get(6), topRow.get(7),
                        topRow.get(8), topRow.get(9), topRow.get(10), topRow.get(11), topRow.get(12), topRow.get(13), topRow.get(14), topRow.get(15),
                        topRow.get(16), topRow.get(17), topRow.get(18), topRow.get(19), topRow.get(20), topRow.get(21), topRow.get(22), topRow.get(23),
                        topRow.get(24), topRow.get(25), topRow.get(26), topRow.get(27), topRow.get(28), topRow.get(29), topRow.get(30), topRow.get(31),
                        topRow.get(32), topRow.get(33), topRow.get(34), topRow.get(35))

                print(loan)

            }
        }
    }
}
package com.wolfe.kommerce.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ListBucketsRequest
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.wolfe.kommerce.event.TriggerReadEvent
import lombok.extern.slf4j.Slf4j
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream

@Slf4j
@Service
public class S3Service(val s3: AmazonS3, val csvExtractor: CsvExtractor) {

    @Async
    @EventListener
    fun getS3File(event: TriggerReadEvent) {
        s3.listBuckets(ListBucketsRequest()).forEach { println(it) }
        s3.listObjects(ListObjectsRequest("us-commerce", "sba_loans", "/", null, 14)).objectSummaries.forEach { println(it) }
        s3.listBuckets(ListBucketsRequest()).forEach { println(it) }
        val file = Files.createTempFile("thing","csv")

        val read = s3.getObject(GetObjectRequest("us-commerce", "sba_loans/202004220103/sba_loans-7a_data__202004220103__202004220103.csv.gz"))
                .objectContent

        val inZip = GZIPInputStream(read)
        val out = FileOutputStream(file.toFile())

        out.write(inZip.readAllBytes())
        out.close()
        inZip.close()

        csvExtractor.csvRowToEntity(/*File("./thing.csv")  */file.toFile())
    }
}
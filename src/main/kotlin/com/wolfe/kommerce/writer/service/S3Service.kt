package com.wolfe.kommerce.writer.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ListBucketsRequest
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.wolfe.kommerce.writer.event.TriggerReadEvent
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.zip.GZIPInputStream

@Slf4j
@Service
public class S3Service(val s3: AmazonS3, val csvExtractor: CsvExtractor) {

    @Value("s3ObjectName")
    lateinit var s3ObjectName:String

    @Async
    @EventListener
    fun getS3File(event: TriggerReadEvent) {
        s3.listBuckets(ListBucketsRequest()).forEach { println(it) }
        s3.listObjects(ListObjectsRequest("us-commerce", "sba_loans", "/", null, 14)).objectSummaries.forEach { println(it) }
        s3.listBuckets(ListBucketsRequest()).forEach { println(it) }
        val file = Files.createTempFile("thing","csv")

        val read = s3.getObject(GetObjectRequest("us-commerce", s3ObjectName))
                .objectContent

        val inZip = GZIPInputStream(read)
        val out = FileOutputStream(file.toFile())

        out.write(inZip.readAllBytes())
        out.close()
        inZip.close()

        csvExtractor.csvRowToEntity(/*File("./thing.csv")  */file.toFile())
    }
}
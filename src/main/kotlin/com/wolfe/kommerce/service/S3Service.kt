package com.wolfe.kommerce.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ListBucketsRequest
import com.amazonaws.services.s3.model.ListObjectsRequest
import lombok.extern.slf4j.Slf4j
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream

@Slf4j
@Service
public class S3Service(val s3: AmazonS3) {

//    @EventListener
    fun getS3File(/*event: ContextRefreshedEvent*/) {
        s3.listBuckets(ListBucketsRequest()).forEach { println(it) }
        s3.listObjects(ListObjectsRequest("us-commerce","sba_loans/202004220103/","","/",4)).objectSummaries.forEach { println(it) }
        val file = Files.createFile(Path.of("./thing.csv"))

        val read = s3.getObject(GetObjectRequest("us-commerce", "sba_loans/202004220103/sba_loans-7a_data__202004220103__202004220103.csv.gz"))
                .objectContent

        val inZip = GZIPInputStream(read)
        val out = FileOutputStream(file.toFile())
//        while (read.read())
        out.write(inZip.readAllBytes())
        out.close()
        inZip.close()

        println("dadadada-da-da-dadada")
    }
}
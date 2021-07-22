package com.wolfe.kommerce.writer.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class S3Config {

    @Bean
    fun getS3(): AmazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain())
            .withEndpointConfiguration(AwsClientBuilder
                    .EndpointConfiguration("s3.us-east-1.amazonaws.com/us-commerce",
                            "us-east-1"))
            .build()
}
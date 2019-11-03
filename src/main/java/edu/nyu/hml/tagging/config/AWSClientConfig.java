package edu.nyu.hml.tagging.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AWSClientConfig {

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${aws.access_key_id}")
    private String keyId;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket_name}")
    private String bucketName;

    private BasicAWSCredentials credProvider() {
        BasicAWSCredentials cred = new BasicAWSCredentials(keyId, secretKey);
        return cred;
    }

    @Bean
    public AmazonS3 getS3Client() {
        AmazonS3 s3Client =
            AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(this.credProvider()))
                .withRegion(Regions.fromName(region))
                .build();
        return s3Client;
    }

    @Bean
    public DynamoDBMapper getDynamoClient() {
        AmazonDynamoDB client =
                AmazonDynamoDBClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(this.credProvider()))
                        .withRegion(Regions.fromName(region))
                        .build();
        DynamoDBMapper dynamoMapper = new DynamoDBMapper(client);
        return dynamoMapper;
    }
}

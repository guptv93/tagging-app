package edu.nyu.hml.tagging.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
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

    private AmazonS3 s3Client;

    private BasicAWSCredentials credProvider() {
        BasicAWSCredentials cred = new BasicAWSCredentials(keyId, secretKey);
        return cred;
    }

    @PostConstruct
    private void setS3Client() {
        s3Client =
            AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(this.credProvider()))
                .withRegion(Regions.fromName(region))
                .build();
    }
}

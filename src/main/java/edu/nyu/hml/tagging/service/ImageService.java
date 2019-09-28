package edu.nyu.hml.tagging.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import edu.nyu.hml.tagging.config.AWSClientConfig;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private AWSClientConfig awsClient;

    private String getKeyFromIndex(String folderName, int index) {
        AmazonS3 s3Client = awsClient.getS3Client();
        String bucketName = awsClient.getBucketName();

        // Get Correct Key from s3 Response
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName)
            .withPrefix(folderName);
        ListObjectsV2Result result;
        while (true) {
            result = s3Client.listObjectsV2(req);
            String token = result.getNextContinuationToken();
            req.setContinuationToken(token);
            if (!result.isTruncated() && index >= result.getKeyCount()) {
                throw new IndexOutOfBoundsException("You have exceeded the folder-size!");
            } else if (result.isTruncated()) {
                index = index - result.getKeyCount();
            } else if (index < result.getKeyCount()) {
                break;
            }
        }
        S3ObjectSummary objSummary = result.getObjectSummaries().get(index);
        return objSummary.getKey();
    }

    private String getUrlFromKey(String key) {
        AmazonS3 s3Client = awsClient.getS3Client();
        String bucketName = awsClient.getBucketName();

        // Set the presigned URL to expire after 1 hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String getUrlFromIndex(String folderName, int index) {
        return getUrlFromKey(getKeyFromIndex(folderName, index));
    }

}

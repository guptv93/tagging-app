package edu.nyu.hml.tagging.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import edu.nyu.hml.tagging.config.AWSClientConfig;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

    @Autowired
    private AWSClientConfig awsClient;

    @ResponseBody
    @GetMapping("/image/{folderName}/{imgId}")
    public String getImageUrl(
        @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId) {
        if (imgId <= 0) {
            throw new RuntimeException("Image ID should be greater than zero");
        }
        AmazonS3 s3Client = awsClient.getS3Client();
        String bucketName = awsClient.getBucketName();

        // Get Correct Key from s3 Response
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName)
            .withPrefix(folderName).withMaxKeys(3);
        ListObjectsV2Result result;
        while (true) {
            result = s3Client.listObjectsV2(req);
            String token = result.getNextContinuationToken();
            req.setContinuationToken(token);
            if (imgId < result.getKeyCount() || !result.isTruncated()) {
                if (imgId >= result.getKeyCount()) {
                    throw new RuntimeException("You have exceeded the index!");
                }
                break;
            }
            imgId = imgId - result.getKeyCount();
        }
        S3ObjectSummary objSummary = result.getObjectSummaries().get(imgId);

        // Set the presigned URL to expire after 15 mins.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 15;
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, objSummary.getKey())
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}

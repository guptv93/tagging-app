package edu.nyu.hml.tagging.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import edu.nyu.hml.tagging.config.AWSClientConfig;
import edu.nyu.hml.tagging.service.ImageService;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @ResponseBody
    @GetMapping("/image/{folderName}/{imgId}")
    public String getImageUrl(
            @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId) {
        if (imgId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image ID should be greater than 0.");
        }
        try {
            return imageService.getUrlFromIndex(folderName, imgId);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

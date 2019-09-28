package edu.nyu.hml.tagging.service;

import edu.nyu.hml.tagging.config.AWSClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    @Autowired
    private AWSClientConfig awsClient;

    public int getImageIndex(String folderName) {
        return 1;
    }
}

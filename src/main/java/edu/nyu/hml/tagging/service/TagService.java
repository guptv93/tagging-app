package edu.nyu.hml.tagging.service;

import edu.nyu.hml.tagging.config.AWSClientConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private AWSClientConfig awsClient;

    public List<String> getTags(String folderName) {
        List<String> tags = new ArrayList<>();
        tags.add("mother");
        tags.add("father");
        tags.add("wardrobe");
        return tags;
    }

}

package edu.nyu.hml.tagging.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class TagService {

    @Autowired
    private Jedis redisClient;

    public List<String> getTags(String folderName) {
        Set<String> zSet = redisClient.zrevrange(folderName, 0, -1);
        return new ArrayList<>(zSet);
    }

    public void addTags(String folderName, List<String> newTags) {
        long millis = System.currentTimeMillis();
        for(String tag : newTags) {
            redisClient.zadd(folderName, millis++, tag);
        }
    }

}

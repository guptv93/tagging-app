package edu.nyu.hml.tagging.repository;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class TagsRepository {

    @Autowired
    private Jedis redisClient;

    public Set<String> getTags(String folderName) {
        Set<String> zSet = redisClient.zrevrange(folderName, 0, -1);
        return zSet;
    }

    public void addTag(String folderName, String tag, double score) {
        redisClient.zadd(folderName, score, tag);
    }
}

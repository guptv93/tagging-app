package edu.nyu.hml.tagging.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class TagService {

    public static final String HASH_KEY = "FolderName";
    public static final String RANGE_KEY = "ImageId";
    public static final String TAG_KEY = "Tags";
    public static final String FILE_NAME = "FileName";
    public static final String AUTHOR_KEY = "Author";

    @Autowired
    private Jedis redisClient;

    @Autowired
    private DynamoDB dynamoClient;

    @Value("${aws.dynamodb.table_name}")
    private String tableName;

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

    public boolean tagImage(String folderName, int imageId, String fileName, String author, List<String> tags) {
        Table table = dynamoClient.getTable(tableName);
        Item item = new Item().withPrimaryKey(HASH_KEY, folderName, RANGE_KEY, imageId);
        item.withStringSet(TAG_KEY, new HashSet<>(tags));
        item.withString(FILE_NAME, fileName);
        item.withString(AUTHOR_KEY, author);
        table.putItem(item);
        return true;
    }

    public List<String> getImageTags(String folderName, int imageId) {
        Table table = dynamoClient.getTable(tableName);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(HASH_KEY, folderName, RANGE_KEY, imageId);
        Item item = table.getItem(spec);
        if(item == null || item.isNull(TAG_KEY)) return  new ArrayList<>();
        Set<String> tagSet = item.getStringSet(TAG_KEY);
        return new ArrayList<>(tagSet);
    }

}

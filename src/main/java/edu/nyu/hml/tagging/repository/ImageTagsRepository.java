package edu.nyu.hml.tagging.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageTagsRepository {

    @Autowired
    DynamoDBMapper dbMapper;

    public void saveItem(ImageTags imageTags) {
        dbMapper.save(imageTags);
    }

    public ImageTags getItem(String folderName, int imageId) {
        return dbMapper.load(ImageTags.class, folderName, imageId);
    }

    public List<ImageTags> getItemsAsc(String folderName) {
        HashMap<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
        valueMap.put(":fn", new AttributeValue().withS(folderName));
        return dbMapper.query(ImageTags.class, new DynamoDBQueryExpression<ImageTags>()
                .withKeyConditionExpression("folder_name = :fn")
                .withExpressionAttributeValues(valueMap));
    }

    public List<ImageTags> getItemsDesc(String folderName) {
        HashMap<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
        valueMap.put(":fn", new AttributeValue().withS(folderName));
        return dbMapper.query(ImageTags.class, new DynamoDBQueryExpression<ImageTags>()
                .withKeyConditionExpression("FolderName = :fn")
                .withExpressionAttributeValues(valueMap)
                .withScanIndexForward(false));
    }
}

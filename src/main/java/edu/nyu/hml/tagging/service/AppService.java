package edu.nyu.hml.tagging.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    @Autowired
    private DynamoDB dynamoDB;

    @Value("${aws.dynamodb.table_name}")
    private String tableName;

    public int getImageIndex(String folderName) {
        Table table = dynamoDB.getTable(tableName);
        QuerySpec spec = new QuerySpec().withKeyConditionExpression(TagService.HASH_KEY + " = :v_id")
                .withValueMap(new ValueMap().withString(":v_id", folderName))
                .withScanIndexForward(false).withMaxResultSize(1);
        try {
            ItemCollection<QueryOutcome> items = table.query(spec);
            if (items.firstPage().iterator().hasNext()) {
                Item item = items.firstPage().iterator().next();
                return item.getInt(TagService.RANGE_KEY) + 1;
            } else {
                return 1;
            }
        } catch(Exception e) {
            System.out.println("Couldn't get the last tagged image id for " + folderName + " dataset.");
            return 1;
        }

    }
}

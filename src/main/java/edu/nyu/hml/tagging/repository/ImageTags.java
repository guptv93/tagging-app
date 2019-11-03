package edu.nyu.hml.tagging.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "ImageTagsTable")
public class ImageTags {

    @Setter
    private String folderName;

    @Setter
    private int imageId;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "FileName")
    private String fileName;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "Author")
    private String author;

    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "Tags")
    private Set<String> tags;

    @DynamoDBHashKey(attributeName = "FolderName")
    public String getFolderName() {
        return folderName;
    }

    @DynamoDBRangeKey(attributeName = "ImageId")
    public int getImageId() {
        return imageId;
    }


}

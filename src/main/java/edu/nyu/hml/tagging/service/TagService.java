package edu.nyu.hml.tagging.service;

import edu.nyu.hml.tagging.repository.ImageTags;
import edu.nyu.hml.tagging.repository.ImageTagsRepository;
import edu.nyu.hml.tagging.repository.TagsRepository;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagsRepository tagsRepo;

    @Autowired
    private ImageTagsRepository imageTagsRepo;

    public List<String> getTags(String folderName) {
        return new ArrayList<>(tagsRepo.getTags(folderName));
    }

    public void addTags(String folderName, List<String> newTags) {
        long millis = System.currentTimeMillis();
        for(String tag : newTags) {
            tagsRepo.addTag(folderName, tag, millis--);
        }
    }

    public void tagImage(String folderName, int imageId, String fileName, String author, List<String> tags) {
        Set<String> tagsSet = new HashSet<>(tags);
        imageTagsRepo.saveItem(new ImageTags(folderName, imageId, fileName, author, tagsSet));
    }

    public int getMaxTaggedImageId(String folderName) {
        List<ImageTags> itemsList = imageTagsRepo.getItemsDesc(folderName);
        if(itemsList.size() == 0) return 1;
        else return itemsList.get(0).getImageId();
    }

    public List<String> getImageTags(String folderName, int imageId) {
        ImageTags imageTags = imageTagsRepo.getItem(folderName, imageId);
        if(imageTags != null) return new ArrayList<>(imageTags.getTags());
        else return new ArrayList<>();
    }

}

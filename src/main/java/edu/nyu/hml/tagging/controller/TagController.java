package edu.nyu.hml.tagging.controller;

import edu.nyu.hml.tagging.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags/{folderName}")
    public ModelAndView getTags(Map<String, Object> model,
            @PathVariable("folderName") String folderName) {
        model.put("tags", tagService.getTags(folderName));
        return new ModelAndView("tags", model);
    }

    @PostMapping("/tags/{folderName}")
    public ModelAndView addTags(Map<String, Object> model,
            @PathVariable("folderName") String folderName,
            @RequestBody String tagString) {
        String[] tags = tagString.split(",");
        List<String> cleanTags = new ArrayList<>();
        for (String tag : tags) {
            tag = tag.trim();
            if (!tag.isEmpty()) {
                cleanTags.add(tag);
            }
        }
        tagService.addTags(folderName, cleanTags);
        model.put("tags", tagService.getTags(folderName));
        return new ModelAndView("tags", model);
    }

    @ResponseBody
    @PutMapping("/tags/{folderName}")
    public void updateTags(@PathVariable("folderName") String folderName,
            @RequestBody List<String> tags) {
        tagService.addTags(folderName, tags);
    }

    @ResponseBody
    @GetMapping("/imagetag/{folderName}/{imgId}")
    public List<String> getImageTags(
            @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId) {
        try {
            return tagService.getImageTags(folderName, imgId);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Couldn't fetch image tags from DynamoDB");
        }
    }

    @ResponseBody
    @PostMapping("/imagetag/{folderName}/{imgId}")
    public void addImageTags(
            @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId,
            @RequestBody ImageTagDocument tagDoc) {
        try {
            tagService.tagImage(folderName, imgId, tagDoc.getFileName(), tagDoc.getAuthor(), tagDoc.getTags());
            tagService.addTags(folderName, tagDoc.getTags());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Couldn't save tags for image id : " + imgId + ". Please try again!");
        }
    }

}
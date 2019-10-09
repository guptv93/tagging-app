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

    @ResponseBody
    @PostMapping("/tags/{folderName}")
    public void addTags(@PathVariable("folderName") String folderName, @RequestBody String tagString) {
        String[] tags = tagString.split(",");
        List<String> cleanTags = new ArrayList<>();
        for(String tag: tags) {
            tag = tag.trim();
            if(!tag.isEmpty()) {
                cleanTags.add(tag);
            }
        }
        tagService.addTags(folderName, cleanTags);
        System.out.println(tagService.getTags(folderName));
    }

}
package edu.nyu.hml.tagging.controller;

import edu.nyu.hml.tagging.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags/{folderName}")
    @ResponseBody
    public List<String> getTags(@PathVariable("folderName") String folderName) {
        return tagService.getTags(folderName);
    }

}
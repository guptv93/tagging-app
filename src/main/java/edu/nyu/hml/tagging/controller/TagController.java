package edu.nyu.hml.tagging.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TagController {

    @GetMapping("/tags/{db}")
    @ResponseBody
    public List<String> getTags(@PathVariable("db") String db) {
        List<String> tags = new ArrayList<>();
        tags.add("mother");
        tags.add("wardrobe");
        return tags;
    }

}

package edu.nyu.hml.tagging.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @GetMapping("/app/{folderName}/{imgId}")
    public ModelAndView startApp(Map<String, Object> model,
        @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId) {
        model.put("helloString", folderName);
        return new ModelAndView("main", model);
    }

}

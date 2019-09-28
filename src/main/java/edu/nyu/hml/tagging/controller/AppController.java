package edu.nyu.hml.tagging.controller;

import edu.nyu.hml.tagging.service.AppService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping("/app/{folderName}")
    public ModelAndView startApp(Map<String, Object> model,
            @PathVariable("folderName") String folderName, @RequestParam(required = false) String index) {
        if(index != null) {
            model.put("imageIndex", index);
        }
        else {
            model.put("imageIndex", appService.getImageIndex(folderName));
        }
        model.put("folderName", folderName);
        return new ModelAndView("main", model);
    }

}

package edu.nyu.hml.tagging.controller;

import edu.nyu.hml.tagging.service.ImageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ThumbnailsController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/thumbnails/{folderName}/{imgId}")
    public ModelAndView getThumbnails(Map<String, Object> model,
            @PathVariable("folderName") String folderName, @PathVariable("imgId") int imgId) {
        if (imgId <= 0) {
            throw new RuntimeException("Image ID should be greater than zero");
        }
        List<String> imgUrls = new ArrayList<>();
        for(int i = 0; i < 36; i++) {
            try {
                imgUrls.add(imageService.getUrlFromIndex(folderName, imgId + i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        model.put("imgUrls", imgUrls);
        model.put("folderName", folderName);
        model.put("imgId", imgId);
        return new ModelAndView("thumbnails", model);
    }

}

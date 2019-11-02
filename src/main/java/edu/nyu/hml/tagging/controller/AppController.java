package edu.nyu.hml.tagging.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.google.common.collect.Lists;
import edu.nyu.hml.tagging.service.AppService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class AppController implements ErrorController {

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

    @GetMapping("/error")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleError() {
        return "Try accessing /app/[dataset] with proper x-auth header.";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}

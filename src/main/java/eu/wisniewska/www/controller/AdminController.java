package eu.wisniewska.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping({"/_admin", "/_admin/"})
    public String index() {
        return "admin/index";
    }

}

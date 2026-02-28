package eu.wisniewska.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUserController {

    @GetMapping("/_admin/users")
    public String index(Model model) {
        model.addAttribute("activePage", "users");
        model.addAttribute("pageTitle", "Admin User Listing");

        return "admin/users/index";
    }

    @GetMapping("/_admin/users/add")
    public String add(Model model) {
        model.addAttribute("activePage", "users");
        model.addAttribute("pageTitle", "Adding a new Admin User");

        return "admin/users/add";
    }


}

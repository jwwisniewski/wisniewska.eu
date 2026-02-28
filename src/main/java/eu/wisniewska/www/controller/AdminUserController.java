package eu.wisniewska.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/_admin/users")
public class AdminUserController {

    @ModelAttribute()
    public void commonAttributes(Model model) {
        model.addAttribute("activePage", "users");
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Admin User Listing");

        return "admin/users/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("pageTitle", "Adding a new Admin User");

        return "admin/users/add";
    }


}

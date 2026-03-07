package eu.wisniewska.www.controller;

import eu.wisniewska.www.entity.AdminUserRole;
import eu.wisniewska.www.form.AdminUserCreateForm;
import eu.wisniewska.www.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/_admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @ModelAttribute()
    public void commonAttributes(Model model) {
        model.addAttribute("activePage", "users");
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Admin User Listing");

        model.addAttribute("users", adminUserService.findAll());

        return "admin/users/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("pageTitle", "Adding a new Admin User");
        model.addAttribute("adminUserCreateForm", new AdminUserCreateForm());
        model.addAttribute("adminUserRoles", AdminUserRole.values());

        return "admin/users/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute AdminUserCreateForm adminUserCreateForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminUserRoles", AdminUserRole.values());
            return "admin/users/add";
        }

        adminUserService.save(adminUserCreateForm);

        return "redirect:/_admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {

        adminUserService.delete(id);

        return "redirect:/_admin/users";
    }
}

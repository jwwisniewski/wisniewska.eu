package eu.wisniewska.www.controller;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.entity.AdminUserRole;
import eu.wisniewska.www.form.AdminUserCreateForm;
import eu.wisniewska.www.form.AdminUserEditForm;
import eu.wisniewska.www.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/_admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
    public String save(@Valid @ModelAttribute AdminUserCreateForm adminUserCreateForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminUserRoles", AdminUserRole.values());
            return "admin/users/add";
        }

        adminUserService.save(adminUserCreateForm);
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully");

        return "redirect:/_admin/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable UUID id) {
        AdminUser adminUser = adminUserService.findById(id);
        model.addAttribute("pageTitle", "Edit '" + adminUser.getUsername() + "'");

        AdminUserEditForm adminUserEditForm = new AdminUserEditForm(adminUser);

        model.addAttribute("adminUserEditForm", adminUserEditForm);
        model.addAttribute("adminUserRoles", AdminUserRole.values());

        return "admin/users/edit";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute AdminUserEditForm adminUserEditForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminUserRoles", AdminUserRole.values());
            return "admin/users/edit";
        }
        adminUserService.update(adminUserEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");

        return "redirect:/_admin/users/edit/" + adminUserEditForm.getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {

        adminUserService.findById(id);
        adminUserService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");

        return "redirect:/_admin/users";
    }
}

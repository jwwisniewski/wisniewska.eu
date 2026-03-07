package eu.wisniewska.www.service;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.form.AdminUserCreateForm;
import eu.wisniewska.www.form.AdminUserEditForm;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AdminUser findById(UUID id) {
        return adminUserRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));
    }

    public List<AdminUser> findAll() {
        return adminUserRepository.findAll();
    }

    public void save(AdminUserCreateForm adminUserCreateForm) {

        AdminUser adminUser = new AdminUser();

        adminUser.setUsername(adminUserCreateForm.getUsername());
        adminUser.setRole(adminUserCreateForm.getRole());
        adminUser.setPassword(
                bCryptPasswordEncoder.encode(adminUserCreateForm.getPassword())
        );

        adminUserRepository.save(adminUser);
    }

    public void update(AdminUserEditForm adminUserEditForm) {
        AdminUser adminUser = findById(adminUserEditForm.getId());
        adminUser.setRole(adminUserEditForm.getRole());
        if (adminUserEditForm.getPassword() != null) {
            adminUser.setPassword(
                    bCryptPasswordEncoder.encode(adminUserEditForm.getPassword())
            );
        }
        adminUserRepository.save(adminUser);
    }

    public void delete(UUID id) {
        adminUserRepository.deleteById(id);
    }
}

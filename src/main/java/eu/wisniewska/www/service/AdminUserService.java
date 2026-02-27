package eu.wisniewska.www.service;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<AdminUser> findAll() {
        return adminUserRepository.findAll();
    }

    public void save(AdminUser adminUser) {
        adminUser.setPassword(
                bCryptPasswordEncoder.encode(adminUser.getPassword())
        );
        adminUserRepository.save(adminUser);
    }
}

package eu.wisniewska.www.service;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminUserRepository adminUserRepository;
    @Value("${app.admin.username}")
    private String serviceAccountUsername;
    @Value("${app.admin.password}")
    private String serviceAccountPassword;
    @Value("${app.admin.role}")
    private String serviceAccountRole;

    public CustomUserDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder, AdminUserRepository adminUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AdminUser> adminUser = adminUserRepository.findByUsername(username);

        if (adminUser.isPresent()) {
            return new User(
                    adminUser.get().getUsername(),
                    adminUser.get().getPassword(),
                    List.of(
                            new SimpleGrantedAuthority("ROLE_%s".formatted(adminUser.get().getRole()))
                    )
            );
        }

        if (username.equals(serviceAccountUsername)) {
            return new User(
                    serviceAccountUsername,
                    bCryptPasswordEncoder.encode(serviceAccountPassword),
                    List.of(
                            new SimpleGrantedAuthority("ROLE_%s".formatted(serviceAccountRole))
                    )
            );
        }

        throw new UsernameNotFoundException("Admin user '%s' not found".formatted(username));
    }
}

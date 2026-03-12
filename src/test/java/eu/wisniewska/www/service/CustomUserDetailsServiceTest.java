package eu.wisniewska.www.service;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.entity.AdminUserRole;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private void givenTheServiceAccountExistsWithUsername(String username) {
        ReflectionTestUtils.setField(customUserDetailsService, "serviceAccountUsername", username);
        ReflectionTestUtils.setField(customUserDetailsService, "serviceAccountPassword", "servicepassword");
        ReflectionTestUtils.setField(customUserDetailsService, "serviceAccountRole", "ADMIN");
    }


    @Test
    void test_loadUserByUsername_WHEN_userDoesNotExist_THEN_throwsException() {

        when(adminUserRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    void test_loadUserByUsername_WHEN_onlyTheServiceUserExists_BUT_searchUserDoesNotExist_THEN_throwsException() {
        when(adminUserRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        givenTheServiceAccountExistsWithUsername("admin");

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    void test_loadUserByUsername_WHEN_onlyTheServiceUserExists_THEN_returnsServiceUser() {
        when(adminUserRepository.findByUsername("admin")).thenReturn(Optional.empty());
        givenTheServiceAccountExistsWithUsername("admin");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches("servicepassword", userDetails.getPassword()));
        assertEquals("ROLE_ADMIN", userDetails.getAuthorities().iterator().next().getAuthority());

    }

    @Test
    void test_loadUserByUsername_WHEN_onlyDbUserExists_THEN_returnsDbUser() {
        AdminUser user = new AdminUser();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("dbpassword"));
        user.setRole(AdminUserRole.USER);

        givenTheServiceAccountExistsWithUsername("admin");

        when(adminUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches("dbpassword", userDetails.getPassword()));
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void test_loadUserByUsername_WHEN_bothUsersExist_THEN_returnsDbUser() {
        AdminUser user = new AdminUser();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("dbpassword"));
        user.setRole(AdminUserRole.USER);

        when(adminUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches("dbpassword", userDetails.getPassword()));
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }


}

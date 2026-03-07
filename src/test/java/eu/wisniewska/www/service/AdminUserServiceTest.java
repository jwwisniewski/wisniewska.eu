package eu.wisniewska.www.service;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.entity.AdminUserRole;
import eu.wisniewska.www.form.AdminUserCreateForm;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {

    @InjectMocks
    private AdminUserService adminUserService;

    @Mock
    private AdminUserRepository adminUserRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void test_findAll_WHEN_noData_THEN_returnsEmptyList() {
        when(adminUserRepository.findAll()).thenReturn(List.of());
        List<AdminUser> result = adminUserService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void test_findAll_WHEN_hasData_THEN_returnsNonEmptyList() {
        when(adminUserRepository.findAll()).thenReturn(List.of(new AdminUser()));
        List<AdminUser> result = adminUserService.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_findAll_WHEN_hasMultipleData_THEN_returnsCorrectSize() {
        when(adminUserRepository.findAll()).thenReturn(List.of(new AdminUser(), new AdminUser()));
        List<AdminUser> result = adminUserService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void test_save_WHEN_passwordIsPlainText_THEN_encodesPassword() {
        AdminUserCreateForm adminUserCreateForm = new AdminUserCreateForm();
        adminUserCreateForm.setUsername("login");
        adminUserCreateForm.setPassword("plaintext");
        adminUserCreateForm.setRole(AdminUserRole.ADMIN);
        adminUserService.save(adminUserCreateForm);

        ArgumentCaptor<AdminUser> captor = ArgumentCaptor.forClass(AdminUser.class);
        verify(adminUserRepository).save(captor.capture());
        AdminUser savedUser = captor.getValue();
        assertNotNull(savedUser);
        assertTrue(bCryptPasswordEncoder.matches("plaintext", savedUser.getPassword()));
    }
}

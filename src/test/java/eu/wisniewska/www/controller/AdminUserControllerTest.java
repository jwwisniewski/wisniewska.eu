package eu.wisniewska.www.controller;

import eu.wisniewska.www.entity.AdminUser;
import eu.wisniewska.www.entity.AdminUserRole;
import eu.wisniewska.www.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminUserControllerTest {

    private static final String LISTING_URL = "/_admin/users";
    private static final String ADD_URL = "/_admin/users/add";
    private static final String SAVE_URL = "/_admin/users/save";
    private static final String DELETE_URL = "/_admin/users/delete";
    private static final String EDIT_URL = "/_admin/users/edit";
    private static final String UPDATE_URL = "/_admin/users/update";
    private static final String LOGIN_URL = "/login";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    static Stream<Arguments> invalidUpdateData() {
        return Stream.of(
                Arguments.of("ab", "ADMIN", "password too short"),
                Arguments.of("abcdefg", "ADMIN", "password 7 chars"),
                Arguments.of("a".repeat(256), "ADMIN", "password too long"),
                Arguments.of("password", "INVALID", "invalid role"),
                Arguments.of("password", "admin", "role wrong case"),
                Arguments.of("password", "SUPERADMIN", "non-existent role"),
                Arguments.of("password", "", "blank role"),
                Arguments.of("ab", "INVALID", "password too short and invalid role")
        );
    }

    static Stream<Arguments> invalidCreateData() {
        return Stream.of(
                Arguments.of("", "password", "ADMIN", "blank username"),
                Arguments.of("ab", "password", "ADMIN", "username too short"),
                Arguments.of("a".repeat(256), "password", "ADMIN", "username too long"),
                Arguments.of("   ", "password", "ADMIN", "whitespace-only username"),
                Arguments.of("admin", "", "ADMIN", "blank password"),
                Arguments.of("admin", "ab", "ADMIN", "password too short"),
                Arguments.of("admin", "a".repeat(256), "ADMIN", "password too long"),
                Arguments.of("admin", "   ", "ADMIN", "whitespace-only password"),
                Arguments.of("admin", "password", "", "blank role"),
                Arguments.of("admin", "password", "INVALID", "invalid role"),
                Arguments.of("admin", "password", "admin", "role wrong case"),
                Arguments.of("admin", "password", "SUPERADMIN", "non-existent role"),
                Arguments.of("", "", "", "all fields blank"),
                Arguments.of("ab", "ab", "INVALID", "all fields invalid")
        );
    }

    private AdminUser givenThereIsAUser() {
        AdminUser user = new AdminUser();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRole(AdminUserRole.ADMIN);
        return adminUserRepository.save(user);
    }

    @Test
    @WithAnonymousUser
    public void test_index_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(get(LISTING_URL))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_index_WHEN_authenticated_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(LISTING_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("activePage", "users"))
                .andExpect(model().attribute("pageTitle", "Admin User Listing"))
        ;
    }

    @Test
    @WithAnonymousUser
    public void test_add_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(get(ADD_URL))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_add_WHEN_authenticated_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(ADD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("activePage", "users"))
                .andExpect(model().attribute("pageTitle", "Adding a new Admin User"))
        ;
    }

    @Test
    @WithAnonymousUser
    public void test_save_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(post(SAVE_URL).with(csrf()))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_save_WHEN_validPayload_THEN_redirectsToListing() throws Exception {
        mockMvc
                .perform(
                        post(SAVE_URL)
                                .with(csrf())
                                .param("username", "test")
                                .param("password", "password")
                                .param("role", "ADMIN")
                )
                .andExpect(redirectedUrl(LISTING_URL))
        ;
    }

    @ParameterizedTest(name = "{index}: {3}")
    @WithMockUser(roles = "ADMIN")
    @MethodSource("invalidCreateData")
    public void test_save_WHEN_incorrectPayload_THEN_returnsFormWithErrors(
            String username,
            String password,
            String role,
            String testCase
    ) throws Exception {
        mockMvc
                .perform(
                        post(SAVE_URL)
                                .with(csrf())
                                .param("username", username)
                                .param("password", password)
                                .param("role", role)
                )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("admin/users/add"));
    }

    @Test
    @WithAnonymousUser
    public void test_delete_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(post(DELETE_URL).with(csrf()))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_delete_WHEN_validId_THEN_redirectsToListing() throws Exception {
        AdminUser user = givenThereIsAUser();

        mockMvc
                .perform(
                        post(DELETE_URL + "/" + user.getId())
                                .with(csrf())
                )
                .andExpect(redirectedUrl(LISTING_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_delete_WHEN_invalidId_THEN_returnsHTTP404() throws Exception {
        mockMvc
                .perform(
                        post(DELETE_URL + "/" + UUID.randomUUID())
                                .with(csrf())
                )
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @WithAnonymousUser
    public void test_edit_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(get(EDIT_URL + UUID.randomUUID()))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_edit_WHEN_authenticated_BUT_entityDoesNotExist_THEN_returnsHTTP404() throws Exception {
        mockMvc
                .perform(get(EDIT_URL + UUID.randomUUID()))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_edit_WHEN_authenticated_THEN_returnsHTTP200() throws Exception {
        AdminUser user = givenThereIsAUser();

        mockMvc
                .perform(get(EDIT_URL + "/" + user.getId()))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithAnonymousUser
    public void test_update_WHEN_notAuthenticated_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(post(UPDATE_URL).with(csrf()))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_update_WHEN_authenticated__BUT_entityDoesNotExist_THEN_returnsHTTP404() throws Exception {
        mockMvc
                .perform(
                        post(UPDATE_URL)
                                .with(csrf())
                                .param("id", UUID.randomUUID().toString())
                                .param("password", "password")
                                .param("role", "ADMIN")
                )
                .andExpect(status().isNotFound())
        ;
    }

    @ParameterizedTest(name = "{index}: {2}")
    @WithMockUser(roles = "ADMIN")
    @MethodSource("invalidUpdateData")
    public void test_update_WHEN_authenticated_BUT_invalidPayload_THEN_returnsFormWithErrors(
            String password,
            String role,
            String testCase
    ) throws Exception {
        AdminUser user = givenThereIsAUser();

        mockMvc
                .perform(
                        post(UPDATE_URL)
                                .with(csrf())
                                .param("id", user.getId().toString())
                                .param("password", password)
                                .param("role", role)
                )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("admin/users/edit"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_update_WHEN_authenticated_THEN_redirectsToEdit() throws Exception {
        AdminUser user = givenThereIsAUser();

        mockMvc
                .perform(
                        post(UPDATE_URL)
                                .with(csrf())
                                .param("id", user.getId().toString())
                                .param("password", "password")
                                .param("role", "ADMIN")
                )
                .andExpect(redirectedUrl(EDIT_URL + "/" + user.getId()))
        ;
    }


}

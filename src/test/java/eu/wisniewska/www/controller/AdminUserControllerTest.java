package eu.wisniewska.www.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

    @Autowired
    private MockMvc mockMvc;

    static Stream<Arguments> invalidUserData() {
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

    @Test
    @WithAnonymousUser
    public void test_WHEN_notAuthenticated_AND_indexCalled_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(get(LISTING_URL))
                .andExpect(redirectedUrl("/login"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_WHEN_indexCalled_THEN_returnsHTTP200() throws Exception {
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
    public void test_WHEN_notAuthenticated_AND_addUserCalled_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(get(ADD_URL))
                .andExpect(redirectedUrl("/login"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_WHEN_addUserCalled_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(ADD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("activePage", "users"))
                .andExpect(model().attribute("pageTitle", "Adding a new Admin User"))
        ;
    }

    @Test
    @WithAnonymousUser
    public void test_WHEN_notAuthenticated_AND_saveUserCalled_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(post(SAVE_URL).with(csrf()))
                .andExpect(redirectedUrl("/login"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_WHEN_saveUserCalled_THEN_redirectsToListing() throws Exception {
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
    @MethodSource("invalidUserData")
    public void test_WHEN_incorrectPayload_THEN_returnsHTTP400(
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
    public void test_WHEN_notAuthenticated_AND_deleteCalled_THEN_redirectsToLogin() throws Exception {
        mockMvc
                .perform(post(DELETE_URL).with(csrf()))
                .andExpect(redirectedUrl("/login"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_WHEN_deleteCalled_THEN_redirectsToListing() throws Exception {
        mockMvc
                .perform(
                        post(DELETE_URL + "/" + UUID.randomUUID())
                                .with(csrf())
                )
                .andExpect(redirectedUrl(LISTING_URL))
        ;
    }

}

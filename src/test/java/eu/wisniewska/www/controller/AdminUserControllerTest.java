package eu.wisniewska.www.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mockMvc;


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
                .perform(get(SAVE_URL))
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
                                .param("password", "fake")
                                .param("role", "ADMIN")
                )
                .andExpect(redirectedUrl(LISTING_URL))
        ;
    }

}

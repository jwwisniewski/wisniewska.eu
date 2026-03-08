package eu.wisniewska.www.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {
    public static final String URL = "/_admin";
    public static final String LOGIN_URL = "/_admin/login";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void WHEN_indexCalledUnauthenticated_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(URL))
                .andExpect(redirectedUrl(LOGIN_URL))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void WHEN_indexCalledWithAdminRole_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(URL))
                .andExpect(status().isOk())
        ;
    }
}

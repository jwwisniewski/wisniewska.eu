package eu.wisniewska.www.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IndexControllerTest {
    public static final String URL = "/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void WHEN_indexCalled_THEN_returnsHTTP200() throws Exception {
        mockMvc
                .perform(get(URL))
                .andExpect(status().isOk())
        ;
    }
}

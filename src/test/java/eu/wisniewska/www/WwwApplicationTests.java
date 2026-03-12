package eu.wisniewska.www;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
class WwwApplicationTests {
    public static final String IMG_URL = "/img/background.jpg";
    public static final String CSS_URL = "/css/admin.css";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_staticResourcesDoNotHaveNoCacheHeaders() {
        ResponseEntity<byte[]> imgResponse = restTemplate.getForEntity(IMG_URL, byte[].class);
        ResponseEntity<byte[]> cssResponse = restTemplate.getForEntity(CSS_URL, byte[].class);

        String imgHeaders = imgResponse.getHeaders().getCacheControl();
        String cssHeaders = cssResponse.getHeaders().getCacheControl();

        assertThat(imgHeaders).doesNotContain("no-cache");
        assertThat(cssHeaders).doesNotContain("no-cache");
    }

}

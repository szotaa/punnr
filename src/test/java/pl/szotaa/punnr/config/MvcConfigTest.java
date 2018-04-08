package pl.szotaa.punnr.config;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.szotaa.IntegrationTest;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, SecurityConfig.class})
@MockBean(classes = {PasswordEncoder.class, DataSource.class})
public class MvcConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String BOOTSTRAP_URL = "/webjars/bootstrap/4.0.0-2/js/bootstrap.min.js";
    private final static String JQUERY_URL = "/webjars/jquery/3.3.1-1/jquery.min.js";
    private final static String POPPERJS_URL = "/webjars/popper.js/1.14.1/dist/umd/popper.min.js";

    @Test
    public void requestHomepage_homepageGetsServed() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void requestLoginPage_loginPageGetsServed() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void requestBootstrapResource_resourceGetsServed() throws Exception {
        mockMvc.perform(get(BOOTSTRAP_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/javascript"));
    }

    @Test
    public void requestJqueryResource_resourceGetsServed() throws Exception {
        mockMvc.perform(get(JQUERY_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/javascript"));
    }

    @Test
    public void requestPopperJs_resourceGetsServed() throws Exception {
        mockMvc.perform(get(POPPERJS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/javascript"));
    }
}
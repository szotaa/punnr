package pl.szotaa.punnr.config;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szotaa.IntegrationTest;

@WebMvcTest
@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, SecurityConfig.class})
public class MvcConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String BOOTSTRAP_PATH = "/webjars/bootstrap/4.0.0-2/js/bootstrap.min.js";
    private final static String JQUERY_PATH = "/webjars/jquery/3.3.1/dist/jquery.min.js";
    private final static String POPPERJS_PATH = "/webjars/popper.js/1.14.1/dist/umd/popper.min.js";

    @Test
    public void requestBootstrapResource_resourceGetsServed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BOOTSTRAP_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/javascript"));
    }

    @Test
    public void requestJqueryResource_resourceGetsServed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(JQUERY_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/javascript"));
    }

    @Test
    public void requestPopperJs_resourceGetsServed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(POPPERJS_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/javascript"));
    }
}
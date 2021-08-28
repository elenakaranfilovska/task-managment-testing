package com.ios.backend;

//com/ios/backend/services/MailService.java
//com/ios/backend/MailServiceMutationTest.java
import com.ios.backend.controllers.UserController;
import com.ios.backend.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class)})
@AutoConfigureMockMvc()
public class MockMVCTestsWithSecurity {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();

    }


    @Test
    public void getAllUsersShouldReturn401Status() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/getAllUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAllUserWithTokenShouldReturn200Status() throws Exception {
        String accessToken = obtainAccessToken("ece", "test123");
        mvc.perform(
                MockMvcRequestBuilders
                        .get("/getAllUsers")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "fooClientIdPassword");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("fooClientIdPassword", "secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}

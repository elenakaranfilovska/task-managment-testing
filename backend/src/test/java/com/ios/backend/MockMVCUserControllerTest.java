package com.ios.backend;

import com.ios.backend.controllers.UserController;
import com.ios.backend.entities.User;
import com.ios.backend.resources.UserResource;
import com.ios.backend.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class)},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(secure = false)
public class MockMVCUserControllerTest {


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
    public void getAllEmployeesAPIStatusShouldBe200() throws Exception {
        List<UserResource> userList = new ArrayList<>();
        userList.add(new UserResource(new User("name", "username", "email", "password")));
        when(userService.getAllUser()).thenReturn(userList);

        mvc.perform(MockMvcRequestBuilders
                .get("/getAllUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void getAllEmployeesAPIResponseShouldContainsUserlist() throws Exception {
        List<UserResource> userList = new ArrayList<>();
        userList.add(new UserResource(new User("name", "username", "email", "password")));
        when(userService.getAllUser()).thenReturn(userList);

        mvc.perform(MockMvcRequestBuilders
                .get("/getAllUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userList[*].id").isNotEmpty());
    }

    @Test
    public void getUserByIdStatusShouldBe200() throws Exception {
        User user = new User("name", "username", "email", "password");
        user.setId(100L);
        when(userService.getUser(100L)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .get("/getUser/100")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByIdResponse() throws Exception {
        User user = new User("name", "username", "email", "password");
        user.setId(100L);
        when(userService.getUser(100L)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .get("/getUser/100")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").exists());

    }

}

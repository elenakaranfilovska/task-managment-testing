package com.ios.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ios.backend.controllers.ProgramController;
import com.ios.backend.controllers.UserController;
import com.ios.backend.dto.NewProgramDTO;
import com.ios.backend.services.ProgramService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProgramController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class)},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(secure = false)
public class MockMVCProgramControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ProgramService programService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();

    }


    @Test
    public void shouldCreateNewProgram() throws Exception {
        NewProgramDTO programDTO = new NewProgramDTO();
        programDTO.setDescription("This is new program");
        programDTO.setName("Test");
        when(programService.createProgram(programDTO, 1)).thenReturn(1L);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/createProgram/1")
                .content(asJsonString(programDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"true");
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

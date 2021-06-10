package com.ezfarm.ezfarmback.common;

import com.ezfarm.ezfarmback.security.filter.TokenAuthenticationFilter;
import com.ezfarm.ezfarmback.security.local.CustomUserDetailsService;
import com.ezfarm.ezfarmback.security.local.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class ControllerTest {




    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    CorsFilter corsFilter;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {

        //objectMapper = new ObjectMapper();
        /*mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();*/
    }
}

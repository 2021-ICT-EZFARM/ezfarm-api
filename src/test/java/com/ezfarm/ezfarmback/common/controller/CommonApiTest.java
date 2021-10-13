package com.ezfarm.ezfarmback.common.controller;

import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ComponentScan(basePackages = {"com.ezfarm.ezfarmback.security",
    "com.ezfarm.ezfarmback.config.security"})
public class CommonApiTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected ModelMapper modelMapper;

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(springSecurity())
            .build();
    }
}

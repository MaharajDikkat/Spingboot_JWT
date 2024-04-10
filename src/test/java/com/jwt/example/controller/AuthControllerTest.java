package com.jwt.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.example.domain.User;
import com.jwt.example.dto.requests.JwtRequest;
import com.jwt.example.security.JwtHelper;
import com.jwt.example.security.TokenBlacklist;
import com.jwt.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private TokenBlacklist tokenBlacklist;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link AuthController#exceptionHandler()}
     */
    @Test
    void testExceptionHandler() {

        assertEquals("Credentials Invalid !!", authController.exceptionHandler());
    }

    /**
     * Method under test: {@link AuthController#login(JwtRequest)}
     */
    @Test
    void testLogin() throws Exception {
        when(jwtHelper.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(userDetailsService.loadUserByUsername(Mockito.<String>any())).thenReturn(new User());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail("har12gautam@gmail.com");
        jwtRequest.setPassword("Akatsuki");
        String content = (new ObjectMapper()).writeValueAsString(jwtRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"jwtToken\":\"ABC123\",\"username\":null}"));
    }

    /**
     * Method under test: {@link AuthController#login(JwtRequest)}
     */
    @Test
    void testLogin2() throws Exception {
        when(jwtHelper.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(userDetailsService.loadUserByUsername(Mockito.<String>any())).thenReturn(new User());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new BadCredentialsException("Msg"));

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail("har12gautam@test.com");
        jwtRequest.setPassword("onepice");
        String content = (new ObjectMapper()).writeValueAsString(jwtRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Credentials Invalid !!"));
    }
}


package com.jakubcieslik.linkscopeserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.dto.RegisterReqDTO;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerRegisterTests {

  private static final String ENDPOINT_PATH = "/auth";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testIfSomeoneIsAlreadyLoggedIn() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "test@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(reqData))
        .cookie(new Cookie("refreshToken", "test"));

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Someone is already logged in."));
  }

  @Test
  void testIfNoBodyIsProvided() throws Exception {
    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json");

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Constants.BAD_REQUEST));
  }

  @Test
  void testIfEmailIsInvalid() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(reqData));

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(422))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email cannot be empty."));
  }

  @Test
  public void testIfUserWithGivenEmailAlreadyExists() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "test@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(reqData));

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with this email already exists."));
  }

  @Test
  public void testIfUserWithGivenAliasAlreadyExists() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "newtest@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(reqData));

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with this alias already exists."));
  }

  @Test
  public void testIfUserHasBeenSuccessfullyRegistered() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "newtest@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "newtest",
        true
    );

    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(reqData));

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(201))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Registration successful. Now you can log in."));
  }
}

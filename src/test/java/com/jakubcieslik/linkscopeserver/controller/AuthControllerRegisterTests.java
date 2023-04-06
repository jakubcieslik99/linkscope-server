package com.jakubcieslik.linkscopeserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.dto.RegisterReqDTO;
import com.jakubcieslik.linkscopeserver.model.User;
import com.jakubcieslik.linkscopeserver.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerRegisterTests {

  private static final String ENDPOINT_PATH = "/auth";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void someoneIsAlreadyLoggedIn() throws Exception {
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
  void noBodyIsProvided() throws Exception {
    final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(ENDPOINT_PATH + "/register")
        .contentType("application/json");

    mockMvc.perform(builder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Constants.BAD_REQUEST));
  }

  @Test
  void emailIsInvalid() throws Exception {
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
  public void userWithGivenEmailAlreadyExists() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "test@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    Mockito.when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.of(new User()));

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
  public void userWithGivenAliasAlreadyExists() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "newtest@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "test",
        true
    );

    Mockito.when(userRepository.findByAlias(Mockito.any())).thenReturn(Optional.of(new User()));

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
  public void userHasBeenSuccessfullyRegistered() throws Exception {
    RegisterReqDTO reqData = new RegisterReqDTO(
        "newtest@gmail.com",
        "test123!".toCharArray(),
        "test123!".toCharArray(),
        "newtest",
        true
    );

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(null);

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

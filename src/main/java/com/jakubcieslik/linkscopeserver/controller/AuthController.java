package com.jakubcieslik.linkscopeserver.controller;

import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.dto.*;
import com.jakubcieslik.linkscopeserver.error.AppError;
import com.jakubcieslik.linkscopeserver.error.ErrorResDTO;
import com.jakubcieslik.linkscopeserver.filter.JWTProvider;
import com.jakubcieslik.linkscopeserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final JWTProvider jwtProvider;

  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<MessageResDTO> register(
      @RequestBody RegisterReqDTO registerReqDTO,
      @CookieValue(value = "refreshToken", defaultValue = "") String refreshTokenCookie
  ) {
    if (!refreshTokenCookie.isEmpty()) throw new AppError("Someone is already logged in.", HttpStatus.CONFLICT);

    String message = authService.register(registerReqDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(MessageResDTO.builder().message(message).build());
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<Object> login(
      @RequestBody LoginReqDTO loginReqDTO,
      @CookieValue(value = "refreshToken", defaultValue = "") String refreshTokenCookie
  ) {
    if (!refreshTokenCookie.isEmpty()) {
      SecurityContextHolder.clearContext();

      authService.deleteRefreshToken(refreshTokenCookie);

      return ResponseEntity.status(HttpStatus.CONFLICT)
          .headers(jwtProvider.setRefreshTokenCookie("", -1L))
          .body(new ErrorResDTO("Someone is already logged in. Log out or try again.", HttpStatus.CONFLICT.value()));
    }

    UserInfo userInfo = authService.login(loginReqDTO);
    String accessToken = jwtProvider.generateAccessToken(userInfo.getLogin());
    String refreshToken = jwtProvider.generateRefreshToken(userInfo.getLogin());

    authService.saveRefreshToken(userInfo.getLogin(), refreshToken);

    return ResponseEntity.status(HttpStatus.OK)
        .headers(jwtProvider.setRefreshTokenCookie(refreshToken, Duration.ofDays(90).getSeconds()))
        .body(LoginResDTO.builder().userInfo(userInfo).accessToken(accessToken).build());
  }

  @GetMapping("/refreshToken")
  @ResponseBody
  public ResponseEntity<RefreshResDTO> refresh(@CookieValue(value = "refreshToken", defaultValue = "") String refreshTokenCookie) {
    if (refreshTokenCookie.isEmpty()) throw new AppError(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

    String newAccessToken = jwtProvider.validateRefreshToken(refreshTokenCookie);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(RefreshResDTO.builder().accessToken(newAccessToken).build());
  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", defaultValue = "") String refreshTokenCookie) {
    SecurityContextHolder.clearContext();

    if (refreshTokenCookie.isEmpty()) return ResponseEntity.noContent().build();

    authService.deleteRefreshToken(refreshTokenCookie);

    return ResponseEntity.noContent()
        .headers(jwtProvider.setRefreshTokenCookie("", -1L)).build();
  }
}

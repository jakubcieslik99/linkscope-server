package com.jakubcieslik.linkscopeserver.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jakubcieslik.linkscopeserver.common.AuthEntryPoint;
import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.dto.UserInfo;
import com.jakubcieslik.linkscopeserver.service.AuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTProvider {

  private final AuthService authService;
  private final AuthEntryPoint authEntryPoint;
  @Value("${ENV}")
  private String env;
  @Value("${JWT_ACCESS_TOKEN_SECRET}")
  private String accessTokenSecret;
  @Value("${JWT_REFRESH_TOKEN_SECRET}")
  private String refreshTokenSecret;

  public JWTProvider(AuthService authService, AuthEntryPoint authEntryPoint) {
    this.authService = authService;
    this.authEntryPoint = authEntryPoint;
  }

  @PostConstruct
  protected void init() {
    accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenSecret.getBytes());
  }

  public String generateAccessToken(String login) {
    try {
      Date currentDate = new Date();
      Date expirationDate = new Date(currentDate.getTime() + 1000 * 60 * 20); // 20 minutes
      Algorithm algorithm = Algorithm.HMAC256(accessTokenSecret);

      return JWT.create().withIssuer(login).withIssuedAt(currentDate).withExpiresAt(expirationDate).sign(algorithm);
    } catch (RuntimeException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
      throw e;
    }
  }

  public String generateRefreshToken(String login) {
    try {
      Date currentDate = new Date();
      Date expirationDate = new Date(currentDate.getTime() + 1000L * 60 * 60 * 24 * 90); // 90 days
      Algorithm algorithm = Algorithm.HMAC256(refreshTokenSecret);

      return JWT.create().withIssuer(login).withIssuedAt(currentDate).withExpiresAt(expirationDate).sign(algorithm);
    } catch (RuntimeException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
      throw e;
    }
  }

  public HttpHeaders setRefreshTokenCookie(String refreshToken, Long maxAge) {
    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)
        .sameSite("None")
        .secure(!Objects.equals(env, "test"))
        .maxAge(maxAge)
        .path("/")
        .build();
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    return headers;
  }


  public Authentication validateAccessToken(String accessToken) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(accessTokenSecret);
      JWTVerifier verifier = JWT.require(algorithm).build();

      DecodedJWT decoded = verifier.verify(accessToken);

      UserInfo userInfo = authService.findUser(decoded.getIssuer());

      return new UsernamePasswordAuthenticationToken(userInfo, null, Collections.emptyList());
    } catch (JWTVerificationException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.UNAUTHORIZED, "Unauthorized access. Session has expired.");
      throw e;
    } catch (RuntimeException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
      throw e;
    }
  }

  public String validateRefreshToken(String refreshToken) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(refreshTokenSecret);
      JWTVerifier verifier = JWT.require(algorithm).build();

      DecodedJWT decoded = verifier.verify(refreshToken);

      UserInfo userInfo = authService.findUser(decoded.getIssuer());

      return generateAccessToken(userInfo.getLogin());
    } catch (JWTVerificationException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.UNAUTHORIZED, Constants.UNAUTHORIZED);
      throw e;
    } catch (RuntimeException e) {
      authEntryPoint.setHttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
      throw e;
    }
  }
}

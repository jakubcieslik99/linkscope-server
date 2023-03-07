package com.jakubcieslik.linkscopeserver.controller;

import com.jakubcieslik.linkscopeserver.dto.*;
import com.jakubcieslik.linkscopeserver.filter.JWTProvider;
import com.jakubcieslik.linkscopeserver.model.Link;
import com.jakubcieslik.linkscopeserver.model.User;
import com.jakubcieslik.linkscopeserver.service.AccountService;
import com.jakubcieslik.linkscopeserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

  private final AuthService authService;
  private final AccountService accountService;
  private final JWTProvider jwtProvider;

  @GetMapping("/getAccount")
  @ResponseBody
  public ResponseEntity<AccountResDTO> getAccount(@AuthenticationPrincipal UserInfo userInfo) {
    User account = accountService.getAccount(userInfo.getLogin());

    return ResponseEntity.status(HttpStatus.OK)
        .body(AccountResDTO.builder()
            .id(account.getId())
            .alias(account.getAlias())
            .title(account.getTitle())
            .bio(account.getBio())
            .build());
  }

  @PutMapping("/updateCredentials")
  @ResponseBody
  public ResponseEntity<CredentialsResDTO> updateCredentials(
      @AuthenticationPrincipal UserInfo userInfo,
      @CookieValue(value = "refreshToken", defaultValue = "") String refreshTokenCookie,
      @RequestBody CredentialsReqDTO credentialsReqDTO
  ) {
    if (!refreshTokenCookie.isEmpty()) authService.deleteRefreshToken(refreshTokenCookie);

    UserInfo newUserInfo = accountService.updateCredentials(userInfo.getLogin(), credentialsReqDTO);
    String accessToken = jwtProvider.generateAccessToken(newUserInfo.getLogin());
    String refreshToken = jwtProvider.generateRefreshToken(newUserInfo.getLogin());

    authService.saveRefreshToken(newUserInfo.getLogin(), refreshToken);

    return ResponseEntity.status(HttpStatus.OK)
        .headers(jwtProvider.setRefreshTokenCookie(refreshToken, Duration.ofDays(90).getSeconds()))
        .body(CredentialsResDTO.builder().userInfo(newUserInfo).accessToken(accessToken).build());
  }

  @DeleteMapping("/deleteAccount")
  @ResponseBody
  public ResponseEntity<MessageResDTO> deleteAccount(@AuthenticationPrincipal UserInfo userInfo) {
    String message = accountService.deleteAccount(userInfo.getLogin());

    SecurityContextHolder.clearContext();

    return ResponseEntity.status(HttpStatus.OK)
        .headers(jwtProvider.setRefreshTokenCookie("", -1L))
        .body(MessageResDTO.builder().message(message).build());
  }

  @PutMapping("/updateDetails")
  @ResponseBody
  public ResponseEntity<DetailsResDTO> updateDetails(
      @AuthenticationPrincipal UserInfo userInfo,
      @RequestBody DetailsReqDTO detailsReqDTO
  ) {
    DetailsInfo detailsInfo = accountService.updateDetails(userInfo.getLogin(), detailsReqDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .body(DetailsResDTO.builder().detailsInfo(detailsInfo).build());
  }

  @GetMapping("/getLinks")
  @ResponseBody
  public ResponseEntity<LinksResDTO> getLinks(@AuthenticationPrincipal UserInfo userInfo) {
    List<Link> links = accountService.getLinks(userInfo.getLogin());

    return ResponseEntity.status(HttpStatus.OK)
        .body(LinksResDTO.builder().links(links).build());
  }

  @PostMapping("/addLink")
  @ResponseBody
  public ResponseEntity<LinkResDTO> addLink(
      @AuthenticationPrincipal UserInfo userInfo,
      @RequestBody LinkReqDTO linkReqDTO
  ) {
    Link link = accountService.addLink(userInfo.getLogin(), linkReqDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(LinkResDTO.builder().message("Link has been created.").link(link).build());
  }

  @PutMapping("/updateLink/{id}")
  @ResponseBody
  public ResponseEntity<LinkResDTO> updateLink(
      @AuthenticationPrincipal UserInfo userInfo,
      @PathVariable Long id,
      @RequestBody LinkReqDTO linkReqDTO
  ) {
    Link link = accountService.updateLink(userInfo.getLogin(), id, linkReqDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .body(LinkResDTO.builder().message("Link has been updated.").link(link).build());
  }

  @DeleteMapping("/deleteLink/{id}")
  @ResponseBody
  public ResponseEntity<MessageResDTO> deleteLink(
      @AuthenticationPrincipal UserInfo userInfo,
      @PathVariable Long id
  ) {
    String message = accountService.deleteLink(userInfo.getLogin(), id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(MessageResDTO.builder().message(message).build());
  }
}

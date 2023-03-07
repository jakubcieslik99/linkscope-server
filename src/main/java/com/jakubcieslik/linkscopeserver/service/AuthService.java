package com.jakubcieslik.linkscopeserver.service;

import com.jakubcieslik.linkscopeserver.common.ObjectsValidator;
import com.jakubcieslik.linkscopeserver.dto.LoginReqDTO;
import com.jakubcieslik.linkscopeserver.dto.RegisterReqDTO;
import com.jakubcieslik.linkscopeserver.dto.UserInfo;
import com.jakubcieslik.linkscopeserver.error.AppError;
import com.jakubcieslik.linkscopeserver.model.Token;
import com.jakubcieslik.linkscopeserver.model.User;
import com.jakubcieslik.linkscopeserver.repository.TokenRepository;
import com.jakubcieslik.linkscopeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final ObjectsValidator objectsValidator;
  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  public UserInfo findUser(String login) {
    Optional<User> user = userRepository.findByLogin(login);
    if (user.isEmpty())
      throw new AppError("User with this email doesn't exist.", HttpStatus.NOT_FOUND);

    return new UserInfo(user.get().getId(), user.get().getLogin());
  }

  private User findUserModel(String login) {
    Optional<User> user = userRepository.findByLogin(login);
    if (user.isEmpty())
      throw new AppError("User doesn't exist.", HttpStatus.NOT_FOUND);

    return user.get();
  }

  public void saveRefreshToken(String login, String refreshToken) {
    tokenRepository.deleteByExpirationBefore(LocalDateTime.now());

    User user = findUserModel(login);
    Token token = Token.builder()
        .user(user)
        .token(refreshToken)
        .expiration(LocalDateTime.now().plusDays(90))
        .build();

    tokenRepository.save(token);
  }

  public void deleteRefreshToken(String refreshToken) {
    tokenRepository.deleteByToken(refreshToken);
  }

  public String register(RegisterReqDTO registerReqDTO) {
    objectsValidator.validate(registerReqDTO);

    Optional<User> conflictUserEmail = userRepository.findByLogin(registerReqDTO.getLogin());
    if (conflictUserEmail.isPresent())
      throw new AppError("User with this email already exists.", HttpStatus.CONFLICT);

    Optional<User> conflictUserAlias = userRepository.findByAlias(registerReqDTO.getAlias());
    if (conflictUserAlias.isPresent())
      throw new AppError("User with this alias already exists.", HttpStatus.CONFLICT);

    User user = User.builder()
        .login(registerReqDTO.getLogin())
        .password(passwordEncoder.encode(CharBuffer.wrap(registerReqDTO.getPassword())))
        .alias(registerReqDTO.getAlias())
        .title(registerReqDTO.getAlias().substring(0, 1).toUpperCase() + registerReqDTO.getAlias().substring(1))
        .bio("")
        .build();

    userRepository.save(user);

    return "Registration successful. Now you can log in.";
  }

  public UserInfo login(LoginReqDTO loginReqDTO) {
    objectsValidator.validate(loginReqDTO);

    User user = findUserModel(loginReqDTO.getLogin());

    if (!passwordEncoder.matches(CharBuffer.wrap(loginReqDTO.getPassword()), user.getPassword()))
      throw new AppError("Invalid email or password.", HttpStatus.UNAUTHORIZED);

    return new UserInfo(user.getId(), user.getLogin());
  }
}

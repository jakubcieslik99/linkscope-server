package com.jakubcieslik.linkscopeserver.service;

import com.jakubcieslik.linkscopeserver.common.ObjectsValidator;
import com.jakubcieslik.linkscopeserver.dto.*;
import com.jakubcieslik.linkscopeserver.error.AppError;
import com.jakubcieslik.linkscopeserver.model.Link;
import com.jakubcieslik.linkscopeserver.model.User;
import com.jakubcieslik.linkscopeserver.repository.LinkRepository;
import com.jakubcieslik.linkscopeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

  private final ObjectsValidator objectsValidator;
  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;
  private final LinkRepository linkRepository;

  public User getAccount(String login) {
    Optional<User> account = userRepository.findByLogin(login);
    if (account.isEmpty())
      throw new AppError("User doesn't exist.", HttpStatus.NOT_FOUND);

    return account.get();
  }

  public Link getLink(Long id, String login) {
    Optional<Link> link = linkRepository.getLinkByIdAndUserLogin(id, login);
    if (link.isEmpty())
      throw new AppError("Link doesn't exist.", HttpStatus.NOT_FOUND);
    
    return link.get();
  }

  public UserInfo updateCredentials(String login, CredentialsReqDTO credentialsReqDTO) {
    objectsValidator.validate(credentialsReqDTO);

    User account = getAccount(login);

    if (!passwordEncoder.matches(CharBuffer.wrap(credentialsReqDTO.getPassword()), account.getPassword()))
      throw new AppError("Invalid password.", HttpStatus.UNAUTHORIZED);

    if (credentialsReqDTO.getNewpassword() != null && credentialsReqDTO.getNewpassword().length > 0) {
      if (credentialsReqDTO.getNewpassword().length < 8 || credentialsReqDTO.getNewpassword().length > 60)
        throw new AppError("New password must be between 8 and 60 characters long.", HttpStatus.BAD_REQUEST);

      account.setPassword(passwordEncoder.encode(CharBuffer.wrap(credentialsReqDTO.getNewpassword())));
    }

    if (!Objects.equals(credentialsReqDTO.getLogin(), account.getLogin())) {
      Optional<User> conflictUserEmail = userRepository.findByLogin(credentialsReqDTO.getLogin());
      if (conflictUserEmail.isPresent())
        throw new AppError("User with this email already exists.", HttpStatus.CONFLICT);

      account.setLogin(credentialsReqDTO.getLogin());
    }

    userRepository.save(account);

    return new UserInfo(account.getId(), account.getLogin());
  }

  public String deleteAccount(String login) {
    User account = getAccount(login);

    userRepository.deleteById(account.getId());

    return "Account deleted successfully.";
  }

  public DetailsInfo updateDetails(String login, DetailsReqDTO detailsReqDTO) {
    objectsValidator.validate(detailsReqDTO);

    User account = getAccount(login);

    account.setAlias(detailsReqDTO.getAlias());
    account.setTitle(detailsReqDTO.getTitle());
    account.setBio(detailsReqDTO.getBio());

    userRepository.save(account);

    return new DetailsInfo(account.getAlias(), account.getTitle(), account.getBio());
  }

  public List<Link> getLinks(String login) {
    User account = getAccount(login);

    return account.getLinks();
  }

  public Link addLink(String login, LinkReqDTO linkReqDTO) {
    objectsValidator.validate(linkReqDTO);

    User account = getAccount(login);

    Link link = Link.builder()
        .user(account)
        .title(linkReqDTO.getTitle())
        .link(linkReqDTO.getLink())
        .build();

    linkRepository.save(link);

    return link;
  }

  public Link updateLink(String login, Long linkId, LinkReqDTO linkReqDTO) {
    objectsValidator.validate(linkReqDTO);

    Link link = getLink(linkId, login);
    link.setTitle(linkReqDTO.getTitle());
    link.setLink(linkReqDTO.getLink());

    linkRepository.save(link);

    return link;
  }

  public String deleteLink(String login, Long linkId) {
    Link link = getLink(linkId, login);

    linkRepository.deleteById(link.getId());

    return "Link deleted successfully.";
  }
}

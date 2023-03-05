package com.jakubcieslik.linkscopeserver.common;

import com.jakubcieslik.linkscopeserver.dto.RegisterReqDTO;
import com.jakubcieslik.linkscopeserver.error.AppError;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ObjectsValidator {

  private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
  private final Validator validator = validatorFactory.getValidator();

  public void validate(Object object) {
    var violations = validator.validate(object);
    if (!violations.isEmpty())
      throw new AppError(violations.iterator().next().getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

    //custom filter for RegisterReqDTO
    if (object instanceof RegisterReqDTO registerReqDTO) {
      if (!Arrays.equals(registerReqDTO.getPassword(), registerReqDTO.getRepassword()))
        throw new AppError("Passwords don't match.", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}

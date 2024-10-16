package com.backend.ecommercebackend.enums;

import com.backend.ecommercebackend.model.consts.ExceptionMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Exceptions {

  INVALID_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, ExceptionMessage.TOKEN_IS_INVALID_MSG),
  PASSWORD_MISMATCH_EXCEPTION(HttpStatus.BAD_REQUEST,ExceptionMessage.PASSWORD_MISMATCH_MSG),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND,ExceptionMessage.USER_NOT_FOUND_MSG),
  USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,ExceptionMessage.USER_ALREADY_EXIST_MSG),
  PASSWORD_SAME_AS_OLD_EXCEPTION(HttpStatus.BAD_REQUEST,ExceptionMessage.PASSWORD_SAME_AS_OLD_MSG),
  NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,ExceptionMessage.NOT_FOUND_EXCEPTION_MSG),
  TERMS_ACCEPTANCE_EXCEPTION(HttpStatus.BAD_REQUEST,ExceptionMessage.TERMS_ACCEPTANCE_EXCEPTION_MSG),
  IMAGE_STORAGE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessage.IMAGE_STORAGE_EXCEPTION_MSG),
  DIRECTORY_CREATION_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,ExceptionMessage.CREATE_DIRECTORY_ERROR_MSG),
  INVALID_FORMAT_EXCEPTION(HttpStatus.BAD_REQUEST,ExceptionMessage.INVALID_FORMAT_EXCEPTION_MSG);

  private final HttpStatus httpStatus;
  private final String message;
}

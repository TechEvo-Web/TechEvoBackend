package com.backend.ecommercebackend.exception;


import com.backend.ecommercebackend.enums.Exceptions;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException{
  private final Exceptions exception;
  private final String message;

  public ApplicationException(Exceptions exception) {
    super(exception.getMessage());
    this.exception = exception;
    this.message = exception.getMessage();
  }
  public ApplicationException(Exceptions exception, String customMessage) {
    super(customMessage);
    this.exception = exception;
    this.message = customMessage;
  }

  public HttpStatus getHttpStatus() {
    return exception.getHttpStatus();
  }

  @Override
  public String getMessage() {
    return message != null ? message : exception.getMessage();}
}


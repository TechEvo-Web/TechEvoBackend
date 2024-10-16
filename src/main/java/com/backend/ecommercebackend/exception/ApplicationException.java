package com.backend.ecommercebackend.exception;


import com.backend.ecommercebackend.enums.Exceptions;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException{
  private final Exceptions exception;

  public ApplicationException(Exceptions exception) {
    super(exception.getMessage());
    this.exception = exception;
  }

  public HttpStatus getHttpStatus() {
    return exception.getHttpStatus();
  }
}

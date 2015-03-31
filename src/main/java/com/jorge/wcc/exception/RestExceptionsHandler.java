package com.jorge.wcc.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jorge.wcc.domain.ResponseException;

@ControllerAdvice
public class RestExceptionsHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(UsernameNotFoundException.class)
  protected ResponseEntity<Object> handleDuplicateUsser(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    responseMessage.setMessage(ex.getMessage());
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  protected ResponseEntity<Object> handleUserAlreadyExistException(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    responseMessage.setMessage(ex.getMessage());
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(EmptyFieldsException.class)
  protected ResponseEntity<Object> handleEmptyFieldsException(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    responseMessage.setMessage(ex.getMessage());
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(PostalCodeNotFoundException.class)
  protected ResponseEntity<Object> handlePostalCodeNotFoundException(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    responseMessage.setMessage(ex.getMessage());
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  protected ResponseEntity<Object> handleInternalAuthenticationServiceException(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    responseMessage.setMessage(ex.getMessage());
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<Object> handleNullPonterException(RuntimeException ex, WebRequest request) {
    ResponseException responseMessage = new ResponseException();
    ex.printStackTrace();
    responseMessage.setMessage("An error occurred while processing your request");
    return handleExceptionInternal(ex, responseMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}

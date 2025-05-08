package me.goldm0ng.duriseo_be.common.handler;

import me.goldm0ng.duriseo_be.common.exception.DuriseoException;
import me.goldm0ng.duriseo_be.common.response.APIErrorResponse;
import me.goldm0ng.duriseo_be.enums.message.FailMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuriseoException.class)
    public ResponseEntity<APIErrorResponse> handleDuriseoException(final DuriseoException exception) {

        final FailMessage failMessage = exception.getFailMessage();

        return APIErrorResponse.of(failMessage.getHttpStatus(), failMessage.getCode(), failMessage.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {

        final String customMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        final FailMessage failMessage = FailMessage.BAD_REQUEST_REQUEST_BODY_VALID;

        return APIErrorResponse.of(failMessage.getHttpStatus(), failMessage.getCode(), customMessage);
    }

    @ExceptionHandler
    public ResponseEntity<APIErrorResponse> handleMissingParamException(final MissingServletRequestParameterException exception) {

        final String customMessage = "누락된 파라미터 : " + exception.getParameterName();
        final FailMessage failMessage = FailMessage.BAD_REQUEST_MISSING_PARAM;

        return APIErrorResponse.of(failMessage.getHttpStatus(), failMessage.getCode(), customMessage);
    }


}

package com.genesis.contacts.exceptions.interceptors;


import com.genesis.contacts.domain.enums.ERROR_CODE;
import com.genesis.contacts.exceptions.ContactsException;
import com.genesis.contacts.exceptions.TechnicalException;
import com.genesis.contacts.exceptions.ValidationException;
import com.genesis.contacts.utils.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
@RestController
@Slf4j
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        var error = new JsonResponse("HttpMessageNotReadable", ex.getMessage());
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        var error = new JsonResponse(ex.getErrorCode(), ex.getMessage());
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        var error = new JsonResponse("MissingRequestParameter", ex.getMessage());
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        JsonResponse error = null;
        var objectError = ex.getBindingResult().getAllErrors().get(0);
        if (objectError instanceof FieldError) {
            var fieldError = (FieldError) objectError;
            error = new JsonResponse(fieldError.getCode(),
                    fieldError.getField() + " " + fieldError.getDefaultMessage());
        } else {
            error = new JsonResponse(objectError.getCode(), objectError.getDefaultMessage());
        }

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        JsonResponse error = null;
        var objectError = ex.getBindingResult().getAllErrors().get(0);
        if (objectError instanceof FieldError) {
            var fieldError = (FieldError) objectError;
            error = new JsonResponse(fieldError.getCode(),
                    fieldError.getField() + " " + fieldError.getDefaultMessage());
        } else {
            error = new JsonResponse(objectError.getCode(), objectError.getDefaultMessage());
        }

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ TechnicalException.class })
    public JsonResponse handleTechnicalException(TechnicalException e, HttpServletRequest request) {

        var jsonResponse = new JsonResponse();
        jsonResponse.setStatus(JsonResponse.STATUS.FAILED);

        jsonResponse.setErrorCode(
                StringUtils.isNotBlank(e.getErrorCode()) ? e.getErrorCode() : ERROR_CODE.TECHNICAL_EXCEPTION.name());

        setTranslatedMessage(jsonResponse, request.getHeader("Accept-Language"));

        return jsonResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ValidationException.class })
    public JsonResponse handleValidationExceptionException(ContactsException e, HttpServletRequest request) {

        var jsonResponse = new JsonResponse();
        jsonResponse.setStatus(JsonResponse.STATUS.FAILED);


            jsonResponse.setErrorCode(e.getErrorCode());


        var stackTrace = e.getStackTrace();
        String origin = null;
        if (stackTrace != null && stackTrace.length > 0) {
            origin = stackTrace[0].getClassName();
            origin += "." + stackTrace[0].getMethodName();
        }

        setTranslatedMessage(jsonResponse, request.getHeader("Accept-Language"));
        logException(e.getClass().getName(), origin, e.getErrorCode(), jsonResponse.getErrorMsg(), e.getMessage());

        return jsonResponse;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })
    public JsonResponse handleException(Exception e, HttpServletRequest request) {

        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(JsonResponse.STATUS.FAILED);
        jsonResponse.setErrorCode(ERROR_CODE.BACKEND_TECHNICAL_EXCEPTION.name());
        var stackTrace = e.getStackTrace();
        String origin = null;
        if (stackTrace != null && stackTrace.length > 0) {
            origin = stackTrace[0].getClassName();
            origin += "." + stackTrace[0].getMethodName();
        }

        setTranslatedMessage(jsonResponse, request.getHeader("Accept-Language"));
        logException(e.getClass().getName(), origin, e.getMessage(), jsonResponse.getErrorMsg(), e.getMessage());
        log.error("Technical ERROR ", e);
        return jsonResponse;
    }

    // ParseException
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler({ ParseException.class })
    // public JsonResponse handleParseException(Exception e, HttpServletRequest
    // request) {
    //
    // var jsonResponse = new JsonResponse();
    // jsonResponse.setStatus(JsonResponse.STATUS.FAILED);
    // jsonResponse.setErrorCode(ERROR_CODE.WRONG_PARAMS.name());
    // jsonResponse.setErrorMsg("test");
    //
    // var stackTrace = e.getStackTrace();
    // String origin = null;
    // if (stackTrace != null && stackTrace.length > 0) {
    // origin = stackTrace[0].getClassName();
    // origin += "." + stackTrace[0].getMethodName();
    // }
    //
    // setTranslatedMessage(jsonResponse, request.getHeader("Accept-Language"));
    // logException(e.getClass().getName(), origin, e.getMessage(),
    // jsonResponse.getErrorMsg(), e.getMessage());
    //
    // return jsonResponse;
    // }

    private void setTranslatedMessage(JsonResponse jsonResponse, String lang) {
        try {

            Locale locale = StringUtils.isNotBlank(lang) ? new Locale(lang) : new Locale("fr");

            String message = messageSource.getMessage(jsonResponse.getErrorCode(), null, locale);

            jsonResponse.setErrorMsg(message);

        } catch (NoSuchMessageException e2) {
            jsonResponse.setErrorMsg("");
        }
    }

    private static void logException(String exceptionClass, String origin, String errorCode, String restMsg,
                                     String exceptionMsg) {
        log.error("{} {}({}) => REST error message : {}", (StringUtils.isNotBlank(origin) ? origin : ""),
                exceptionClass, errorCode, restMsg);
        if (StringUtils.isNotBlank(exceptionMsg)) {
            log.error("{} {}({}) => Exception error message : {} ", (StringUtils.isNotBlank(origin) ? origin : ""),
                    exceptionClass, errorCode, exceptionMsg);
        }
    }

}

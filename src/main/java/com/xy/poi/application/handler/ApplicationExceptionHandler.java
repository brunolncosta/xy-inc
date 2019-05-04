package com.xy.poi.application.handler;

import com.xy.poi.infrastructure.roxana.BusinessException;
import com.xy.poi.infrastructure.service.MessageService;
import com.xy.poi.presentation.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected MessageService mensagemService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleRuntimeException(final Exception ex) {
        BusinessException businessExAnnotation = AnnotationUtils.findAnnotation(ex.getClass(), BusinessException.class);

        if(businessExAnnotation != null)
            return createExceptionResponse(businessExAnnotation.status(), businessExAnnotation.key());

        log.error(ex.getMessage(), ex);
        return createExceptionResponse(BAD_REQUEST, "system-intern-error");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Response<Object> response = new Response<>();
        List<String> erros = ex.getConstraintViolations().stream()
                                .map(this::extractErroPorViolation)
                                .collect(Collectors.toList());

        response.setErrors(erros);
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    private String extractErroPorViolation(ConstraintViolation constraintViolation) {
        return MessageFormat.format(constraintViolation.getMessage(),
                                    constraintViolation.getPropertyPath());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Response<Object> response = new Response<>();
        response.setErrors(obterListaErros(ex.getBindingResult()));
        return handleExceptionInternal(ex, response, headers, BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> createExceptionResponse(HttpStatus status, String chave) {
        Response<Object> response = new Response<>();
        response.setErrors(Arrays.asList((mensagemService.getMessage(chave))));
        return ResponseEntity.status(status).body(response);
    }

    protected List<String> obterListaErros(BindingResult bindingResult) {
        List<String> erros = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(e -> erros.add(mensagemService.getMessage(e)));
        return erros;
    }

}

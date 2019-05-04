package com.xy.poi.infrastructure.roxana;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

// PARTE TO MEU FRAMEWORK - https://github.com/rooting-company/roxana

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessException {

    String key();

    HttpStatus status() default HttpStatus.BAD_REQUEST;

}

package com.xy.poi.domain.exception;

import com.xy.poi.infrastructure.roxana.BusinessException;

@BusinessException(key = "ponto-invalid-range")
public class InvalidRangeException extends RuntimeException {
}

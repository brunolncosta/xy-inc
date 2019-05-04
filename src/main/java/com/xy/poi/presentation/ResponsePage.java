package com.xy.poi.presentation;

import org.springframework.data.domain.Page;

import java.io.Serializable;

public final class ResponsePage<T> extends Response<Page<T>> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ResponsePage(Page<T> page) {
        super(page);
    }
}

package com.xy.poi.presentation;

import java.io.Serializable;
import java.util.List;

public final class ResponseList<T> extends Response<List<T>> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ResponseList(List<T> list) {
        super(list);
    }
}

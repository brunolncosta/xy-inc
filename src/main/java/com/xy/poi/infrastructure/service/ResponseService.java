package com.xy.poi.infrastructure.service;

import com.xy.poi.presentation.Response;
import com.xy.poi.presentation.ResponseList;
import com.xy.poi.presentation.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class ResponseService {

    public <T> Response<T> create(T data) {
        return new Response<>(data);
    }

    public <T> ResponsePage<T> create(Page<T> page) {
        return new ResponsePage<>(page);
    }

    public <T> ResponseList<T> create(List<T> list) {
        return new ResponseList<>(list);
    }

}

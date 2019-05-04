package com.xy.poi.application.controller;

import com.xy.poi.infrastructure.service.ConverterService;
import com.xy.poi.infrastructure.service.ResponseService;
import com.xy.poi.presentation.Response;
import com.xy.poi.presentation.ResponseList;
import com.xy.poi.presentation.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController {

    @Autowired
    private ConverterService converterService;

    @Autowired
    private ResponseService responseService;

    protected <Z> Z convertToBean(Object dto, Class<Z> clazz) {
        return converterService.convert(dto, clazz);
    }

    protected <Z> Z convertToDto(Object bean, Class<Z> dtoClass) {
        return converterService.convert(bean, dtoClass);
    }

    protected <Z> Response<Z> convertToResponse(Object bean, Class<Z> dtoClass) {
        return responseService.create(convertToDto(bean, dtoClass));
    }

    protected <Z> ResponsePage<Z> convertToResponse(Page<?> page, Class<Z> dtoClass) {
        return responseService.create(page.map(t -> convertToDto(t, dtoClass)));
    }

    protected <Z> ResponseList<Z> convertToResponse(List<?> list, Class<Z> dtoClass) {
        return responseService.create(list.stream()
                                .map(i -> convertToDto(i, dtoClass))
                                .collect(Collectors.toList()));
    }

}
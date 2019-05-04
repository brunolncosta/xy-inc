package com.xy.poi.infrastructure.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class ConverterService {

    @Autowired
    private ModelMapper modelMapper;

    public <T> T convert(Object data, Class<T> type) {
        return modelMapper.map(data, type);
    }

    public <T> List<T> convert(List<?> dataList, Class<T> type) {
        return dataList.stream().map(d -> modelMapper.map(d, type)).collect(Collectors.toList());
    }

    public <T> Page<T> convert(Page<?> dataList, Class<T> type) {
        return dataList.map(d -> modelMapper.map(d, type));
    }

}

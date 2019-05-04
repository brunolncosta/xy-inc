package com.xy.poi.domain.service;

import com.querydsl.core.types.Predicate;
import com.xy.poi.domain.model.PontoInteresse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PontoInteresseService {

    List<PontoInteresse> findNear(Double latitude, Double longitude, Double range);

    Page<PontoInteresse> find(Predicate predicate, Pageable pageable);

    Page<PontoInteresse> findAll(Pageable pageable);

    PontoInteresse add(PontoInteresse pontoInteresse);
}

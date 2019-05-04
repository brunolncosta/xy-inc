package com.xy.poi.application.service;

import com.querydsl.core.types.Predicate;
import com.xy.poi.domain.exception.InvalidRangeException;
import com.xy.poi.domain.model.PontoInteresse;
import com.xy.poi.domain.service.PontoInteresseService;
import com.xy.poi.infrastructure.repository.PontoInteresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PontoInteresseServiceImpl implements PontoInteresseService {

    @Autowired
    private PontoInteresseRepository repository;

    @Override
    @Transactional
    public PontoInteresse add(PontoInteresse pontoInteresse) {
        return repository.save(pontoInteresse);
    }

    @Override
    public Page<PontoInteresse> find(Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @Override
    public Page<PontoInteresse> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<PontoInteresse> findNear(Double latitude, Double longitude, Double range) {
        Point point = createPoint(latitude, longitude);
        Distance maxDistance = getDistanciaMax(range);
        return repository.findByLocalizacaoNear(point, maxDistance);
    }

    private Distance getDistanciaMax(Double range) {
        if (range == null || range < 0) {
            throw new InvalidRangeException();
        }

        Double rangeEmMetros = 0D;
        if (range != 0) {
            rangeEmMetros = range * 100;
        }

        return new Distance(rangeEmMetros, Metrics.KILOMETERS);
    }

    private Point createPoint(Double latitude, Double longitude) {
        return new Point(latitude, longitude);
    }

}

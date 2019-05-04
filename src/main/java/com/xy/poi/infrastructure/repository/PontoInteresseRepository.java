package com.xy.poi.infrastructure.repository;

import com.xy.poi.domain.model.PontoInteresse;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PontoInteresseRepository extends MongoRepository<PontoInteresse, String>,
                                                  QuerydslPredicateExecutor<PontoInteresse> {

    List<PontoInteresse> findByLocalizacaoNear(Point point, Distance distanciaMax);

}

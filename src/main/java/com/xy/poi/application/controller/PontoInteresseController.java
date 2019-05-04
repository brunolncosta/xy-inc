package com.xy.poi.application.controller;

import com.querydsl.core.types.Predicate;
import com.xy.poi.domain.model.PontoInteresse;
import com.xy.poi.domain.service.PontoInteresseService;
import com.xy.poi.presentation.Response;
import com.xy.poi.presentation.ResponseList;
import com.xy.poi.presentation.ResponsePage;
import com.xy.poi.presentation.ponto.PontoInteresseRequest;
import com.xy.poi.presentation.ponto.PontoInteresseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("pontos-de-interesse")
public class PontoInteresseController extends BaseController {

    @Autowired
    private PontoInteresseService service;

    @PostMapping
    @ResponseStatus(CREATED)
    private Response<PontoInteresseResponse> create(@RequestBody PontoInteresseRequest request) {
        PontoInteresse pontoInteresse = convertToBean(request, PontoInteresse.class);
        pontoInteresse = service.add(pontoInteresse);
        return convertToResponse(pontoInteresse, PontoInteresseResponse.class);
    }

    @GetMapping
    private ResponsePage<PontoInteresseResponse> findByQueryDsl(@QuerydslPredicate(root = PontoInteresse.class) Predicate predicate,
                                                      Pageable pageable) {

        Page<PontoInteresse> result = service.find(predicate, pageable);
        return convertToResponse(result, PontoInteresseResponse.class);
    }

    @GetMapping("/all")
    private ResponsePage<PontoInteresseResponse> findAll(Pageable pageable) {
        Page<PontoInteresse> result = service.findAll(pageable);
        return convertToResponse(result, PontoInteresseResponse.class);
    }

    @GetMapping("/near")
    public final ResponseList<PontoInteresseResponse> getNear(@RequestParam("latitude") Double latitude,
                                                              @RequestParam("longitude") Double longitude,
                                                              @RequestParam(name = "range") Double range) {
        List<PontoInteresse> result = service.findNear(latitude, longitude, range);
        return convertToResponse(result, PontoInteresseResponse.class);
    }

    @GetMapping("/create-all")
    public final void createAll() {

        PontoInteresse lanchonete = new PontoInteresse("Lanchonete", 27D, 12D);
        PontoInteresse posto = new PontoInteresse("Posto", 31D, 18D);
        PontoInteresse joalheria = new PontoInteresse("Joalheria", 15D, 12D);
        PontoInteresse pub = new PontoInteresse("Pub", 12D, 8D);
        PontoInteresse supermercado = new PontoInteresse("Supermercado", 23D, 6D);
        PontoInteresse churrascaria = new PontoInteresse("Churrascaria", 28D, 2D);

        service.add(lanchonete);
        service.add(posto);
        service.add(joalheria);
        service.add(pub);
        service.add(supermercado);
        service.add(churrascaria);
    }

}

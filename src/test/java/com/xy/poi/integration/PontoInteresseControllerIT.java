package com.xy.poi.integration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.xy.poi.domain.model.PontoInteresse;
import com.xy.poi.infrastructure.repository.PontoInteresseRepository;
import com.xy.poi.presentation.ponto.PontoInteresseRequest;
import com.xy.poi.presentation.ponto.PontoInteresseResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.*;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.xy.poi.domain.model.PontoInteresseGenerator.*;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PontoInteresseControllerIT {

    private static final String FIND_ALL_PATH = "/poi/pontos-de-interesse/all";

    private static final String CREATE_PATH = "/poi/pontos-de-interesse";

    private static final String NEAR_PATH = "/poi/pontos-de-interesse/near";

    private static final String ERROS_MATCHES = "errors";

    @LocalServerPort
    private int port;

    @Autowired
    private PontoInteresseRepository repository;

    public void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void beforeEach() {
        repository.deleteAll();
    }

    @Test
    public void getAllPontosInteresseTest() {
        List<PontoInteresse> pontos = getList(createLanchonete(),
                                                createPosto(),
                                                createJoalheria(),
                                                createPub(),
                                                createSupermercado(),
                                                createChurrascaria());
        repository.saveAll(pontos);

        Response reponse = when()
                                .get(FIND_ALL_PATH)
                            .then()
                                .statusCode(OK.value())
                                .body("errors", hasSize(0))
                                .root("data")
                                    .body("totalElements", is(6))
                                    .body("content", hasSize(6))
                            .extract()
                                .response();

        List<PontoInteresseResponse> pontosResponse = convetToResponse(reponse.jsonPath()
                                                                              .getList("data.content"));
        for (int i = 0; i < pontos.size(); i++) {
            assertTrue(isEquals(pontos.get(i), pontosResponse.get(i)));
        }

    }

    @Test
    public void addPontoInteresseTest() {
        PontoInteresseRequest request = new PontoInteresseRequest();
        request.setNome("Shopping");
        request.setLatitude(10D);
        request.setLongitude(15D);

        String id = given()
                        .contentType(JSON)
                        .content(request)
                        .post(CREATE_PATH)
                    .then()
                        .statusCode(CREATED.value())
                        .body("errors", hasSize(0))
                        .root("data")
                            .body("nome", is(request.getNome()))
                            .body("latitude", is(request.getLatitude().floatValue()))
                            .body("longitude", is(request.getLongitude().floatValue()))
                    .extract()
                        .response().jsonPath().getString("data.id");

        assertNotNull(repository.findById(id).get());
    }

    @Test
    public void addInvalidPontoInteresseTest() {
        PontoInteresseRequest request = new PontoInteresseRequest();
        request.setNome("");
        request.setLatitude(null);
        request.setLongitude(null);

        given()
            .contentType(JSON)
            .content(request)
            .post(CREATE_PATH)
        .then()
            .statusCode(BAD_REQUEST.value())
            .body("errors", hasSize(3))
            .body("data", Matchers.nullValue())
            .root("errors");
    }

    @Test
    public void getPontosInteresseNearTest() {
        List<PontoInteresse> nearests = getList(createSupermercado(),
                                                createJoalheria(),
                                                createLanchonete(),
                                                createPub());

        List<PontoInteresse> pontos = getList(createPosto(), createChurrascaria());
        pontos.addAll(nearests);

        repository.saveAll(pontos);

        Response reponse = given()
                                .queryParam("latitude", 20D)
                                .queryParam("longitude", 10D)
                                .queryParam("range", 10)
                            .when()
                                .get(NEAR_PATH)
                            .then()
                                .statusCode(OK.value())
                                .body("errors", hasSize(0))
                                .body("data", hasSize(4))
                            .extract()
                                .response();

        List<PontoInteresseResponse> pontosResponse = convetToResponse(reponse.jsonPath().getList("data"));
        Map<String, PontoInteresseResponse> responseMap = mapResponseById(pontosResponse);

        nearests.forEach(n -> {
            PontoInteresseResponse response = responseMap.get(n.getId());
            assertNotNull(reponse);
            assertTrue(isEquals(n, response));
        });
    }

    @Test
    public void getPontosInteresseNearComRangeZeroTest() {
        List<PontoInteresse> nearests = getList(createSupermercado());
        List<PontoInteresse> pontos = getList(createPosto(),
                                                createChurrascaria(),
                                                createJoalheria(),
                                                createLanchonete(),
                                                createPub());
        pontos.addAll(nearests);

        repository.saveAll(pontos);

        Response reponse = given()
                .queryParam("latitude", 23D)
                .queryParam("longitude", 6D)
                .queryParam("range", 0)
                .when()
                .get(NEAR_PATH)
                .then()
                .statusCode(OK.value())
                .body("errors", hasSize(0))
                .body("data", hasSize(1))
                .extract()
                .response();

        List<PontoInteresseResponse> pontosResponse = convetToResponse(reponse.jsonPath().getList("data"));
        Map<String, PontoInteresseResponse> responseMap = mapResponseById(pontosResponse);

        nearests.forEach(n -> {
            PontoInteresseResponse response = responseMap.get(n.getId());
            assertNotNull(reponse);
            assertTrue(isEquals(n, response));
        });
    }

    private Map<String, PontoInteresseResponse> mapResponseById(List<PontoInteresseResponse> responses) {
        return responses.stream()
                        .collect(toMap(p -> p.getId(), p -> p));
    }

    private List<PontoInteresseResponse> convetToResponse(List<HashMap<String, Object>> response) {
        List<PontoInteresseResponse> list = new ArrayList<>();
        response.forEach(s -> {
            PontoInteresseResponse ponto = convetToResponse(s);
            list.add(ponto);
        });
        return list;
    }

    private PontoInteresseResponse convetToResponse(HashMap<String, Object> propers) {
        PontoInteresseResponse ponto = new PontoInteresseResponse();
        ponto.setId((String) propers.get("id"));
        ponto.setLatitude(Double.valueOf(propers.get("latitude").toString()));
        ponto.setLongitude(Double.valueOf(propers.get("longitude").toString()));
        ponto.setNome((String) propers.get("nome"));
        return ponto;
    }

    private boolean isEquals(PontoInteresse ponto, PontoInteresseResponse response) {
        if (ponto.getId().equals(response.getId()) &&
            ponto.getNome().equals(response.getNome()) &&
            ponto.getLatitude().equals(response.getLatitude()) &&
            ponto.getLongitude().equals(response.getLongitude())) {
            return true;
        }
        return false;
    }

    private List<PontoInteresse> getList(PontoInteresse... pontos) {
        List<PontoInteresse> list = new ArrayList<>();
        List<PontoInteresse> arrayAsList = Arrays.asList(pontos);
        list.addAll(arrayAsList);
        return list;
    }

}

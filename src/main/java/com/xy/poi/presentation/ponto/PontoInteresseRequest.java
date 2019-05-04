package com.xy.poi.presentation.ponto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class PontoInteresseRequest {

    private String nome;

    private Double latitude;

    private Double longitude;

    @JsonIgnore
    public GeoJsonPoint getLocalizacao() {

        if (latitude == null || longitude == null)
            return new GeoJsonPoint(0, 0);

        return new GeoJsonPoint(latitude, longitude);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

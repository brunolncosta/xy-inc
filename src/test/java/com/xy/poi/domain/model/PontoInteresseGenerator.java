package com.xy.poi.domain.model;

import java.util.ArrayList;
import java.util.List;

public final class PontoInteresseGenerator {

    public static PontoInteresse createLanchonete() {
        return new PontoInteresse("Lanchonete", 27D, 12D);
    }

    public static PontoInteresse createPosto() {
        return new PontoInteresse("Posto", 31D, 18D);
    }

    public static PontoInteresse createJoalheria() {
        return new PontoInteresse("Joalheria", 15D, 12D);
    }

    public static PontoInteresse createPub() {
        return new PontoInteresse("Pub", 12D, 8D);
    }

    public static PontoInteresse createSupermercado() {
        return new PontoInteresse("Supermercado", 23D, 6D);
    }

    public static PontoInteresse createChurrascaria() {
        return new PontoInteresse("Churrascaria", 28D, 2D);
    }

    public static List<PontoInteresse> getFullList() {
        List<PontoInteresse> list = new ArrayList<>();
        list.add(createLanchonete());
        list.add(createPosto());
        list.add(createJoalheria());
        list.add(createPub());
        list.add(createSupermercado());
        list.add(createChurrascaria());
        return list;
    }

}

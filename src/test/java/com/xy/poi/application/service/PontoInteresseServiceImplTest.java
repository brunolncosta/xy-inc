package com.xy.poi.application.service;

import com.xy.poi.domain.exception.InvalidRangeException;
import com.xy.poi.domain.service.PontoInteresseService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PontoInteresseServiceImplTest {

    @Test
    public void invalidRangeTest() {
        PontoInteresseService service = new PontoInteresseServiceImpl();
        assertThrows(InvalidRangeException.class, () -> service.findNear(0D, 0D, -1D));
    }

}

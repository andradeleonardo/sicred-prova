package br.com.sicred.prova.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppUtilsTest {

    @Test
    void shouldGetCurrentDatetime() {
        assertNotNull(AppUtils.getCurrentDatetime());
    }

}

package br.com.sicred.prova.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AppUtils {

    private AppUtils() {
    }

    public static LocalDateTime getCurrentDatetime() {
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

}

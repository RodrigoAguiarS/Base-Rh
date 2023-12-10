package br.com.rodrigo.api.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class DateTimeUtil {

    private static final ZoneId FUSO_HORARIO = ZoneId.of("America/Sao_Paulo");

    public static LocalDate obterDataAtual() {
        return LocalDate.now(FUSO_HORARIO);
    }

    public static LocalTime obterHoraAtual() {
        return LocalTime.now(FUSO_HORARIO);
    }
}

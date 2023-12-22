package br.com.rodrigo.api.service.impl.salario;

import java.math.BigDecimal;

public class IRRFConstantes {

    public static final BigDecimal LIMITE_FAIXA1 = new BigDecimal("2112.00");
    public static final BigDecimal LIMITE_FAIXA2 = new BigDecimal("2826.65");
    public static final BigDecimal LIMITE_FAIXA3 = new BigDecimal("3751.05");
    public static final BigDecimal LIMITE_FAIXA4 = new BigDecimal("4664.68");

    public static final BigDecimal ALIQUOTA_FAIXA1 = BigDecimal.ZERO;
    public static final BigDecimal ALIQUOTA_FAIXA2 = new BigDecimal("7.5");
    public static final BigDecimal ALIQUOTA_FAIXA3 = new BigDecimal("15");
    public static final BigDecimal ALIQUOTA_FAIXA4 = new BigDecimal("22.5");
    public static final BigDecimal ALIQUOTA_FAIXA5 = new BigDecimal("27.5");
}

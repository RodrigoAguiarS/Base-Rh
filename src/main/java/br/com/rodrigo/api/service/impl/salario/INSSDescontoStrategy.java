package br.com.rodrigo.api.service.impl.salario;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class INSSDescontoStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(BigDecimal salario) {
        if (salario.compareTo(INSSConstantes.LIMITE_FAIXA1) <= 0) {
            return salario.multiply(INSSConstantes.ALIQUOTA_FAIXA1.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else if (salario.compareTo(INSSConstantes.LIMITE_FAIXA2) <= 0) {
            return salario.multiply(INSSConstantes.ALIQUOTA_FAIXA2.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else if (salario.compareTo(INSSConstantes.LIMITE_FAIXA3) <= 0) {
            return salario.multiply(INSSConstantes.ALIQUOTA_FAIXA3.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else {
            return salario.multiply(INSSConstantes.ALIQUOTA_FAIXA4.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        }
    }
}

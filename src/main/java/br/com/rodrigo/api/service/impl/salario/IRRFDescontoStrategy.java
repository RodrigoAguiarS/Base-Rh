package br.com.rodrigo.api.service.impl.salario;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class IRRFDescontoStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(BigDecimal salario) {
        BigDecimal divisor = BigDecimal.valueOf(100);

        if (salario.compareTo(IRRFConstantes.LIMITE_FAIXA1) <= 0) {
            return BigDecimal.ZERO; // Isenção
        } else if (salario.compareTo(IRRFConstantes.LIMITE_FAIXA2) <= 0) {
            return salario.divide(divisor, 2, RoundingMode.HALF_UP)
                    .multiply(IRRFConstantes.ALIQUOTA_FAIXA2);
        } else if (salario.compareTo(IRRFConstantes.LIMITE_FAIXA3) <= 0) {
            return salario.divide(divisor, 2, RoundingMode.HALF_UP)
                    .multiply(IRRFConstantes.ALIQUOTA_FAIXA3);
        } else if (salario.compareTo(IRRFConstantes.LIMITE_FAIXA4) <= 0) {
            return salario.divide(divisor, 2, RoundingMode.HALF_UP)
                    .multiply(IRRFConstantes.ALIQUOTA_FAIXA4);
        } else {
            return salario.divide(divisor, 2, RoundingMode.HALF_UP)
                    .multiply(IRRFConstantes.ALIQUOTA_FAIXA5);
        }
    }
}

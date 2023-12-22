package br.com.rodrigo.api.service.impl.salario;

import java.math.BigDecimal;

public interface DescontoStrategy {
    BigDecimal calcularDesconto(BigDecimal salario);
}
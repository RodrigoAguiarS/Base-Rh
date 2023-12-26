package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.Funcionario;

import java.math.BigDecimal;
import java.util.List;

public interface DescontoStrategy {
    BigDecimal calcularDesconto(Funcionario funcionario);
}
package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class INSSDescontoStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(Funcionario funcionario) {
        if (funcionario.getCargo().getSalarioBase().compareTo(INSSConstantes.LIMITE_FAIXA1) <= 0) {
            return funcionario.getCargo().getSalarioBase().multiply(INSSConstantes.ALIQUOTA_FAIXA1.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else if (funcionario.getCargo().getSalarioBase().compareTo(INSSConstantes.LIMITE_FAIXA2) <= 0) {
            return funcionario.getCargo().getSalarioBase().multiply(INSSConstantes.ALIQUOTA_FAIXA2.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else if (funcionario.getCargo().getSalarioBase().compareTo(INSSConstantes.LIMITE_FAIXA3) <= 0) {
            return funcionario.getCargo().getSalarioBase().multiply(INSSConstantes.ALIQUOTA_FAIXA3.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else {
            return funcionario.getCargo().getSalarioBase().multiply(INSSConstantes.ALIQUOTA_FAIXA4.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        }
    }
}

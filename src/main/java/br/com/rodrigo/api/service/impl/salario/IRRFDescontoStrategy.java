package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class IRRFDescontoStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(Funcionario funcionario) {

        if (funcionario.getCargo().getSalarioBase().compareTo(IRRFConstantes.LIMITE_FAIXA1) <= 0) {
            return IRRFConstantes.ALIQUOTA_FAIXA1; // Isenção
        }

        if (funcionario.getCargo().getSalarioBase().compareTo(IRRFConstantes.LIMITE_FAIXA2) <= 0) {
            return calcularDescontoFaixa(funcionario.getCargo().getSalarioBase(), IRRFConstantes.ALIQUOTA_FAIXA2,
                    IRRFConstantes.PARCELA_REDUZIR_FAIXA2);
        }

        if (funcionario.getCargo().getSalarioBase().compareTo(IRRFConstantes.LIMITE_FAIXA3) <= 0) {
            return calcularDescontoFaixa(funcionario.getCargo().getSalarioBase(), IRRFConstantes.ALIQUOTA_FAIXA3,
                    IRRFConstantes.PARCELA_REDUZIR_FAIXA3);
        }

        if (funcionario.getCargo().getSalarioBase().compareTo(IRRFConstantes.LIMITE_FAIXA4) <= 0) {
            return calcularDescontoFaixa(funcionario.getCargo().getSalarioBase(), IRRFConstantes.ALIQUOTA_FAIXA4,
                    IRRFConstantes.PARCELA_REDUZIR_FAIXA4);
        }

        return calcularDescontoFaixa(funcionario.getCargo().getSalarioBase(), IRRFConstantes.ALIQUOTA_FAIXA5,
                IRRFConstantes.PARCELA_REDUZIR_FAIXA5);
    }

    private BigDecimal calcularDescontoFaixa(BigDecimal salario, BigDecimal aliquota, BigDecimal parcelaReduzir) {
        BigDecimal divisor = BigDecimal.valueOf(100);
        BigDecimal descontoAntesReducao = salario.multiply(aliquota).divide(divisor, 2, RoundingMode.HALF_UP);
        return descontoAntesReducao.subtract(parcelaReduzir);
    }
}
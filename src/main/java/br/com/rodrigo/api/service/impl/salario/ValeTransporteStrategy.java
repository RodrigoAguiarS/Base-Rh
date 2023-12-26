package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Component
public class ValeTransporteStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(Funcionario funcionario) {
        if (funcionario.getTiposDesconto().stream().anyMatch(tipoDesconto ->
                Objects.equals(tipoDesconto.getId(), ValeTransporteConstantes.VALE_TRANSPORTE))) {
            BigDecimal salarioBruto = funcionario.getCargo().getSalarioBase();
            BigDecimal porcentagemDesconto = ValeTransporteConstantes.ALIQUOTA_VALE_TRANSPORTE;

            return salarioBruto.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).multiply(porcentagemDesconto);
        } else {
            return BigDecimal.ZERO;
        }
    }
}

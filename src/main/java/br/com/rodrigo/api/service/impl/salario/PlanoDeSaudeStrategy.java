package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class PlanoDeSaudeStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(Funcionario funcionario) {
        if (funcionario.getTiposDesconto().stream().anyMatch(tipoDesconto ->
                Objects.equals(tipoDesconto.getId(), PlanoDeSaudeConstantes.PLANO_DE_SAUDE))) {

            return PlanoDeSaudeConstantes.VALOR_PLANO_DE_SAUDE;
        } else {
            return BigDecimal.ZERO;
        }
    }
}

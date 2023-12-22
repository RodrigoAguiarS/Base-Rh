package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.model.FolhaPagamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.repository.FolhaPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository folhaPagamentoRepository;

    private final INSSDescontoStrategy inssDescontoStrategy;

    private final IRRFDescontoStrategy irrfDescontoStrategy;

    public FolhaPagamento gerarFolhaPagamento(Funcionario funcionario) {
        FolhaPagamento folhaPagamento = new FolhaPagamento();
        folhaPagamento.setFuncionario(funcionario);
        folhaPagamento.setDataPagamento(new Date());

        BigDecimal salarioBase = funcionario.getCargo().getSalarioBase();
        BigDecimal descontoInss = inssDescontoStrategy.calcularDesconto(salarioBase);
        BigDecimal descontoIrrf = irrfDescontoStrategy.calcularDesconto(salarioBase);

        BigDecimal valorLiquido = salarioBase.subtract(descontoInss).subtract(descontoIrrf);
        BigDecimal totalDescontos = descontoInss.add(descontoIrrf);

        folhaPagamento.setValorBruto(salarioBase);
        folhaPagamento.setDescontoInss(descontoInss);
        folhaPagamento.setDescontoIrrf(descontoIrrf);
        folhaPagamento.setTotalDescontos(totalDescontos);
        folhaPagamento.setValorLiquido(valorLiquido);

        return folhaPagamentoRepository.save(folhaPagamento);
    }
}

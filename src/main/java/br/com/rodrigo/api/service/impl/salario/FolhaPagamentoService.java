package br.com.rodrigo.api.service.impl.salario;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.FolhaPagamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.repository.FolhaPagamentoRepository;
import br.com.rodrigo.api.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_GERAR_FOLHA_PAGAMENTO_TODOS;

@Service
@RequiredArgsConstructor
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository folhaPagamentoRepository;

    private final FuncionarioService funcionarioService;

    private final INSSDescontoStrategy inssDescontoStrategy;

    private final IRRFDescontoStrategy irrfDescontoStrategy;

    private final ValeTransporteStrategy valeTransporteStrategy;

    private final PlanoDeSaudeStrategy planoDeSaudeStrategy;

    @Value("classpath:reports/folha_de_pagamento.jasper")
    private Resource relatorioCompilado;

    private final DataSource dataSource;

    @Transactional
    public List<FolhaPagamento> gerarFolhaPagamentoParaTodosFuncionarios() {

        validarFolhaPagamentoDoMes();
        List<Funcionario> funcionarios = funcionarioService.listaTodosFuncionariosAtivos();

        List<FolhaPagamento> folhasPagamento = new ArrayList<>();

        for (Funcionario funcionario : funcionarios) {
            FolhaPagamento folhaPagamento = gerarFolhaPagamento(funcionario);
            folhasPagamento.add(folhaPagamento);
        }

        folhaPagamentoRepository.saveAll(folhasPagamento);

        return folhasPagamento;
    }

    private FolhaPagamento gerarFolhaPagamento(Funcionario funcionario) {
        FolhaPagamento folhaPagamento = new FolhaPagamento();
        folhaPagamento.setFuncionario(funcionario);
        folhaPagamento.setDataPagamento(new Date());

        BigDecimal salarioBase = funcionario.getCargo().getSalarioBase();
        BigDecimal descontoInss = inssDescontoStrategy.calcularDesconto(funcionario);
        BigDecimal descontoIrrf = irrfDescontoStrategy.calcularDesconto(funcionario);
        BigDecimal descontoValeTransporte = valeTransporteStrategy.calcularDesconto(funcionario);
        BigDecimal descontoPlanoDeSaude = planoDeSaudeStrategy.calcularDesconto(funcionario);

        BigDecimal valorLiquido = salarioBase.subtract(descontoInss).subtract(descontoIrrf).
                subtract(descontoValeTransporte).subtract(descontoPlanoDeSaude);
        BigDecimal totalDescontos = descontoInss.add(descontoIrrf).add(descontoValeTransporte).
                add(descontoPlanoDeSaude);

        folhaPagamento.setValorBruto(salarioBase);
        folhaPagamento.setDescontoInss(descontoInss);
        folhaPagamento.setDescontoIrrf(descontoIrrf);
        folhaPagamento.setDescontoValeTransporte(descontoValeTransporte);
        folhaPagamento.setDescontoPlanoDeSaude(descontoPlanoDeSaude);
        folhaPagamento.setTotalDescontos(totalDescontos);
        folhaPagamento.setValorLiquido(valorLiquido);

        return folhaPagamento;
    }

    public FolhaPagamento salvarFolhaPagamento(Funcionario funcionario) {
        FolhaPagamento folhaPagamento = gerarFolhaPagamento(funcionario);
        return folhaPagamentoRepository.save(folhaPagamento);
    }

    public byte[] gerarRelatorio(Long idFuncionario) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {

            Map<String, Object> paramentros = new HashMap<>();
            paramentros.put("ID_FUNCIONARIO", idFuncionario);
            return JasperRunManager.runReportToPdf(relatorioCompilado.getInputStream(),
                    paramentros,
                    connection);

        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validarFolhaPagamentoDoMes() {
        Calendar cal = Calendar.getInstance();
        int mesAtual = cal.get(Calendar.MONTH) + 1;
        int anoAtual = cal.get(Calendar.YEAR);

        boolean existeFolhaParaMesAtual = folhaPagamentoRepository.existsByMesAndAno(mesAtual, anoAtual);

        if (existeFolhaParaMesAtual) {
            throw new ViolocaoIntegridadeDadosException(ERRO_GERAR_FOLHA_PAGAMENTO_TODOS);
        }
    }
}

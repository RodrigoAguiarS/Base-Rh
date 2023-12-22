package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.DemissaoFuncionario;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.dto.DemissaoDto;
import br.com.rodrigo.api.repository.DemissaoFuncionarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCIONARIO_JA_DEMITIDO;

@Service
@RequiredArgsConstructor
public class DemissaoFuncionarioService {

    private final DemissaoFuncionarioRepository demissaoFuncionarioRepository;

    public void salvarDemissao(Funcionario funcionario, LocalDate dataSaida, DemissaoDto demissaoDto) {

        DemissaoFuncionario demissaoFuncionario = new DemissaoFuncionario();
        demissaoFuncionario.setFuncionario(funcionario);
        demissaoFuncionario.setDataSaida(dataSaida);
        demissaoFuncionario.setTipoDemissao(demissaoDto.getTipoDemissao());
        demissaoFuncionario.setMotivo(demissaoDto.getMotivo());

        demissaoFuncionarioRepository.save(demissaoFuncionario);
    }
}

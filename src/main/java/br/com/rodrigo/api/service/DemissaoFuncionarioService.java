package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.DemissaoFuncionario;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.dto.DemissaoDto;
import br.com.rodrigo.api.model.dto.TipoDemissaoDto;
import br.com.rodrigo.api.repository.DemissaoFuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class DemissaoFuncionarioService {

    private final DemissaoFuncionarioRepository demissaoFuncionarioRepository;

    public void salvarDemissao(Funcionario funcionario, LocalDate dataSaida, DemissaoDto demissaoDto) {

        DemissaoFuncionario demissaoFuncionario = new DemissaoFuncionario();
        demissaoFuncionario.setFuncionario(funcionario);
        demissaoFuncionario.setDataSaida(dataSaida);
        demissaoFuncionario.setTipoDemissao(TipoDemissaoDto.toEntity(demissaoDto.getTipoDemissao()));
        demissaoFuncionario.setMotivo(demissaoDto.getMotivo());

        demissaoFuncionarioRepository.save(demissaoFuncionario);
    }
}

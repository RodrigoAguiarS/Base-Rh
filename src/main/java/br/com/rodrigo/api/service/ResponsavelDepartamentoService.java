package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.CadastroResponsavelDepartamentoDto;
import br.com.rodrigo.api.model.dto.ResponsavelDepartamentoDto;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.ResponsavelDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResponsavelDepartamentoService {

    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoRepository responsavelDepartamentoRepository;

    private final DepartamentoRepository departamentoRepository;

    public ResponsavelDepartamentoDto vincularResponsavelDepartamento(CadastroResponsavelDepartamentoDto responsavelDepartamentoDto) {
        Optional<Funcionario> optionalFuncionario = funcionarioRepository.findById(responsavelDepartamentoDto.getFuncionario());

        Optional<Departamento> optionalDepartamento = departamentoRepository.findById(responsavelDepartamentoDto.getDepartamento());

        if (optionalFuncionario.isEmpty() || optionalDepartamento.isEmpty()) {
            // Lida com a situação em que funcionário ou departamento não são encontrado
            throw new ViolocaoIntegridadeDadosException("Funcionário ou departamento não encontrado");
        }

        Funcionario funcionario = optionalFuncionario.get();
        Departamento departamento = optionalDepartamento.get();

        verificarResponsabilidadeExistente(funcionario.getId(), departamento.getId());
        verificarResponsavelDepartamentoExistente(departamento.getId());

        ResponsavelDepartamento responsavelDepartamento = new ResponsavelDepartamento();
        responsavelDepartamento.setFuncionario(funcionario);
        responsavelDepartamento.setDepartamento(departamento);

        ResponsavelDepartamento savedResponsavelDepartamento = responsavelDepartamentoRepository.save(responsavelDepartamento);

        return ResponsavelDepartamentoDto.fromEntity(savedResponsavelDepartamento);
    }

    public List<ResponsavelDepartamento> listarTodosResponsavelDepartamentos() {
        return responsavelDepartamentoRepository.findAll();

    }

    public void verificarResponsabilidadeExistente(Long idFuncionario, Long idDepartamento) {
        Optional<ResponsavelDepartamento> responsabilidadeExistente = responsavelDepartamentoRepository
                .findByFuncionarioIdAndDepartamentoId(idFuncionario, idDepartamento);
        if (responsabilidadeExistente.isPresent()) {
            throw new ViolocaoIntegridadeDadosException("O funcionário já é responsável por esta unidade.");
        }
    }

    public void verificarResponsavelDepartamentoExistente(Long idDepartamento) {
        if (responsavelDepartamentoRepository.existsByDepartamentoId(idDepartamento)) {
            throw new ViolocaoIntegridadeDadosException("Este departamento já possui um responsável.");
        }
    }
}

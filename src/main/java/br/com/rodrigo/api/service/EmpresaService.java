package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Empresa;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.dto.EmpresaDto;
import br.com.rodrigo.api.model.dto.EnderecoDto;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import br.com.rodrigo.api.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_EMPRESA_DEPARTAMENTO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_EMPRESA_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    private final DepartamentoRepository departamentoRepository;

    public EmpresaDto cadastrarEmpresa(EmpresaDto empresaDto) {
        Empresa empresa = EmpresaDto.toEntity(empresaDto);
        Empresa savedEmpresa = empresaRepository.save(empresa);
        return EmpresaDto.fromEntity(savedEmpresa);
    }

    public EmpresaDto atualizarEmpresa(Long id, EmpresaDto empresaDto) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresaExistente = empresaOptional.get();
            empresaExistente.setNome(empresaDto.getNome());
            empresaExistente.setCnpj(empresaDto.getCnpj());
            empresaExistente.setTelefone(empresaDto.getTelefone());
            empresaExistente.setEndereco(EnderecoDto.toEntity(empresaDto.getEndereco()));

            Empresa empresaAtualizada = empresaRepository.save(empresaExistente);
            return EmpresaDto.fromEntity(empresaAtualizada);
        }
        return null;
    }

    public List<EmpresaDto> getAllEmpresas() {
        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream().map(EmpresaDto::fromEntity).collect(Collectors.toList());
    }

    public EmpresaDto getEmpresaById(Long id) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        return empresaOptional.map(EmpresaDto::fromEntity)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_EMPRESA_NAO_ENCONTRADO));
    }

    @Transactional
    public void deleteEmpresa(Long id) {

        if (departamentoRepository.existsByEmpresaId(id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_EMPRESA_DEPARTAMENTO);
        }

        empresaRepository.deleteById(id);
    }

    Empresa obterEmpresaDoFuncionario(Funcionario funcionario) {
        return empresaRepository.findById(funcionario.getCargo().getDepartamento().getEmpresa().getId())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_EMPRESA_NAO_ENCONTRADO));
    }

    Optional<Empresa> getEmpresaByIdOptional(Long id) {
        return empresaRepository.findById(id);
    }
}

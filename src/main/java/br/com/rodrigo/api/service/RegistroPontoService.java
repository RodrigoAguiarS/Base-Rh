package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.RegistroPonto;
import br.com.rodrigo.api.model.dto.CadastroRegistroPontoDto;
import br.com.rodrigo.api.model.dto.RegistroPontoDto;
import br.com.rodrigo.api.repository.RegistroPontoRepository;
import br.com.rodrigo.api.util.DateTimeUtil;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_REGISTRO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_REGISTRO_PONTO_PESSOA;


@Service
@RequiredArgsConstructor
public class RegistroPontoService {

    private final UsuarioService usuarioService;

    private final RegistroPontoRepository registroPontoRepository;

    public RegistroPontoDto registrarPonto(CadastroRegistroPontoDto cadastroRegistroPontoDto) {
        Funcionario funcionario = usuarioService.getFuncionarioDoUsuarioLogado();
        RegistroPonto registroPonto = criarRegistroPontoAPartirDeDTO(cadastroRegistroPontoDto, funcionario);

        return RegistroPontoDto.fromEntity(registroPontoRepository.save(registroPonto));
    }

    public RegistroPonto criarRegistroPontoAPartirDeDTO(CadastroRegistroPontoDto registroPontoDto, Funcionario funcionario) {
        LocalDate dataAtual = DateTimeUtil.obterDataAtual();
        LocalTime horaAtual = DateTimeUtil.obterHoraAtual();

        Optional<RegistroPonto> optionalRelogioPonto = registroPontoRepository
                .findByFuncionarioAndDataRegistroAndPontoRegistrado(funcionario, dataAtual, false);

        if (optionalRelogioPonto.isPresent()) {
            RegistroPonto relogioPontoExistente = optionalRelogioPonto.get();
            if (ValidatorUtil.isEmpty(relogioPontoExistente.getHoraSaida())) {
                relogioPontoExistente.setObservacoes(registroPontoDto.getObservacoes());
                return registrarSaida(relogioPontoExistente, horaAtual,
                        true);
            } else {
                throw new ViolocaoIntegridadeDadosException(ERRO_REGISTRO_PONTO_PESSOA);
            }
        } else {
            RegistroPonto registroPontoNovo = CadastroRegistroPontoDto.toEntity(registroPontoDto);
            registroPontoNovo.setHoraEntrada(horaAtual);
            registroPontoNovo.setDataRegistro(dataAtual);
            registroPontoNovo.setFuncionario(funcionario);
            return registroPontoNovo;
        }
    }

    public RegistroPonto registrarSaida(RegistroPonto registroPonto, LocalTime horaSaida,
                                        boolean pontoRegistrado) {
        registroPonto.setHoraSaida(horaSaida);
        registroPonto.setPontoRegistrado(pontoRegistrado);
        return registroPontoRepository.save(registroPonto);
    }

    public boolean isEntrada() {
        Funcionario funcionario = usuarioService.getFuncionarioDoUsuarioLogado();
        LocalDate dataAtual = LocalDate.now();
        Optional<RegistroPonto> optionalRegistroPonto = registroPontoRepository
                .findByFuncionarioAndDataRegistroAndPontoRegistrado(funcionario, dataAtual, false);
        return optionalRegistroPonto.isPresent();
    }
    public List<RegistroPontoDto> listarHorariosDeTrabalhoFuncionario() {

        Funcionario funcionario = usuarioService.getFuncionarioDoUsuarioLogado();

        if (ValidatorUtil.isNotEmpty(funcionario)) {
            List<RegistroPonto> registros = registroPontoRepository.
                    findByFuncionarioOrderByDataRegistroDescHoraEntradaDesc(funcionario);
            // Mapeie os registros para DTOs
            return registros.stream()
                    .map(RegistroPontoDto::fromEntity)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public RegistroPontoDto buscarPorIdRegistro(Long id) {
        RegistroPonto registroPonto = registroPontoRepository.findById(id).
                orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_REGISTRO_NAO_ENCONTRADO));
        return RegistroPontoDto.fromEntity(registroPonto);
    }
}


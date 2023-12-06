package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.RegistroPonto;
import br.com.rodrigo.api.model.dto.RegistroPontoDto;
import br.com.rodrigo.api.repository.RegistroPontoRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class RegistroPontoService {

    private final UsuarioService usuarioService;

    private final RegistroPontoRepository registroPontoRepository;

    public RegistroPontoDto registrarPonto(RegistroPontoDto registroPontoDto) {
        Funcionario funcionario = usuarioService.getFuncionarioDoUsuarioLogado();
        RegistroPonto registroPonto = criarRelogioPontoAPartirDeDTO(registroPontoDto, funcionario);

        return RegistroPontoDto.fromEntity(registroPontoRepository.save(registroPonto));
    }

    public RegistroPonto criarRelogioPontoAPartirDeDTO(RegistroPontoDto registroPontoDto, Funcionario funcionario) {
        LocalDate dataAtual = LocalDate.now();
        Optional<RegistroPonto> optionalRelogioPonto = registroPontoRepository
                .findByFuncionarioAndDataRegistroAndPontoRegistrado(funcionario, dataAtual, false);

        if (optionalRelogioPonto.isPresent()) {
            RegistroPonto relogioPontoExistente = optionalRelogioPonto.get();
            if (ValidatorUtil.isEmpty(relogioPontoExistente.getHoraSaida())) {
                relogioPontoExistente.setObservacoes(registroPontoDto.getObservacoes());
                return updateRegistroPonto(relogioPontoExistente, LocalTime.now(),
                        true);
            } else {
                throw new ViolocaoIntegridadeDadosException("Já existe um registro de ponto para essa pessoa nessa data.");
            }
        } else {
            RegistroPonto relogioPontoNovo = RegistroPontoDto.toEntity(registroPontoDto);
            relogioPontoNovo.setFuncionario(funcionario);
            return relogioPontoNovo;
        }
    }

    public RegistroPonto updateRegistroPonto(RegistroPonto registroPonto, LocalTime horaSaida,
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
}

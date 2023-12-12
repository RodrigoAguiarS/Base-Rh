package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.Vinculo;
import br.com.rodrigo.api.model.dto.VinculoDto;
import br.com.rodrigo.api.repository.VinculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_VINCULO_NAO_ENCONTRADO;


@Service
@RequiredArgsConstructor
public class VinculoService {

    
    private final VinculoRepository vinculoRepository;

    public List<Vinculo> getAllVinculos() {
        return vinculoRepository.findAll();
    }

    public Optional<Vinculo> getVinculoById(Long id) {
        return vinculoRepository.findById(id);
    }

    public VinculoDto salvarVinculo(VinculoDto vinculoDto) {
        Vinculo cargo = VinculoDto.toEntity(vinculoDto);
        Vinculo savedVinculo = vinculoRepository.save(cargo);
        return VinculoDto.fromEntity(savedVinculo);
    }

    public VinculoDto atualizarVinculo(Long id, VinculoDto vinculoDto) {

        Vinculo vinculoExistente = vinculoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_VINCULO_NAO_ENCONTRADO));

        vinculoExistente.setNome(vinculoDto.getNome());
        vinculoExistente.setDescricao(vinculoDto.getDescricao());
        vinculoExistente.setAtivo(vinculoDto.isAtivo());
        vinculoRepository.save(vinculoExistente);

        return VinculoDto.fromEntity(vinculoExistente);
    }
    public void deleteVinculo(Long idVinculo) {

        vinculoRepository.deleteById(idVinculo);
    }
}


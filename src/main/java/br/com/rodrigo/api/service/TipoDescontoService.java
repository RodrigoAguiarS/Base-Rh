package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.TipoDesconto;
import br.com.rodrigo.api.model.dto.TipoDescontoDto;
import br.com.rodrigo.api.repository.TipoDescontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_TIPO_DESCONTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class TipoDescontoService {
    
    private final TipoDescontoRepository tipoDescontoRepository;

    public List<TipoDesconto> getAllTipoDemissoes() {
        return tipoDescontoRepository.findByAtivoTrue();
    }

    public Optional<TipoDesconto> getTipoDescontoById(Long id) {
        return tipoDescontoRepository.findByIdAndAtivoTrue(id);
    }

    public TipoDescontoDto salvarTipoDesconto(TipoDescontoDto tipoDescontoDto) {
        TipoDesconto tipoDesconto = TipoDescontoDto.toEntity(tipoDescontoDto);
        TipoDesconto savedTipoDesconto = tipoDescontoRepository.save(tipoDesconto);
        return TipoDescontoDto.fromEntity(savedTipoDesconto);
    }

    public TipoDescontoDto atualizarTipoDesconto(Long id, TipoDescontoDto tipoDemissaoDto) {

        TipoDesconto tipoDescontoExistente = tipoDescontoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_TIPO_DESCONTO_NAO_ENCONTRADO));

        tipoDescontoExistente.setNome(tipoDemissaoDto.getNome());
        tipoDescontoExistente.setDescricao(tipoDemissaoDto.getDescricao());
        tipoDescontoExistente.setAtivo(tipoDemissaoDto.isAtivo());
        tipoDescontoRepository.save(tipoDescontoExistente);

        return TipoDescontoDto.fromEntity(tipoDescontoExistente);
    }

    public void deleteTipoDesconto(Long idTipoDesconto) {

        tipoDescontoRepository.deleteById(idTipoDesconto);
    }
}

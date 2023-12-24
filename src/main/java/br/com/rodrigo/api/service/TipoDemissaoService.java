package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.TipoDemissao;
import br.com.rodrigo.api.model.dto.TipoDemissaoDto;
import br.com.rodrigo.api.repository.TipoDemissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_TIPO_DEMISAO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class TipoDemissaoService {

    private final TipoDemissaoRepository tipoDemissaoRepository;

    public List<TipoDemissao> getAllTipoDemissoes() {
        return tipoDemissaoRepository.findByAtivoTrue();
    }

    public Optional<TipoDemissao> getTipoDemissaoById(Long id) {
        return tipoDemissaoRepository.findByIdAndAtivoTrue(id);
    }

    public TipoDemissaoDto salvarTipoDemissao(TipoDemissaoDto tipoDemissaoDto) {
        TipoDemissao tipoDemissao = TipoDemissaoDto.toEntity(tipoDemissaoDto);
        TipoDemissao savedTipoDemissao = tipoDemissaoRepository.save(tipoDemissao);
        return TipoDemissaoDto.fromEntity(savedTipoDemissao);
    }

    public TipoDemissaoDto atualizarTipoDemissao(Long id, TipoDemissaoDto tipoDemissaoDto) {

        TipoDemissao tipoDemissaoExistente = tipoDemissaoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_TIPO_DEMISAO_NAO_ENCONTRADO));

        tipoDemissaoExistente.setNome(tipoDemissaoDto.getNome());
        tipoDemissaoExistente.setDescricao(tipoDemissaoDto.getDescricao());
        tipoDemissaoExistente.setAtivo(tipoDemissaoDto.isAtivo());
        tipoDemissaoRepository.save(tipoDemissaoExistente);

        return TipoDemissaoDto.fromEntity(tipoDemissaoExistente);
    }
    public void deleteTipoDemissao(Long idTipoDemissao) {

        tipoDemissaoRepository.deleteById(idTipoDemissao);
    }
}

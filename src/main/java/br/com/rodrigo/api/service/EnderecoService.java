package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.Endereco;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.EnderecoDto;
import br.com.rodrigo.api.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public Endereco salvarEndereco(CadastroUsuarioDto cadastroUsuarioDto) {
        Endereco endereco = EnderecoDto.toEntity(cadastroUsuarioDto.getPessoa().getEndereco());
        return enderecoRepository.save(endereco);
    }
}

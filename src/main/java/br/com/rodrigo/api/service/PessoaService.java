package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.Endereco;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.EnderecoDto;
import br.com.rodrigo.api.model.dto.PessoaDto;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;

import static br.com.rodrigo.api.util.ValidatorUtil.validarCpfExistente;
import static br.com.rodrigo.api.util.ValidatorUtil.validarCpfExistenteComId;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    private final EnderecoService enderecoService;

    public Pessoa atualizarPessoaExistente(Pessoa pessoaExistente, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        String email = cadastroUsuarioDto.getEmail();
        validarCpfExistenteComId(pessoaRepository, email, pessoaExistente.getId());
        PessoaDto pessoaDto = cadastroUsuarioDto.getPessoa();
        Pessoa pessoaAtualizada = PessoaDto.toEntity(pessoaDto);
        pessoaAtualizada.setId(pessoaExistente.getId());

        if (ValidatorUtil.isNotEmpty(pessoaDto.getEndereco())) {
            Endereco enderecoAtualizado = EnderecoDto.toEntity(pessoaDto.getEndereco());
            pessoaAtualizada.setEndereco(enderecoAtualizado);
        }

        return pessoaRepository.save(pessoaAtualizada);
    }

    public Pessoa salvarNovaPessoa(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa novaPessoa = PessoaDto.toEntity(cadastroUsuarioDto.getPessoa());
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        validarCpfExistente(pessoaRepository, cpf);
        novaPessoa.setEndereco(enderecoService.salvarEndereco(cadastroUsuarioDto));
        return pessoaRepository.save(novaPessoa);
    }

    public Pessoa obterPessoaDoUsuario(Usuario usuario) {
        return usuario.getPessoa();
    }

    public void deletarPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }
}

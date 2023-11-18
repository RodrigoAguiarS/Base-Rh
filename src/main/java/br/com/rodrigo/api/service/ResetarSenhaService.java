package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.ResetarSenhaToken;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.EmailRecuperacaoDto;
import br.com.rodrigo.api.model.dto.SenhaRecuperacaoDto;
import br.com.rodrigo.api.repository.ResetarSenhaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_ATUALIZAR_SENHA_USUARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_UID_EXPERIADO;
import static br.com.rodrigo.api.util.EmailMensagensUtil.REDIFINICAO_SENHA;
import static br.com.rodrigo.api.util.EmailMensagensUtil.URL_REDIFINICAO_SENHA;
import static br.com.rodrigo.api.util.EmailMensagensUtil.getEmailConfirmacaoRedifinicao;

@Service
@RequiredArgsConstructor
public class ResetarSenhaService {

    private final ResetarSenhaRepository resetarSenhaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public void solicitarRedefinicaoSenha(EmailRecuperacaoDto solicitacao) {
        String uid = UUID.randomUUID().toString();

        ResetarSenhaToken token = new ResetarSenhaToken(solicitacao.getEmail(), uid, true);
        resetarSenhaRepository.save(token);

        String urlRedefinicaoSenha = URL_REDIFINICAO_SENHA + uid;
        String conteudoEmail = getEmailConfirmacaoRedifinicao(urlRedefinicaoSenha);

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(solicitacao.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            CompletableFuture.runAsync(() -> {
                emailService.sendEmail(usuario.getEmail(), REDIFINICAO_SENHA, conteudoEmail);
            });
        }
    }

    public Optional<ResetarSenhaToken> buscarTokenAtivoPorUid(String uid) {
        return resetarSenhaRepository.findByUidAndAtivo(uid, true);
    }

    public void atualizarSenha(String uid, SenhaRecuperacaoDto solicitacao) {
        Optional<ResetarSenhaToken> tokenOptional = resetarSenhaRepository.findByUidAndAtivo(uid, true);
        if (tokenOptional.isEmpty()) {
            throw new ViolocaoIntegridadeDadosException(ERRO_UID_EXPERIADO);
        }

        ResetarSenhaToken token = tokenOptional.get();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(token.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String senhaCriptografada = passwordEncoder.encode(solicitacao.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
        } else {
            throw new ObjetoNaoEncontradoException(ERRO_ATUALIZAR_SENHA_USUARIO_NAO_ENCONTRADO);
        }
        token.setAtivo(false);
        token.setDataUtilizacao(LocalDateTime.now());
        resetarSenhaRepository.save(token);
    }
}



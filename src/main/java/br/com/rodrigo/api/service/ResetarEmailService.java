package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.ResetarSenhaToken;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.EmailRecuperacaoDto;
import br.com.rodrigo.api.model.dto.SenhaRecuperacaoDto;
import br.com.rodrigo.api.repository.ResetarSenhaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Serviço responsável pela recuperação de senha e envio de emails relacionados.
 */
@Service
@RequiredArgsConstructor
public class ResetarEmailService implements EmailService {

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${reset.url}")
    private String urlRecuperacao;

    @Value("${mensagem.email}")
    private String mensagem;

    private final ResetarSenhaRepository resetarSenhaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Envia um email para o destinatário especificado.
     *
     * @param recipientEmail Endereço de email do destinatário
     * @param subject Assunto do email
     * @param content Conteúdo do email
     */
    @Override
    public void sendEmail(String recipientEmail, String subject, String content) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email enviado com sucesso para: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar o email para " + recipientEmail + ": " + e.getMessage());
        }
    }


    /**
     * Solicita a redefinição de senha para o email especificado.
     *
     * @param solicitacao Dados da solicitação de redefinição de senha
     */
    public void solicitarRedefinicaoSenha(EmailRecuperacaoDto solicitacao) {
        String uid = UUID.randomUUID().toString();

        ResetarSenhaToken token = new ResetarSenhaToken(solicitacao.getEmail(), uid, true);
        resetarSenhaRepository.save(token);

        String urlRedefinicaoSenha = urlRecuperacao + uid;
        String conteudoEmail = mensagem + urlRedefinicaoSenha;

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(solicitacao.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            CompletableFuture.runAsync(() -> {
                sendEmail(usuario.getEmail(), "Redefinição de Senha", conteudoEmail);
            });
        }
    }

    /**
     * Busca um token de redefinição de senha ativo pelo UID.
     *
     * @param uid UID do token de redefinição de senha
     * @return Token de redefinição de senha ativo, se encontrado
     */
    public Optional<ResetarSenhaToken> buscarTokenAtivoPorUid(String uid) {
        return resetarSenhaRepository.findByUidAndAtivo(uid, true);
    }

    /**
     * Atualiza a senha do usuário após a redefinição.
     *
     * @param uid UID do token de redefinição de senha
     * @param solicitacao Dados da solicitação de redefinição de senha
     * @throws ObjetoNaoEncontradoException Se o token ou o usuário não forem encontrados
     */
    public void atualizarSenha(String uid, SenhaRecuperacaoDto solicitacao) {
        Optional<ResetarSenhaToken> tokenOptional = resetarSenhaRepository.findByUidAndAtivo(uid, true);
        if (tokenOptional.isEmpty()) {
            throw new RuntimeException("UID inválido ou expirado.");
        }

        ResetarSenhaToken token = tokenOptional.get();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(token.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String senhaCriptografada = passwordEncoder.encode(solicitacao.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
        } else {
            throw new ObjetoNaoEncontradoException("Erro ao atualizar a senha. Usuário não encontrado.");
        }
        token.setAtivo(false);
        token.setDataUtilizacao(LocalDateTime.now());
        resetarSenhaRepository.save(token);
    }
}

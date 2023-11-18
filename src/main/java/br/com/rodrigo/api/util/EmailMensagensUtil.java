package br.com.rodrigo.api.util;

import org.springframework.stereotype.Component;

@Component
public class EmailMensagensUtil {

    public static String CONFIRMACAO_CADASTRO = "Confirmação de cadastro";
    public static String REDIFINICAO_SENHA = "Redefinição de Senha";
    public static String URL_REDIFINICAO_SENHA = "http://localhost:4200/login-alterar/";

    public static String getEmailCadastroTexto(String nome, String email, String senha) {
        return String.format("Olá %s, obrigado por se cadastrar! Seu e-mail: %s, Sua senha: %s", nome, email, senha);
    }

    public static String getEmailConfirmacaoTexto(String link) {
        return String.format("Clique no link a seguir para confirmar seu cadastro: %s", link);
    }

    public static String getEmailConfirmacaoRedifinicao(String url) {
        return String.format("Olá,\\n\\nClique no link a seguir para redefinir sua senha : %s", url);
    }
}

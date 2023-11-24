package br.com.rodrigo.api.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    public static final String ERRO_CPF_DUPLICADO = "CPF já cadastrado. Por favor, insira um CPF único.";
    public static final String ERRO_EMAIL_DUPLICADO = "E-mail já cadastrado. Por favor, insira um e-mail único.";
    public static final String ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL = "Usuário não encontrado para o e-mail: ";
    public static final String ERRO_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    public static final String ERRO_UID_EXPERIADO = "UID inválido ou expirado.";
    public static final String ERRO_ENVIAR_EMAIL = "Erro ao enviar o email para";

    public static final String ERRO_DEPARTAMENTO_NAO_ENCONTRADO = "Departamento não encontrado com o ID";

    public static final String ERRO_FUNCINARIO_NAO_ENCONTRADO = "Funcionário não encontrado com o ID";

    public static final String ERRO_CARGO_NAO_ENCONTRADO = "Cargo não encontrado com o ID: ";


    public static final String ERRO_ATUALIZAR_SENHA_USUARIO_NAO_ENCONTRADO = "Erro ao atualizar a senha. Usuário não encontrado.";

    public static final String ERRO_DELETAR_DERPARTAMENTO_CARGO = "Este departamento possui cargos associados e não pode ser deletado.";
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(long timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public void addErrors(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}

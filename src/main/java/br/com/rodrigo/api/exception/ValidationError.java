package br.com.rodrigo.api.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    public static final String ERRO_CPF_DUPLICADO = "CPF já cadastrado. Por favor, insira um CPF único.";
    public static final String ERRO_EMAIL_DUPLICADO = "E-mail já cadastrado. Por favor, insira um e-mail único.";
    public static final String ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL = "Usuário não encontrado para o e-mail: ";
    public static final String ERRO_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";

    public static final String ERRO_REGISTRO_NAO_ENCONTRADO = "Registro não encontrado";

    public static final String ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO = "Não é possível excluir o usuário. O funcionário é responsável por um departamento.";
    public static final String ERRO_UID_EXPERIADO = "UID inválido ou expirado.";
    public static final String ERRO_ENVIAR_EMAIL = "Erro ao enviar o email para";

    public static final String ERRO_PESSOA_NAO_ENCONTRADA = "Pessoa não encontrada com o ID";

    public static final String ERRO_REGISTRO_NAO_ENCONTRADO_FUNCIONARIO = "Registro de entrada não encontrado para o funcionário na data atual.";

    public static final String ERRO_PESSOA_EH_FUNCIONARIO = "Esta pessoa já está vinculada a outro funcionário.";

    public static final String ERRO_USUARIO_NAO_ASSOCIADA_AO_PESSOA = "Está usuário não está associada a nenhuma pessoa.";

    public static final String ERRO_DEPARTAMENTO_NAO_ENCONTRADO = "Departamento não encontrado com o ID";

    public static final String ERRO_FUNCINARIO_NAO_ENCONTRADO = "Funcionário não encontrado com o ID";

    public static final String ERRO_CARGO_NAO_ENCONTRADO = "Cargo não encontrado com o ID: ";

    public static final String ERRO_RESPONSAVEL_DEPARTAMENTO_NAO_ENCONTRADO = "Responsável por Departamento não encontrado: ";

    public static final String ERRO_ATUALIZAR_SENHA_USUARIO_NAO_ENCONTRADO = "Erro ao atualizar a senha. Usuário não encontrado.";

    public static final String ERRO_DELETAR_DEPARTAMENTO_CARGO = "Este departamento possui cargos associados e não pode ser deletado.";

    public static final String ERRO_DELETAR_DEPARTAMENTO_RESPONSAVEL = "Este departamento possui responsável associado e não pode ser deletado.";

    public static final String ERRO_DELETAR_CARGO_COM_FUNCIONARIO = "Não é possível excluir o cargo porque existem funcionários associados a ele.";

    public static final String ERRO_DEPARTAMENTO_COM_RESPONSAVEL = "Este departamento já possui um responsável.";

    public static final String ERRO_FUNCIONARIO_RESPONSAVEL_DEPARTAMENTO = "O funcionário já é responsável por esta unidade.";
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

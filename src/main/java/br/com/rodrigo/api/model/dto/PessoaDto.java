package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class PessoaDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    @NotNull(message = "Campo telefone é requerido")
    @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres")
    private String telefone;

    @NotNull(message = "Campo CPF é requerido")
    @NotBlank(message = "Campo CPF não pode estar em branco")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter exatamente 11 dígitos numéricos")
    private String cpf;

    @NotNull(message = "Campo data de nascimento é requerido")
    @NotBlank(message = "Campo data de nascimento não pode estar em branco")
    private String dataNascimento;

    private String sexo;

    @Valid
    private EnderecoDto endereco;
}

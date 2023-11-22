package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.text.ParseException;
import java.util.Date;



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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dataNascimento;

    private String sexo;

    @Valid
    private EnderecoDto endereco;

    public static PessoaDto fromEntity(Pessoa pessoa) {
        PessoaDto dto = new PessoaDto();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setTelefone(pessoa.getTelefone());
        dto.setCpf(pessoa.getCpf());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setSexo(pessoa.getSexo());

        if (ValidatorUtil.isNotEmpty(pessoa.getEndereco())) {
            dto.setEndereco(EnderecoDto.fromEntity(pessoa.getEndereco()));
        }

        return dto;
    }

    public static Pessoa toEntity(PessoaDto dto) throws ParseException {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(dto.getId());
        pessoa.setNome(dto.getNome());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setCpf(dto.getCpf());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setSexo(dto.getSexo());

        if (ValidatorUtil.isNotEmpty(dto.getEndereco())) {
            pessoa.setEndereco(EnderecoDto.toEntity(dto.getEndereco()));
        }
        return pessoa;
    }
}

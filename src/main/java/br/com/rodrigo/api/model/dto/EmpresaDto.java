package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    @NotNull(message = "Campo cnpj é requerido")
    @NotBlank(message = "Campo cnpj não pode estar em branco")
    private String cnpj;

    @NotNull(message = "Campo telefone é requerido")
    @NotBlank(message = "Campo telefone não pode estar em branco")
    private String telefone;

    @Valid
    private EnderecoDto endereco;

    public static EmpresaDto fromEntity(Empresa empresa) {
        EmpresaDto dto = new EmpresaDto();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCnpj(empresa.getCnpj());
        dto.setTelefone(empresa.getTelefone());
        dto.setEndereco(EnderecoDto.fromEntity(empresa.getEndereco()));
        return dto;
    }

    public static Empresa toEntity(EmpresaDto dto) {
        Empresa empresa = new Empresa();
        empresa.setId(dto.getId());
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setTelefone(dto.getTelefone());
        empresa.setEndereco(EnderecoDto.toEntity(dto.getEndereco()));
        return empresa;
    }
}

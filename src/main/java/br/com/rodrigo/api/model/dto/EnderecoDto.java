package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Endereco;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class EnderecoDto {

    private Long id;

    private String rua;

    private String bairro;

    private String cidade;

    private String estado;

    @NotNull(message = "Campo Cep é requerido")
    @NotBlank(message = "Campo Cep não pode estar em branco")
    private String cep;

    @NotNull(message = "Campo Número é requerido")
    @NotBlank(message = "Campo Número não pode estar em branco")
    private String numero;

    public static EnderecoDto fromEntity(Endereco endereco) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(endereco.getId());
        dto.setRua(endereco.getRua());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        dto.setNumero(endereco.getNumero());
        return dto;
    }

    public static Endereco toEntity(EnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setId(dto.getId());
        endereco.setRua(dto.getRua());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        return endereco;
    }
}

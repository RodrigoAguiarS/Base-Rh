package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Empresa;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DadosGeraisUsuarioDto {

    private Long id;

    private String email;

    private Set<String> perfis;

    private PessoaDto pessoa;

    private Long funcionario;

    private LocalDate dataEntrada;

    private LocalDate dataSaida;

    private EmpresaDto empresa;

    private VinculoDto vinculo;

    private CargoDto cargo;

    private ResponsavelDepartamentoDto responsavelDepartamento;

    private boolean ativo;

    public static DadosGeraisUsuarioDto fromEntity(Pessoa pessoa, Funcionario funcionario,
                                                   Usuario usuario,
                                                   ResponsavelDepartamento responsavelDepartamento,
                                                   Empresa empresa) {

        DadosGeraisUsuarioDto dto = new DadosGeraisUsuarioDto();
        dto.setPessoa(PessoaDto.fromEntity(pessoa));
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setAtivo(usuario.getAtivo());
        dto.setPerfis(usuario.getPerfis().stream().map(Perfil::getDescricao).collect(Collectors.toSet()));

        if (ValidatorUtil.isNotEmpty(funcionario)) {
            dto.setFuncionario(funcionario.getId());
            dto.setDataEntrada(funcionario.getDataEntrada());
            dto.setDataSaida(funcionario.getDataSaida());
            dto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
        }

        if (ValidatorUtil.isNotEmpty(empresa)) {
            dto.setEmpresa(EmpresaDto.fromEntity(empresa));
        }

        if (ValidatorUtil.isNotEmpty(funcionario.getVinculo())) {
            dto.setVinculo(VinculoDto.fromEntity(funcionario.getVinculo()));
        }

        if (ValidatorUtil.isNotEmpty(responsavelDepartamento)) {
            dto.setResponsavelDepartamento(ResponsavelDepartamentoDto.fromEntity(responsavelDepartamento));
        }

        return dto;
    }
}


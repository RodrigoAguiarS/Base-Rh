package br.com.rodrigo.api.model;

import br.com.rodrigo.api.util.ValidatorUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "responsavel_departamento")
public class ResponsavelDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsavel_departamento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @OneToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;

    @Column(name = "data_inicio_responsabilidade")
    private LocalDate dataInicioResponsabilidade;

    @Column(name = "data_fim_responsabilidade")
    private LocalDate dataFimResponsabilidade;

    @PrePersist
    public void definirDataFimResponsabilidade() {
        if (ValidatorUtil.isNotEmpty(dataInicioResponsabilidade)) {
            dataFimResponsabilidade = dataInicioResponsabilidade.plusYears(1);
        }
    }
}

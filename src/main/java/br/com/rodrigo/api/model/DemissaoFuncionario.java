package br.com.rodrigo.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "demissao_funcionario")
public class DemissaoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demissao_funcionario")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Funcionario funcionario;

    @Column(name = "data_saida", nullable = false)
    private LocalDate dataSaida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_demissao", nullable = false)
    private TipoDemissao tipoDemissao;

    @Column(name = "motivo", nullable = false)
    private String motivo;

}

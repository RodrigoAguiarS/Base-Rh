package br.com.rodrigo.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "folha_pagamento")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FolhaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_folha_pagamento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_funcinario")
    private Funcionario funcionario;

    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "valor_bruto")
    private BigDecimal valorBruto;

    @Column(name = "valor_liquido")
    private BigDecimal valorLiquido;

    @Column(name = "total_descontos")
    private BigDecimal totalDescontos;

    @Column(name = "desconto_inss")
    private BigDecimal descontoInss;

    @Column(name = "desconto_irrf")
    private BigDecimal descontoIrrf;
}

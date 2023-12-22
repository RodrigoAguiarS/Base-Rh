package br.com.rodrigo.api.model;

import lombok.Getter;

@Getter
public enum TipoDemissao {
    SEM_JUSTA_CAUSA("Sem Justa Causa"),

    COM_JUSTA_CAUSA("Com Justa Causa"),

    PEDIDO_DE_DEMISSAO("Pedido de Demissão"),

    ACORDO_ENTRE_PARTES("Acordo entre Partes"),

    DEMISSAO_CONSENSUAL("Demissão Consensual");

    private final String descricao;

    TipoDemissao(String descricao) {
        this.descricao = descricao;
    }
}
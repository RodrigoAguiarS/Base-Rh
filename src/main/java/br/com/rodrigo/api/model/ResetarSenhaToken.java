package br.com.rodrigo.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resetar_senha_token")
public class ResetarSenhaToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resetar_senha")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "codigo", nullable = false)
    private String uid;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_utilizacao")
    private LocalDateTime dataUtilizacao;

    public ResetarSenhaToken(String email, String uid, boolean ativo) {
        this.email = email;
        this.uid = uid;
        this.ativo = ativo;
        this.dataCriacao = LocalDateTime.now();
    }
}

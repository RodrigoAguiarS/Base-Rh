package br.com.rodrigo.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Perfil {

    ADMIN_GERAL(0, "ROLE_ADMIN_GERAL"),

    OPERADORES(1, "ROLE_OPERADORES");

    @Column(name = "id_perfil")
    private Integer id;

    private String descricao;

    public static Perfil toEnum(Integer id) {
        if (id == null) {
            return null;
        }

        for (Perfil x : Perfil.values()) {
            if (id.equals(x.getId())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Perfil inválido");
    }
    public static Perfil toEnumString(String id) {
        if (id == null) {
            return null;
        }

        for (Perfil x : Perfil.values()) {
            if (id.equals(String.valueOf(x.getId()))) {
                return x;
            }
        }

        throw new IllegalArgumentException("Perfil inválido: " + id);
    }

    public Integer getCod() {
        return id;
    }
    public static Set<Integer> getCodes(Set<Perfil> perfis) {
        return perfis.stream().map(Perfil::getCod).collect(Collectors.toSet());
    }
}

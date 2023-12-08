package br.com.rodrigo.api.model.dto;
import br.com.rodrigo.api.model.RegistroPonto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class RegistroPontoDto {

    private Long id;
    private LocalDate dataRegistro;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private boolean pontoRegistrado = false;
    private String observacoes;

    public static RegistroPontoDto fromEntity(RegistroPonto registroPonto) {
        RegistroPontoDto dto = new RegistroPontoDto();
        dto.setId(registroPonto.getId());
        dto.setDataRegistro(registroPonto.getDataRegistro());
        dto.setHoraEntrada(registroPonto.getHoraEntrada());
        dto.setHoraSaida(registroPonto.getHoraSaida());
        dto.setPontoRegistrado(registroPonto.isPontoRegistrado());
        dto.setObservacoes(registroPonto.getObservacoes());
        return dto;
    }

    public static RegistroPonto toEntity(RegistroPontoDto dto) {
        RegistroPonto registroPonto = new RegistroPonto();
        registroPonto.setId(dto.getId());
        registroPonto.setDataRegistro(dto.getDataRegistro());
        registroPonto.setHoraEntrada(dto.getHoraEntrada());
        registroPonto.setHoraSaida(dto.getHoraSaida());
        registroPonto.setPontoRegistrado(dto.isPontoRegistrado());
        registroPonto.setObservacoes(dto.getObservacoes());
        return registroPonto;
    }
}

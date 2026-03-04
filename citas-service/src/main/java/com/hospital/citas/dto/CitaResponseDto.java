package com.hospital.citas.dto;

import com.hospital.citas.model.EstadoCita;
import java.time.LocalDateTime;

public record CitaResponseDto(
        Long id,
        Long pacienteId,
        Long medicoId,
        LocalDateTime fechaHora,
        EstadoCita estado
) {
}

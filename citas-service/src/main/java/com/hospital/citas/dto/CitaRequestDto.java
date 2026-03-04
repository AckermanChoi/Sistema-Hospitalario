package com.hospital.citas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaRequestDto(
        @NotNull(message = "El pacienteId es obligatorio")
        Long pacienteId,

        @NotNull(message = "El medicoId es obligatorio")
        Long medicoId,

        @NotNull(message = "La fecha y hora es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser futura")
        LocalDateTime fechaHora
) {
}

package com.hospital.medicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MedicoRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "La especialidad es obligatoria")
        String especialidad,

        @NotBlank(message = "El número de colegiado es obligatorio")
        @Pattern(regexp = "^[0-9A-Za-z\\-]{4,30}$", message = "El número de colegiado debe tener entre 4 y 30 caracteres válidos")
        String numeroColegiado
) {
}

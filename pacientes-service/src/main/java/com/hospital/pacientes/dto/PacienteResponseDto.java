package com.hospital.pacientes.dto;

import java.time.LocalDate;

public record PacienteResponseDto(
        Long id,
        String nombre,
        String apellido,
        String dni,
        LocalDate fechaNacimiento
) {
}

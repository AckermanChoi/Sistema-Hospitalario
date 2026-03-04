package com.hospital.medicos.dto;

public record MedicoResponseDto(
        Long id,
        String nombre,
        String especialidad,
        String numeroColegiado
) {
}

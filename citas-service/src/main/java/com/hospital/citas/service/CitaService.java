package com.hospital.citas.service;

import com.hospital.citas.client.HospitalValidationClient;
import com.hospital.citas.dto.CitaRequestDto;
import com.hospital.citas.dto.CitaResponseDto;
import com.hospital.citas.exception.BusinessException;
import com.hospital.citas.exception.ResourceNotFoundException;
import com.hospital.citas.model.Cita;
import com.hospital.citas.model.EstadoCita;
import com.hospital.citas.repository.CitaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final HospitalValidationClient validationClient;

    public CitaService(CitaRepository citaRepository, HospitalValidationClient validationClient) {
        this.citaRepository = citaRepository;
        this.validationClient = validationClient;
    }

    public CitaResponseDto crear(CitaRequestDto requestDto) {
        if (requestDto.fechaHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se puede crear una cita con fecha pasada");
        }

        validationClient.validarPacienteExiste(requestDto.pacienteId());
        validationClient.validarMedicoExiste(requestDto.medicoId());

        Cita cita = Cita.builder()
                .pacienteId(requestDto.pacienteId())
                .medicoId(requestDto.medicoId())
                .fechaHora(requestDto.fechaHora())
                .estado(EstadoCita.PROGRAMADA)
                .build();

        return toResponse(citaRepository.save(cita));
    }

    public List<CitaResponseDto> listar() {
        return citaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CitaResponseDto obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));

        return toResponse(cita);
    }

    public CitaResponseDto cancelar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));

        cita.setEstado(EstadoCita.CANCELADA);
        return toResponse(citaRepository.save(cita));
    }

    private CitaResponseDto toResponse(Cita cita) {
        return new CitaResponseDto(
                cita.getId(),
                cita.getPacienteId(),
                cita.getMedicoId(),
                cita.getFechaHora(),
                cita.getEstado()
        );
    }
}

package com.hospital.pacientes.service;

import com.hospital.pacientes.dto.PacienteRequestDto;
import com.hospital.pacientes.dto.PacienteResponseDto;
import com.hospital.pacientes.exception.DuplicateResourceException;
import com.hospital.pacientes.exception.ResourceNotFoundException;
import com.hospital.pacientes.model.Paciente;
import com.hospital.pacientes.repository.PacienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public PacienteResponseDto crear(PacienteRequestDto requestDto) {
        if (pacienteRepository.existsByDni(requestDto.dni())) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI " + requestDto.dni());
        }

        Paciente paciente = Paciente.builder()
                .nombre(requestDto.nombre())
                .apellido(requestDto.apellido())
                .dni(requestDto.dni())
                .fechaNacimiento(requestDto.fechaNacimiento())
                .build();

        return toResponse(pacienteRepository.save(paciente));
    }

    public List<PacienteResponseDto> listar() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PacienteResponseDto obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));

        return toResponse(paciente);
    }

    public PacienteResponseDto actualizar(Long id, PacienteRequestDto requestDto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));

        if (pacienteRepository.existsByDniAndIdNot(requestDto.dni(), id)) {
            throw new DuplicateResourceException("Ya existe otro paciente con el DNI " + requestDto.dni());
        }

        paciente.setNombre(requestDto.nombre());
        paciente.setApellido(requestDto.apellido());
        paciente.setDni(requestDto.dni());
        paciente.setFechaNacimiento(requestDto.fechaNacimiento());

        return toResponse(pacienteRepository.save(paciente));
    }

    private PacienteResponseDto toResponse(Paciente paciente) {
        return new PacienteResponseDto(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getFechaNacimiento()
        );
    }
}

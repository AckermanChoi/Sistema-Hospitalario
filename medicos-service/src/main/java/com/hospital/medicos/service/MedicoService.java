package com.hospital.medicos.service;

import com.hospital.medicos.dto.MedicoRequestDto;
import com.hospital.medicos.dto.MedicoResponseDto;
import com.hospital.medicos.exception.DuplicateResourceException;
import com.hospital.medicos.exception.ResourceNotFoundException;
import com.hospital.medicos.model.Medico;
import com.hospital.medicos.repository.MedicoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public MedicoResponseDto crear(MedicoRequestDto requestDto) {
        if (medicoRepository.existsByNumeroColegiado(requestDto.numeroColegiado())) {
            throw new DuplicateResourceException("Ya existe un médico con número de colegiado " + requestDto.numeroColegiado());
        }

        Medico medico = Medico.builder()
                .nombre(requestDto.nombre())
                .especialidad(requestDto.especialidad())
                .numeroColegiado(requestDto.numeroColegiado())
                .build();

        return toResponse(medicoRepository.save(medico));
    }

    public List<MedicoResponseDto> listar() {
        return medicoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MedicoResponseDto obtenerPorId(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con id " + id));

        return toResponse(medico);
    }

    public List<MedicoResponseDto> buscarPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidadIgnoreCase(especialidad)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicoResponseDto toResponse(Medico medico) {
        return new MedicoResponseDto(
                medico.getId(),
                medico.getNombre(),
                medico.getEspecialidad(),
                medico.getNumeroColegiado()
        );
    }
}

package com.hospital.pacientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hospital.pacientes.dto.PacienteRequestDto;
import com.hospital.pacientes.dto.PacienteResponseDto;
import com.hospital.pacientes.exception.DuplicateResourceException;
import com.hospital.pacientes.exception.ResourceNotFoundException;
import com.hospital.pacientes.model.Paciente;
import com.hospital.pacientes.repository.PacienteRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void crear_deberiaGuardarPaciente_cuandoNoExisteDni() {
        PacienteRequestDto request = new PacienteRequestDto("Ana", "Lopez", "12345678A", LocalDate.of(1998, 5, 20));
        Paciente pacienteGuardado = Paciente.builder()
                .id(1L)
                .nombre("Ana")
                .apellido("Lopez")
                .dni("12345678A")
                .fechaNacimiento(LocalDate.of(1998, 5, 20))
                .build();

        when(pacienteRepository.existsByDni("12345678A")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        PacienteResponseDto response = pacienteService.crear(request);

        assertEquals(1L, response.id());
        assertEquals("Ana", response.nombre());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void crear_deberiaLanzarExcepcion_cuandoDniDuplicado() {
        PacienteRequestDto request = new PacienteRequestDto("Ana", "Lopez", "12345678A", LocalDate.of(1998, 5, 20));
        when(pacienteRepository.existsByDni("12345678A")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> pacienteService.crear(request));
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.obtenerPorId(99L));
    }

    @Test
    void actualizar_deberiaLanzarExcepcion_cuandoDniDuplicado() {
        Paciente existente = Paciente.builder()
                .id(1L)
                .nombre("Ana")
                .apellido("Lopez")
                .dni("12345678A")
                .fechaNacimiento(LocalDate.of(1998, 5, 20))
                .build();
        PacienteRequestDto request = new PacienteRequestDto("Ana Maria", "Lopez", "98765432B", LocalDate.of(1998, 5, 20));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pacienteRepository.existsByDniAndIdNot("98765432B", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> pacienteService.actualizar(1L, request));
    }
}

package com.hospital.citas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hospital.citas.client.HospitalValidationClient;
import com.hospital.citas.dto.CitaRequestDto;
import com.hospital.citas.dto.CitaResponseDto;
import com.hospital.citas.exception.BusinessException;
import com.hospital.citas.exception.ResourceNotFoundException;
import com.hospital.citas.model.Cita;
import com.hospital.citas.model.EstadoCita;
import com.hospital.citas.repository.CitaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private HospitalValidationClient validationClient;

    @InjectMocks
    private CitaService citaService;

    @Test
    void crear_deberiaLanzarExcepcion_cuandoFechaPasada() {
        CitaRequestDto request = new CitaRequestDto(1L, 1L, LocalDateTime.now().minusDays(1));

        assertThrows(BusinessException.class, () -> citaService.crear(request));
        verify(validationClient, never()).validarPacienteExiste(any());
        verify(validationClient, never()).validarMedicoExiste(any());
    }

    @Test
    void crear_deberiaCrearConEstadoProgramada() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(2);
        CitaRequestDto request = new CitaRequestDto(1L, 1L, fecha);
        Cita citaGuardada = Cita.builder()
                .id(1L)
                .pacienteId(1L)
                .medicoId(1L)
                .fechaHora(fecha)
                .estado(EstadoCita.PROGRAMADA)
                .build();

        when(citaRepository.save(any(Cita.class))).thenReturn(citaGuardada);

        CitaResponseDto response = citaService.crear(request);

        assertEquals(EstadoCita.PROGRAMADA, response.estado());
        assertEquals(1L, response.id());
        verify(validationClient).validarPacienteExiste(1L);
        verify(validationClient).validarMedicoExiste(1L);
    }

    @Test
    void crear_deberiaLanzarExcepcion_cuandoPacienteNoExiste() {
        CitaRequestDto request = new CitaRequestDto(99L, 1L, LocalDateTime.now().plusDays(1));

        org.mockito.Mockito.doThrow(new BusinessException("No existe el paciente"))
                .when(validationClient)
                .validarPacienteExiste(99L);

        assertThrows(BusinessException.class, () -> citaService.crear(request));
    }

    @Test
    void cancelar_deberiaCambiarEstadoACancelada() {
        Cita cita = Cita.builder()
                .id(1L)
                .pacienteId(1L)
                .medicoId(1L)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .estado(EstadoCita.PROGRAMADA)
                .build();

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(i -> i.getArgument(0));

        CitaResponseDto response = citaService.cancelar(1L);

        assertEquals(EstadoCita.CANCELADA, response.estado());
    }

    @Test
    void cancelar_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(citaRepository.findById(500L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> citaService.cancelar(500L));
    }
}

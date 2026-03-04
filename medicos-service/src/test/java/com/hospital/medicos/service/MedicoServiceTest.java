package com.hospital.medicos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hospital.medicos.dto.MedicoRequestDto;
import com.hospital.medicos.dto.MedicoResponseDto;
import com.hospital.medicos.exception.DuplicateResourceException;
import com.hospital.medicos.exception.ResourceNotFoundException;
import com.hospital.medicos.model.Medico;
import com.hospital.medicos.repository.MedicoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private MedicoService medicoService;

    @Test
    void crear_deberiaGuardarMedico_cuandoNoExisteColegiado() {
        MedicoRequestDto request = new MedicoRequestDto("Dr. Perez", "Cardiologia", "COL-1001");
        Medico medicoGuardado = Medico.builder()
                .id(1L)
                .nombre("Dr. Perez")
                .especialidad("Cardiologia")
                .numeroColegiado("COL-1001")
                .build();

        when(medicoRepository.existsByNumeroColegiado("COL-1001")).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoGuardado);

        MedicoResponseDto response = medicoService.crear(request);

        assertEquals(1L, response.id());
        assertEquals("Cardiologia", response.especialidad());
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    void crear_deberiaLanzarExcepcion_cuandoColegiadoDuplicado() {
        MedicoRequestDto request = new MedicoRequestDto("Dr. Perez", "Cardiologia", "COL-1001");
        when(medicoRepository.existsByNumeroColegiado("COL-1001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> medicoService.crear(request));
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(medicoRepository.findById(44L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> medicoService.obtenerPorId(44L));
    }

    @Test
    void buscarPorEspecialidad_deberiaRetornarLista() {
        Medico m1 = Medico.builder().id(1L).nombre("Dr. A").especialidad("Cardiologia").numeroColegiado("COL-1").build();
        Medico m2 = Medico.builder().id(2L).nombre("Dr. B").especialidad("Cardiologia").numeroColegiado("COL-2").build();
        when(medicoRepository.findByEspecialidadIgnoreCase("Cardiologia")).thenReturn(List.of(m1, m2));

        List<MedicoResponseDto> result = medicoService.buscarPorEspecialidad("Cardiologia");

        assertEquals(2, result.size());
        assertEquals("Dr. A", result.get(0).nombre());
    }
}

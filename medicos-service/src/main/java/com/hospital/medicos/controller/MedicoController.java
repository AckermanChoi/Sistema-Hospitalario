package com.hospital.medicos.controller;

import com.hospital.medicos.dto.MedicoRequestDto;
import com.hospital.medicos.dto.MedicoResponseDto;
import com.hospital.medicos.service.MedicoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDto> crear(@Valid @RequestBody MedicoRequestDto requestDto) {
        MedicoResponseDto response = medicoService.crear(requestDto);
        URI location = URI.create("/api/medicos/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDto>> listar(
            @RequestParam(value = "especialidad", required = false) String especialidad) {

        if (especialidad != null && !especialidad.isBlank()) {
            return ResponseEntity.ok(medicoService.buscarPorEspecialidad(especialidad));
        }

        return ResponseEntity.ok(medicoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.obtenerPorId(id));
    }
}

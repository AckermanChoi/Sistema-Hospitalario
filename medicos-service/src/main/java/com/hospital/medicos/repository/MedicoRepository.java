package com.hospital.medicos.repository;

import com.hospital.medicos.model.Medico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByNumeroColegiado(String numeroColegiado);

    boolean existsByNumeroColegiadoAndIdNot(String numeroColegiado, Long id);

    List<Medico> findByEspecialidadIgnoreCase(String especialidad);
}

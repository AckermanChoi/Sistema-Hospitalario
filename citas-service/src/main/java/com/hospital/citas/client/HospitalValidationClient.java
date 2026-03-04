package com.hospital.citas.client;

import com.hospital.citas.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class HospitalValidationClient {

    private final RestTemplate restTemplate;
    private final String pacientesBaseUrl;
    private final String medicosBaseUrl;

    public HospitalValidationClient(RestTemplate restTemplate,
                                    @Value("${external.services.pacientes-base-url:http://localhost:8081}") String pacientesBaseUrl,
                                    @Value("${external.services.medicos-base-url:http://localhost:8082}") String medicosBaseUrl) {
        this.restTemplate = restTemplate;
        this.pacientesBaseUrl = pacientesBaseUrl;
        this.medicosBaseUrl = medicosBaseUrl;
    }

    public void validarPacienteExiste(Long pacienteId) {
        String url = pacientesBaseUrl + "/api/pacientes/" + pacienteId;
        try {
            restTemplate.getForEntity(url, Object.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BusinessException("No existe el paciente con id " + pacienteId);
            }
            throw new BusinessException("No se pudo validar el paciente en este momento");
        } catch (Exception ex) {
            throw new BusinessException("No se pudo validar el paciente en este momento");
        }
    }

    public void validarMedicoExiste(Long medicoId) {
        String url = medicosBaseUrl + "/api/medicos/" + medicoId;
        try {
            restTemplate.getForEntity(url, Object.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BusinessException("No existe el médico con id " + medicoId);
            }
            throw new BusinessException("No se pudo validar el médico en este momento");
        } catch (Exception ex) {
            throw new BusinessException("No se pudo validar el médico en este momento");
        }
    }
}

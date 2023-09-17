package med.voll.api.domain.consulta.validaciones;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;

public class ValidadorHorarioAntecedencia implements ValidadorCancelamientoDeConsultas {

    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DatosCancelamientoConsulta datos) {
        var consulta =  repository.getReferenceById(datos.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getDate()).toHours();

        if (diferenciaEnHoras < 24) {
            throw new ValidationException("Consulta solamente pude ser cancelada con antecedencia minima de 24 horas");
        }
    }


}

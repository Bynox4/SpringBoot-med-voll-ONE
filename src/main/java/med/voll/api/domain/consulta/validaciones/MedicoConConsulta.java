package med.voll.api.domain.consulta.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class MedicoConConsulta implements ValidadorDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosAgendarConsulta datos){

        if (datos.idMedico() == null) {
            return;
        }

        var medicoConConsulta = repository.existsByMedicoIdAndDate(datos.idMedico(), datos.date());

        if (medicoConConsulta) {
            throw new ValidationException("este medico ya tiene una consulta en ese horario");
        }
    }
}

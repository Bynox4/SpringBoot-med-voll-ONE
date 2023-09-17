package med.voll.api.domain.consulta.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class PacienteSinConsulta implements ValidadorDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosAgendarConsulta datos){
        var primerHorario = datos.date().withHour(7);
        var ultimoHorario = datos.date().withHour(18);

        var pacienteConConsulta = repository.existsByPacienteIdAndDateBetween(datos.idPaciente(), primerHorario, ultimoHorario);

        if (pacienteConConsulta) {
            throw new ValidationException("el paciente ya tiene una cita para ese dia");
        }
    }
}

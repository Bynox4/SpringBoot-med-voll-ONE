package med.voll.api.domain.consulta.validaciones;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class HorarioDeAnticipacion implements ValidadorDeConsultas{
    public void validar(DatosAgendarConsulta datos){
        var ahora = LocalDateTime.now();
        var horaDeConsulta = datos.date();

        var diferecniaDe30Min = Duration.between(ahora, horaDeConsulta).toMinutes()<30;

        if (diferecniaDe30Min) {
            throw new ValidationException("Las consultas deben programarse con al menos 30 minutos de anticipación");
        }
    }
}

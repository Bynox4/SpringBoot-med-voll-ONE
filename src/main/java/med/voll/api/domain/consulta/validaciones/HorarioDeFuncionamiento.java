package med.voll.api.domain.consulta.validaciones;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class HorarioDeFuncionamiento implements ValidadorDeConsultas{
    public void validar(DatosAgendarConsulta datos){

        var domingo = DayOfWeek.SUNDAY.equals(datos.date().getDayOfWeek());

        var antesDeApertura = datos.date().getHour()<7;
        var despuesDeCierre = datos.date().getHour()>19;

        if (domingo || antesDeApertura || despuesDeCierre) {
            throw new ValidationException("El horario de atención de la clínica e de lines a sábado de 07:00 a 19:00 horas");
        }
    }
}

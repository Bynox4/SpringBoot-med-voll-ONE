package med.voll.api.domain.medico;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("deberia retornar nulo cuando el medico se enscuentre en consulta con otro paciente en ese horario")
    void testSeleccionarMedicoConEspecialidadEnDateEscenario1() {

        // Given
        var proximoLunes10H = LocalDate.now()
            .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            .atTime(10,0);

        var medico = registrarMedico("Jose", "test@test.com", "123456", Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Antonio", "test1@test.com", "654321");
        registrarConsulta(medico, paciente, proximoLunes10H);

        // When
        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnDate(Especialidad.CARDIOLOGIA, proximoLunes10H);

        // Then
        assertThat(medicoLibre).isNull();
    }

    @Test
    @DisplayName("deberia retornar medico cuando realiza la consulta en la base de datos para ese horario")
    void testSeleccionarMedicoConEspecialidadEnDateEscenario2() {

        // Given
        var proximoLunes10H = LocalDate.now()
            .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            .atTime(10,0);

        var medico = registrarMedico("Jose", "test@test.com", "123456", Especialidad.CARDIOLOGIA);

        // When
        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnDate(Especialidad.CARDIOLOGIA, proximoLunes10H);

        // Then
        assertThat(medicoLibre).isEqualTo(medico);
    }

     private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        em.persist(new Consulta(medico, paciente, fecha));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad) {
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        em.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento) {
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad) {
        return new DatosRegistroMedico(
                nombre,
                email,
                "61999999999",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento) {
        return new DatosRegistroPaciente(
                nombre,
                email,
                "61999999999",
                documento,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                " loca",
                "azul",
                "acapulpo",
                321,
                "12"
        );
    }
}

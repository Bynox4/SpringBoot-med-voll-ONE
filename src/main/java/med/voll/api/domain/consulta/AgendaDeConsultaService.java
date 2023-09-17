package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.consulta.validaciones.ValidadorCancelamientoDeConsultas;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadores;

    List<ValidadorCancelamientoDeConsultas> validadoresCancelamiento;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos){

        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("el id paciente no fue encontrado");
        }

        if (datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())) {
            throw new ValidacionDeIntegridad("el id medico no fue encontrado");
        }

        validadores.forEach(v->v.validar(datos)); // Validaciones

        var paciente = pacienteRepository.findById(datos.idPaciente()).get();

        var medico = seleccionarMedico(datos);

        if (medico==null) {
            throw new ValidacionDeIntegridad("no existen medicos disponible para este horario y especialidad");
        }

        var consulta = new Consulta(medico, paciente, datos.date());

        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelamientoConsulta datos){
        if (!consultaRepository.existsById(datos.idConsulta())) {
            throw new ValidacionDeIntegridad("Id de la Consulta informado no existe!");
        }

        validadoresCancelamiento.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }


    private Medico seleccionarMedico(DatosAgendarConsulta datos){
        
        if (datos.idMedico()!=null) {
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad()==null) {
            throw new ValidacionDeIntegridad("debe selecionar una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnDate(datos.especialidad(),datos.date());
    }
}

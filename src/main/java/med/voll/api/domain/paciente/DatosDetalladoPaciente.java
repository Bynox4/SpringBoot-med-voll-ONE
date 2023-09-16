package med.voll.api.domain.paciente;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosDetalladoPaciente(String nombre, String email, String telefono, String documentoIdentidad, DatosDireccion direccion) { 
    public DatosDetalladoPaciente(Paciente paciente) { 
        this(
            paciente.getNombre(),
            paciente.getEmail(),
            paciente.getTelefono(), 
            paciente.getDocumento(), 
            new DatosDireccion(
                paciente.getDireccion().getCalle(),
                paciente.getDireccion().getDistrito(),
                paciente.getDireccion().getCiudad(),
                paciente.getDireccion().getNumero(),
                paciente.getDireccion().getComplemento()
        ));
    }
} 
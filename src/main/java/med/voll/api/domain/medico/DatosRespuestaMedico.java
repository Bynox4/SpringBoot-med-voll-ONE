package med.voll.api.domain.medico;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRespuestaMedico(
    Long id,
    String nombre,
    String email,
    String telefono,
    String documento,
    DatosDireccion direccion
) {
    public DatosRespuestaMedico(Medico medico){
        this(
            medico.getId(),
            medico.getNombre(),
            medico.getEmail(),
            medico.getTelefono(),
            medico.getDocumento(),
            new DatosDireccion(
                medico.getDireccion().getCalle(),
                medico.getDireccion().getDistrito(),
                medico.getDireccion().getCiudad(),
                medico.getDireccion().getNumero(),
                medico.getDireccion().getComplemento()
            )
        );
    }
}

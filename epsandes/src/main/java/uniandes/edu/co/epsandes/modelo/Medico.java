package uniandes.edu.co.epsandes.modelo;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Document(collection = "medicos")
public class Medico {
    
    @Id
    @Field("numeroDocumento")
    private Long numeroDocumento;
    
    @Field("nombre")
    private String nombre;
    
    @Field("tipoDocumento")
    private String tipoDocumento;
    
    @Field("numeroRegistroMedico")
    private Long numeroRegistroMedico;
    
    @Field("especialidad")
    private String especialidad;
    
    @Field("ipsNit")
    private Long ipsNit;
    
    @Field("serviciosIds")
    private Set<Long> serviciosIds = new HashSet<>();

    // Constructor vacío
    public Medico() {}

    // Constructor con parámetros
    public Medico(Long numeroDocumento, String nombre, String tipoDocumento, 
                 Long numeroRegistroMedico, String especialidad, Long ipsNit, Set<Long> serviciosIds) {
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroRegistroMedico = numeroRegistroMedico;
        this.especialidad = especialidad;
        this.ipsNit = ipsNit;
        this.serviciosIds = serviciosIds;
    }

    // Getters y Setters
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNumeroRegistroMedico() {
        return numeroRegistroMedico;
    }

    public void setNumeroRegistroMedico(Long numeroRegistroMedico) {
        this.numeroRegistroMedico = numeroRegistroMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Long getIpsNit() {
        return ipsNit;
    }

    public void setIpsNit(Long ipsNit) {
        this.ipsNit = ipsNit;
    }

    public Set<Long> getServiciosIds() {
        return serviciosIds;
    }

    public void setServiciosIds(Set<Long> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }
}
package uniandes.edu.co.epsandes.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;

@Document(collection = "prestaciones_servicio")
public class PrestacionServicio implements Serializable {
    
    @Id
    private String id; // MongoDB auto-generated id
    
    @Field("ipsNit")
    private Long ipsNit;
    
    @Field("agendarCitaId")
    private Long agendarCitaId;
    
    @Field("citaRealizada")
    private Boolean citaRealizada;

    // Constructor vacío
    public PrestacionServicio() {
        this.citaRealizada = false;
    }

    // Constructor con parámetros
    public PrestacionServicio(Long ipsNit, Long agendarCitaId, Boolean citaRealizada) {
        this.ipsNit = ipsNit;
        this.agendarCitaId = agendarCitaId;
        this.citaRealizada = citaRealizada;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIpsNit() {
        return ipsNit;
    }

    public void setIpsNit(Long ipsNit) {
        this.ipsNit = ipsNit;
    }

    public Long getAgendarCitaId() {
        return agendarCitaId;
    }    public void setAgendarCitaId(Long agendarCitaId) {
        this.agendarCitaId = agendarCitaId;
    }

    public Boolean getCitaRealizada() {
        return citaRealizada;
    }

    public void setCitaRealizada(Boolean citaRealizada) {
        this.citaRealizada = citaRealizada;
    }
}


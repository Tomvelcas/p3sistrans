package uniandes.edu.co.epsandes.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "citas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AgendarCita {
    
    @Id
    @Field("idCita")
    private Long idCita;
    
    @Field("fechaHora")
    private LocalDateTime fechaHora;
    
    @Field("afiliadoNumeroDocumento")
    private Long afiliadoNumeroDocumento;
    
    @Field("medicoNumeroDocumento")
    private Long medicoNumeroDocumento;
    
    @Field("idOrdenDeServicio")
    private Long idOrdenDeServicio;
    
    @Field("servicioSaludId")
    private Long servicioSaludId;

    // Constructor vacío
    public AgendarCita() {}

    // Constructor con parámetros
    public AgendarCita(Long idCita, LocalDateTime fechaHora, Long afiliadoNumeroDocumento, 
                      Long medicoNumeroDocumento, Long idOrdenDeServicio, 
                      Long servicioSaludId) {
        this.idCita = idCita;
        this.fechaHora = fechaHora;
        this.afiliadoNumeroDocumento = afiliadoNumeroDocumento;
        this.medicoNumeroDocumento = medicoNumeroDocumento;
        this.idOrdenDeServicio = idOrdenDeServicio;
        this.servicioSaludId = servicioSaludId;
    }

    // Getters y Setters
    public Long getIdCita() {
        return idCita;    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Long getAfiliadoNumeroDocumento() {
        return afiliadoNumeroDocumento;
    }

    public void setAfiliadoNumeroDocumento(Long afiliadoNumeroDocumento) {
        this.afiliadoNumeroDocumento = afiliadoNumeroDocumento;
    }

    public Long getMedicoNumeroDocumento() {
        return medicoNumeroDocumento;
    }

    public void setMedicoNumeroDocumento(Long medicoNumeroDocumento) {
        this.medicoNumeroDocumento = medicoNumeroDocumento;
    }

    public Long getIdOrdenDeServicio() {
        return idOrdenDeServicio;
    }

    public void setIdOrdenDeServicio(Long idOrdenDeServicio) {
        this.idOrdenDeServicio = idOrdenDeServicio;
    }

    public Long getServicioSaludId() {
        return servicioSaludId;
    }

    public void setServicioSaludId(Long servicioSaludId) {
        this.servicioSaludId = servicioSaludId;
    }
}
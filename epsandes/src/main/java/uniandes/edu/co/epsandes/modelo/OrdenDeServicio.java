package uniandes.edu.co.epsandes.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "ordenes_servicio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrdenDeServicio {
    
    @Id
    @Field("idOrden")
    private Long idOrden;
    
    @Field("fechaHora")
    private LocalDateTime fechaHora;
    
    @Field("estadoOrden")
    private String estadoOrden;
    
    @Field("medicoNumeroDocumento")
    private Long medicoNumeroDocumento;
    
    @Field("afiliadoNumeroDocumento")
    private Long afiliadoNumeroDocumento;
    
    @Field("serviciosIds")
    private List<Long> serviciosIds = new ArrayList<>();

    // Constructor vacío
    public OrdenDeServicio() {}

    // Constructor con parámetros
    public OrdenDeServicio(Long idOrden, LocalDateTime fechaHora, String estadoOrden,
                          Long medicoNumeroDocumento, Long afiliadoNumeroDocumento) {
        this.idOrden = idOrden;
        this.fechaHora = fechaHora;
        this.estadoOrden = estadoOrden;
        this.medicoNumeroDocumento = medicoNumeroDocumento;
        this.afiliadoNumeroDocumento = afiliadoNumeroDocumento;
    }

    // Getters y Setters
    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public Long getMedicoNumeroDocumento() {
        return medicoNumeroDocumento;
    }

    public void setMedicoNumeroDocumento(Long medicoNumeroDocumento) {
        this.medicoNumeroDocumento = medicoNumeroDocumento;
    }

    public Long getAfiliadoNumeroDocumento() {
        return afiliadoNumeroDocumento;
    }

    public void setAfiliadoNumeroDocumento(Long afiliadoNumeroDocumento) {
        this.afiliadoNumeroDocumento = afiliadoNumeroDocumento;
    }

    public List<Long> getServiciosIds() {
        return serviciosIds;
    }

    public void setServiciosIds(List<Long> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }
}
package uniandes.edu.co.epsandes.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "servicios_salud")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServicioDeSalud {
    
    @Id
    @Field("idServicio")
    private Long idServicio;
    
    @Field("nombre")
    private String nombre;
    
    @Field("descripcion")
    private String descripcion;
    
    @Field("tipo")
    private String tipo;

    // Constructor vacío
    public ServicioDeSalud() {}

    // Constructor con parámetros
    public ServicioDeSalud(Long idServicio, String nombre, String descripcion, String tipo) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
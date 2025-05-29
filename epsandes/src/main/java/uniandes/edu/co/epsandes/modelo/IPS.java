package uniandes.edu.co.epsandes.modelo;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "ips")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IPS {
    
    @Id
    @Field("nit")
    private Long nit;
    
    @Field("nombre")
    private String nombre;
    
    @Field("direccion")
    private String direccion;
    
    @Field("telefono")
    private String telefono;
    
    // Para MongoDB, almacenaremos solo los IDs de las referencias para mejor rendimiento
    @Field("serviciosIds")
    private Set<Long> serviciosIds = new HashSet<>();

    // Constructor vacío
    public IPS() {}

    // Constructor con parámetros
    public IPS(Long nit, String nombre, String direccion, String telefono) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getNit() {
        return nit;
    }

    public void setNit(Long nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Long> getServiciosIds() {
        return serviciosIds;
    }

    public void setServiciosIds(Set<Long> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }
}
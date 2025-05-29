package uniandes.edu.co.epsandes.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "eps")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EPS {
    
    @Id
    @Field("nit")
    private Long nit;
    
    @Field("nombre")
    private String nombre;
    
    @Field("telefono")
    private String telefono;
    
    @Field("direccion")
    private String direccion;
    
    @Field("email")
    private String email;
    
    @Field("tipoEPS")
    private String tipoEPS; // CONTRIBUTIVO, SUBSIDIADO, ESPECIAL
    
    // Referencias a IPS afiliadas almacenadas como IDs para mejor rendimiento
    @Field("ipsIds")
    private Set<Long> ipsIds = new HashSet<>();
    
    // Constructor vacío
    public EPS() {}
    
    // Constructor con parámetros
    public EPS(Long nit, String nombre, String telefono, String direccion, String email, String tipoEPS) {
        this.nit = nit;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.tipoEPS = tipoEPS;
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
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTipoEPS() {
        return tipoEPS;
    }
    
    public void setTipoEPS(String tipoEPS) {
        this.tipoEPS = tipoEPS;
    }
    
    public Set<Long> getIpsIds() {
        return ipsIds;
    }
    
    public void setIpsIds(Set<Long> ipsIds) {
        this.ipsIds = ipsIds;
    }
}

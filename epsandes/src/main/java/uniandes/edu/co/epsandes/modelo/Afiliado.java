package uniandes.edu.co.epsandes.modelo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;

@Document(collection = "afiliados")
public class Afiliado {
    
    @Id
    @Field("numeroDocumento")
    private Long numeroDocumento;
    
    @Field("tipoDocumento")
    private String tipoDocumento;
    
    @Field("nombre")
    private String nombre;
    
    @Field("fechaNacimiento")
    private LocalDate fechaNacimiento;
    
    @Field("direccion")
    private String direccion;
    
    @Field("telefono")
    private String telefono;
    
    @Field("tipoAfiliado")
    private String tipoAfiliado;
    
    @Field("numeroDocumentoContribuyente")
    private Long numeroDocumentoContribuyente;
    
    @Field("parentesco")
    private String parentesco;
    
    // Referencias se manejarán por consultas separadas en MongoDB para mejor rendimiento
    // En lugar de @DBRef que puede causar problemas de rendimiento

    // Constructor vacío
    public Afiliado() {}

    // Constructor con parámetros
    public Afiliado(Long numeroDocumento, String tipoDocumento, String nombre, 
                   LocalDate fechaNacimiento, String direccion, String telefono, 
                   String tipoAfiliado, Long numeroDocumentoContribuyente, String parentesco) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoAfiliado = tipoAfiliado;
        this.numeroDocumentoContribuyente = numeroDocumentoContribuyente;
        this.parentesco = parentesco;
    }

    // Getters y Setters
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public void setTipoAfiliado(String tipoAfiliado) {
        this.tipoAfiliado = tipoAfiliado;
    }

    public Long getNumeroDocumentoContribuyente() {
        return numeroDocumentoContribuyente;
    }

    public void setNumeroDocumentoContribuyente(Long numeroDocumentoContribuyente) {
        this.numeroDocumentoContribuyente = numeroDocumentoContribuyente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }    // Methods for handling relationships are implemented in service layer
    // using separate queries for better MongoDB performance
    
    /**
     * Verifica si este afiliado es un contribuyente (no tiene numeroDocumentoContribuyente)
     */
    public boolean esContribuyente() {
        return this.numeroDocumentoContribuyente == null;
    }
    
    /**
     * Verifica si este afiliado es un beneficiario (tiene numeroDocumentoContribuyente)
     */
    public boolean esBeneficiario() {
        return this.numeroDocumentoContribuyente != null;
    }
}
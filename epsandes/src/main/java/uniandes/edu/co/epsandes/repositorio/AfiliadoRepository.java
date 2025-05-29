package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.Afiliado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfiliadoRepository extends MongoRepository<Afiliado, Long> {
    
    // Buscar afiliados por tipo
    List<Afiliado> findByTipoAfiliado(String tipoAfiliado);
    
    // Buscar beneficiarios de un contribuyente específico
    List<Afiliado> findByNumeroDocumentoContribuyente(Long numeroDocumentoContribuyente);
    
    // Buscar afiliados por nombre
    List<Afiliado> findByNombreContaining(String nombre);
    
    // Buscar afiliados por tipo de documento
    List<Afiliado> findByTipoDocumento(String tipoDocumento);
    
    // Verificar si un afiliado tiene beneficiarios
    @Query("{'numeroDocumentoContribuyente': ?0}")
    List<Afiliado> findBeneficiarios(Long numeroDocumento);
    
    // Método helper para verificar si tiene beneficiarios
    default boolean tieneBeneficiarios(Long numeroDocumento) {
        return !findBeneficiarios(numeroDocumento).isEmpty();
    }
}
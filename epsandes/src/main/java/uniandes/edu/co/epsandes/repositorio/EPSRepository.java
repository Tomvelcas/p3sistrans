package uniandes.edu.co.epsandes.repositorio;

import uniandes.edu.co.epsandes.modelo.EPS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EPSRepository extends MongoRepository<EPS, Long> {
    
    // Buscar EPS por nombre
    Optional<EPS> findByNombre(String nombre);
    
    // Buscar EPS por tipo
    List<EPS> findByTipoEPS(String tipoEPS);
    
    // Buscar EPS que tienen una IPS específica
    @Query("{'ipsIds': ?0}")
    List<EPS> findByIpsId(Long ipsId);
    
    // Verificar si existe una EPS con un NIT específico
    boolean existsByNit(Long nit);
}

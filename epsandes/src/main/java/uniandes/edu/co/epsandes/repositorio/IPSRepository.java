package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.IPS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPSRepository extends MongoRepository<IPS, Long> {
    
    // Buscar IPS por nombre
    Optional<IPS> findByNombre(String nombre);
    
    // Buscar IPS que ofrecen un servicio específico
    @Query("{'serviciosIds': ?0}")
    List<IPS> findByServicioId(Long servicioId);
    
    // Buscar IPS por dirección
    List<IPS> findByDireccionContaining(String direccion);
    
    // Verificar si existe una IPS con un NIT específico
    boolean existsByNit(Long nit);
}
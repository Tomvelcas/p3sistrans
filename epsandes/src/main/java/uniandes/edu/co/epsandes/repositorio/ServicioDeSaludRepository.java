package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServicioDeSaludRepository extends MongoRepository<ServicioDeSalud, Long> {
    
    // Buscar servicios por tipo
    List<ServicioDeSalud> findByTipo(String tipo);
    
    // Buscar servicios por nombre
    List<ServicioDeSalud> findByNombreContaining(String nombre);
    
    // Para los 20 servicios más solicitados, necesitaremos implementar en el servicio
    // ya que MongoDB no tiene JOINs nativos como SQL
    
    // Buscar servicios disponibles en una IPS específica
    // Esto se implementará en el servicio usando consultas separadas
}
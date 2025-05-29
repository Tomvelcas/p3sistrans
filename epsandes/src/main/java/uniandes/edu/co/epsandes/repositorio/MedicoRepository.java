package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.Medico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends MongoRepository<Medico, Long> {
    
    // Buscar médico por número de registro médico
    Optional<Medico> findByNumeroRegistroMedico(Long numeroRegistroMedico);
    
    // Buscar médicos por especialidad
    List<Medico> findByEspecialidad(String especialidad);
    
    // Buscar médicos por IPS
    List<Medico> findByIpsNit(Long nit);
    
    // Buscar médicos que prestan un servicio específico
    @Query("{'serviciosIds': ?0}")
    List<Medico> findByServicioId(Long servicioId);
    
    // Buscar médicos que prestan un servicio específico en una IPS específica
    @Query("{'serviciosIds': ?0, 'ipsNit': ?1}")
    List<Medico> findByServicioIdAndIpsNit(Long servicioId, Long nit);
}
package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.PrestacionServicio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestacionServicioRepository extends MongoRepository<PrestacionServicio, String> {
    
    // Buscar prestaciones por IPS
    List<PrestacionServicio> findByIpsNit(Long nit);
    
    // Buscar prestaciones por estado de realización
    List<PrestacionServicio> findByCitaRealizada(Boolean citaRealizada);
    
    // Verificar si existe una prestación para una cita específica
    boolean existsByAgendarCitaId(Long idCita);
    
    // Buscar prestaciones por cita específica
    List<PrestacionServicio> findByAgendarCitaId(Long agendarCitaId);
    
    // Las consultas complejas con fechas se implementarán en el servicio
    // combinando consultas con AgendarCitaRepository
}
package uniandes.edu.co.epsandes.repositorio;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import uniandes.edu.co.epsandes.modelo.AgendarCita;

@Repository
public interface AgendarCitaRepository extends MongoRepository<AgendarCita, Long> {
    
    // Buscar citas por afiliado
    List<AgendarCita> findByAfiliadoNumeroDocumento(Long numeroDocumento);
    
    // Buscar citas por médico
    List<AgendarCita> findByMedicoNumeroDocumento(Long numeroDocumento);
    
    // Buscar citas por servicio de salud
    List<AgendarCita> findByServicioSaludId(Long idServicio);
    
    // Buscar citas en un rango de fechas
    @Query("{'fechaHora': {$gte: ?0, $lte: ?1}}")
    List<AgendarCita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Verificar si existe una cita para un afiliado en una fecha y hora específica
    boolean existsByAfiliadoNumeroDocumentoAndFechaHora(Long numeroDocumento, LocalDateTime fechaHora);
    
    // Las consultas complejas de disponibilidad se implementarán en el servicio
    // usando MongoDB Aggregation Framework o múltiples consultas
}
package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.OrdenDeServicio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenDeServicioRepository extends MongoRepository<OrdenDeServicio, Long> {
    
    // Buscar órdenes por estado
    List<OrdenDeServicio> findByEstadoOrden(String estadoOrden);
    
    // Buscar órdenes por afiliado
    List<OrdenDeServicio> findByAfiliadoNumeroDocumento(Long numeroDocumento);
    
    // Buscar órdenes por médico
    List<OrdenDeServicio> findByMedicoNumeroDocumento(Long numeroDocumento);
    
    // Buscar órdenes en un rango de fechas
    @Query("{'fechaHora': {$gte: ?0, $lte: ?1}}")
    List<OrdenDeServicio> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar órdenes vigentes de un afiliado
    @Query("{'afiliadoNumeroDocumento': ?0, 'estadoOrden': 'VIGENTE'}")
    List<OrdenDeServicio> findOrdenesVigentesByAfiliado(Long numeroDocumento);
}
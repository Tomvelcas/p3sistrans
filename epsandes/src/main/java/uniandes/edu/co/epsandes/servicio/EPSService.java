package uniandes.edu.co.epsandes.servicio;

import uniandes.edu.co.epsandes.modelo.EPS;
import uniandes.edu.co.epsandes.repositorio.EPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class EPSService {

    private static final Logger logger = LoggerFactory.getLogger(EPSService.class);

    @Autowired
    private EPSRepository epsRepository;

    // RF1 - Registrar EPS
    public EPS registrarEPS(EPS eps) {
        logger.debug("Intentando registrar EPS: {}", eps);
        
        // Verificar si ya existe una EPS con el mismo NIT
        if (epsRepository.existsByNit(eps.getNit())) {
            logger.warn("Ya existe una EPS con el NIT: {}", eps.getNit());
            throw new RuntimeException("Ya existe una EPS con el NIT: " + eps.getNit());
        }
        
        // Validar tipo de EPS
        if (!"CONTRIBUTIVO".equals(eps.getTipoEPS()) && 
            !"SUBSIDIADO".equals(eps.getTipoEPS()) && 
            !"ESPECIAL".equals(eps.getTipoEPS())) {
            logger.warn("Tipo de EPS no válido: {}", eps.getTipoEPS());
            throw new RuntimeException("Tipo de EPS no válido. Debe ser CONTRIBUTIVO, SUBSIDIADO o ESPECIAL");
        }
        
        EPS saved = epsRepository.save(eps);
        logger.info("EPS registrada exitosamente: {}", saved);
        return saved;
    }

    // Obtener todas las EPS
    public List<EPS> obtenerTodasEPS() {
        logger.debug("Obteniendo todas las EPS");
        return epsRepository.findAll();
    }

    // Obtener EPS por NIT
    public Optional<EPS> obtenerEPSPorNit(Long nit) {
        logger.debug("Buscando EPS con NIT: {}", nit);
        return epsRepository.findById(nit);
    }

    // Obtener EPS por nombre
    public Optional<EPS> obtenerEPSPorNombre(String nombre) {
        logger.debug("Buscando EPS con nombre: {}", nombre);
        return epsRepository.findByNombre(nombre);
    }

    // Obtener EPS por tipo
    public List<EPS> obtenerEPSPorTipo(String tipoEPS) {
        logger.debug("Buscando EPS de tipo: {}", tipoEPS);
        return epsRepository.findByTipoEPS(tipoEPS);
    }

    // Actualizar EPS
    public EPS actualizarEPS(Long nit, EPS epsActualizada) {
        logger.debug("Intentando actualizar EPS con NIT: {}", nit);
        
        EPS eps = epsRepository.findById(nit)
                .orElseThrow(() -> {
                    logger.warn("EPS no encontrada con NIT: {}", nit);
                    return new RuntimeException("EPS no encontrada con NIT: " + nit);
                });

        eps.setNombre(epsActualizada.getNombre());
        eps.setTelefono(epsActualizada.getTelefono());
        eps.setDireccion(epsActualizada.getDireccion());
        eps.setEmail(epsActualizada.getEmail());
        
        // No permitir cambiar el tipo de EPS una vez creada
        if (!eps.getTipoEPS().equals(epsActualizada.getTipoEPS())) {
            logger.warn("No se permite cambiar el tipo de EPS");
            throw new RuntimeException("No se permite cambiar el tipo de EPS");
        }

        EPS updated = epsRepository.save(eps);
        logger.info("EPS actualizada exitosamente: {}", updated);
        return updated;
    }

    // Eliminar EPS
    public void eliminarEPS(Long nit) {
        logger.debug("Intentando eliminar EPS con NIT: {}", nit);
        
        if (!epsRepository.existsById(nit)) {
            logger.warn("EPS no encontrada con NIT: {}", nit);
            throw new RuntimeException("EPS no encontrada con NIT: " + nit);
        }
        
        epsRepository.deleteById(nit);
        logger.info("EPS eliminada exitosamente con NIT: {}", nit);
    }

    // Obtener EPS que tienen una IPS específica
    public List<EPS> obtenerEPSPorIPS(Long ipsId) {
        logger.debug("Buscando EPS que tienen la IPS con ID: {}", ipsId);
        return epsRepository.findByIpsId(ipsId);
    }

    // Asignar IPS a EPS
    public EPS asignarIPSAEPS(Long epsNit, Long ipsId) {
        logger.debug("Asignando IPS {} a EPS {}", ipsId, epsNit);
        
        EPS eps = epsRepository.findById(epsNit)
                .orElseThrow(() -> {
                    logger.warn("EPS no encontrada con NIT: {}", epsNit);
                    return new RuntimeException("EPS no encontrada con NIT: " + epsNit);
                });
        
        eps.getIpsIds().add(ipsId);
        EPS updated = epsRepository.save(eps);
        logger.info("IPS {} asignada exitosamente a EPS {}", ipsId, epsNit);
        return updated;
    }

    // Desasignar IPS de EPS
    public EPS desasignarIPSDeEPS(Long epsNit, Long ipsId) {
        logger.debug("Desasignando IPS {} de EPS {}", ipsId, epsNit);
        
        EPS eps = epsRepository.findById(epsNit)
                .orElseThrow(() -> {
                    logger.warn("EPS no encontrada con NIT: {}", epsNit);
                    return new RuntimeException("EPS no encontrada con NIT: " + epsNit);
                });
        
        eps.getIpsIds().remove(ipsId);
        EPS updated = epsRepository.save(eps);
        logger.info("IPS {} desasignada exitosamente de EPS {}", ipsId, epsNit);
        return updated;
    }
}

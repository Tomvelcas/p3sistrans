package uniandes.edu.co.epsandes.servicio;

import uniandes.edu.co.epsandes.repositorio.IPSRepository;
import uniandes.edu.co.epsandes.modelo.Medico;
import uniandes.edu.co.epsandes.repositorio.MedicoRepository;
import uniandes.edu.co.epsandes.repositorio.ServicioDeSaludRepository;
import uniandes.edu.co.epsandes.repositorio.AgendarCitaRepository;
import uniandes.edu.co.epsandes.repositorio.OrdenDeServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private IPSRepository ipsRepository;

    @Autowired
    private ServicioDeSaludRepository servicioRepository;

    @Autowired
    private AgendarCitaRepository citaRepository;

    @Autowired
    private OrdenDeServicioRepository ordenRepository;    // RF4 - Registrar médico
    public Medico registrarMedico(Medico medico, List<Long> serviciosIds) {
        logger.debug("Intentando registrar Medico: {} con servicios: {}", medico, serviciosIds);
        
        // Verificar si ya existe un médico con el mismo número de documento
        if (medicoRepository.existsById(medico.getNumeroDocumento())) {
            logger.warn("Ya existe un médico con el número de documento: {}", medico.getNumeroDocumento());
            throw new RuntimeException("Ya existe un médico con el número de documento: " + medico.getNumeroDocumento());
        }

        // Verificar si ya existe un médico con el mismo número de registro médico
        Optional<Medico> medicoExistente = medicoRepository.findByNumeroRegistroMedico(medico.getNumeroRegistroMedico());
        if (medicoExistente.isPresent()) {
            logger.warn("Ya existe un médico con el número de registro médico: {}", medico.getNumeroRegistroMedico());
            throw new RuntimeException("Ya existe un médico con el número de registro médico: " + medico.getNumeroRegistroMedico());
        }        // Verificar que la IPS existe
        if (medico.getIpsNit() == null) {
            logger.warn("No se proporcionó información de IPS para el médico");
            throw new RuntimeException("Debe proporcionar la IPS a la que pertenece el médico");
        }
        
        ipsRepository.findById(medico.getIpsNit())
                .orElseThrow(() -> {
                    logger.warn("IPS no encontrada con NIT: {}", medico.getIpsNit());
                    return new RuntimeException("IPS no encontrada con NIT: " + medico.getIpsNit());
                });
        
        // Inicializar el conjunto de servicios si es null
        if (medico.getServiciosIds() == null) {
            medico.setServiciosIds(new HashSet<>());
        }
        
        // Guardar el médico primero
        Medico saved = medicoRepository.save(medico);
          // Procesar los servicios si se proporcionaron
        if (serviciosIds != null && !serviciosIds.isEmpty()) {
            for (Long servicioId : serviciosIds) {                if (servicioId != null) {
                    // Verificar que el servicio existe
                    servicioRepository.findById(servicioId)
                        .orElseThrow(() -> {
                            logger.warn("Servicio no encontrado con ID: {}", servicioId);
                            return new RuntimeException("Servicio no encontrado con ID: " + servicioId);
                        });
                    // Agregar el servicio ID al médico
                    saved.getServiciosIds().add(servicioId);
                }
            }
            // Guardar el médico nuevamente con los servicios actualizados
            saved = medicoRepository.save(saved);
        }
        
        logger.info("Médico registrado exitosamente con sus servicios: {}", saved);
        return saved;
    }    // Asignar servicio a médico
    public void asignarServicioAMedico(Long medicoDocumento, Long servicioId) {
        logger.debug("Asignando servicio {} a médico {}", servicioId, medicoDocumento);
        
        Medico medico = medicoRepository.findById(medicoDocumento)
                .orElseThrow(() -> {
                    logger.warn("Médico no encontrado con documento: {}", medicoDocumento);
                    return new RuntimeException("Médico no encontrado con documento: " + medicoDocumento);
                });

        // Verificar que el servicio existe
        servicioRepository.findById(servicioId)
                .orElseThrow(() -> {
                    logger.warn("Servicio no encontrado con ID: {}", servicioId);
                    return new RuntimeException("Servicio no encontrado con ID: " + servicioId);
                });

        // Verificar si ya tiene asignado el servicio
        if (medico.getServiciosIds().contains(servicioId)) {
            logger.warn("El médico ya tiene asignado este servicio");
            throw new RuntimeException("El médico ya tiene asignado este servicio");
        }

        medico.getServiciosIds().add(servicioId);
        medicoRepository.save(medico);
        logger.info("Servicio {} asignado a médico {}", servicioId, medicoDocumento);
    }

    // Obtener todos los médicos
    public List<Medico> obtenerTodosMedicos() {
        return medicoRepository.findAll();
    }

    // Obtener médico por número de documento
    public Optional<Medico> obtenerMedicoPorDocumento(Long numeroDocumento) {
        return medicoRepository.findById(numeroDocumento);
    }

    // Obtener médico por número de registro médico
    public Optional<Medico> obtenerMedicoPorRegistroMedico(Long numeroRegistroMedico) {
        return medicoRepository.findByNumeroRegistroMedico(numeroRegistroMedico);
    }

    // Obtener médicos por especialidad
    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    // Obtener médicos por IPS
    public List<Medico> obtenerMedicosPorIPS(Long nit) {
        return medicoRepository.findByIpsNit(nit);
    }

    // Obtener médicos que prestan un servicio específico
    public List<Medico> obtenerMedicosPorServicio(Long servicioId) {
        return medicoRepository.findByServicioId(servicioId);
    }    // Actualizar médico
    public Medico actualizarMedico(Long numeroDocumento, Medico medicoActualizado) {
        Medico medico = medicoRepository.findById(numeroDocumento)
                .orElseThrow(() -> {
                    logger.warn("Médico no encontrado con documento: {}", numeroDocumento);
                    return new RuntimeException("Médico no encontrado con documento: " + numeroDocumento);
                });

        // Actualizar campos básicos
        medico.setNombre(medicoActualizado.getNombre());
        medico.setTipoDocumento(medicoActualizado.getTipoDocumento());
        medico.setEspecialidad(medicoActualizado.getEspecialidad());

        // Actualizar IPS si se proporciona una diferente
        if (medicoActualizado.getIpsNit() != null && 
            !medico.getIpsNit().equals(medicoActualizado.getIpsNit())) {
            
            // Verificar que la nueva IPS existe
            ipsRepository.findById(medicoActualizado.getIpsNit())
                    .orElseThrow(() -> {
                        logger.warn("IPS no encontrada con NIT: {}", medicoActualizado.getIpsNit());
                        return new RuntimeException("IPS no encontrada con NIT: " + medicoActualizado.getIpsNit());
                    });
            medico.setIpsNit(medicoActualizado.getIpsNit());
        }

        return medicoRepository.save(medico);
    }    // Eliminar médico
    public void eliminarMedico(Long numeroDocumento) {
        if (!medicoRepository.existsById(numeroDocumento)) {
            logger.warn("Médico no encontrado con documento: {}", numeroDocumento);
            throw new RuntimeException("Médico no encontrado con documento: " + numeroDocumento);
        }
        
        // En MongoDB, solo verificamos mediante las consultas en los repositories correspondientes
        // Verificar si tiene citas usando el repository
        boolean tieneCitas = !citaRepository.findByMedicoNumeroDocumento(numeroDocumento).isEmpty();
        if (tieneCitas) {
            logger.warn("No se puede eliminar un médico que tiene citas asignadas");
            throw new RuntimeException("No se puede eliminar un médico que tiene citas asignadas");
        }
        
        // Verificar si tiene órdenes usando el repository
        boolean tieneOrdenes = !ordenRepository.findByMedicoNumeroDocumento(numeroDocumento).isEmpty();
        if (tieneOrdenes) {
            logger.warn("No se puede eliminar un médico que ha generado órdenes de servicio");
            throw new RuntimeException("No se puede eliminar un médico que ha generado órdenes de servicio");
        }
        
        // En MongoDB, al usar referencias por ID no necesitamos limpiar relaciones bidireccionales
        medicoRepository.deleteById(numeroDocumento);
        logger.info("Médico eliminado exitosamente con documento: {}", numeroDocumento);
    }
}
package uniandes.edu.co.epsandes.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import uniandes.edu.co.epsandes.modelo.AgendarCita;


import uniandes.edu.co.epsandes.modelo.OrdenDeServicio;
import uniandes.edu.co.epsandes.modelo.PrestacionServicio;
import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import uniandes.edu.co.epsandes.repositorio.AfiliadoRepository;
import uniandes.edu.co.epsandes.repositorio.AgendarCitaRepository;
import uniandes.edu.co.epsandes.repositorio.IPSRepository;
import uniandes.edu.co.epsandes.repositorio.MedicoRepository;
import uniandes.edu.co.epsandes.repositorio.OrdenDeServicioRepository;
import uniandes.edu.co.epsandes.repositorio.PrestacionServicioRepository;
import uniandes.edu.co.epsandes.repositorio.ServicioDeSaludRepository;

@Service
public class AgendarCitaService {
    
    private static final Logger logger = LoggerFactory.getLogger(AgendarCitaService.class);

    @Autowired
    private AgendarCitaRepository citaRepository;
    
    @Autowired
    private AfiliadoRepository afiliadoRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private ServicioDeSaludRepository servicioRepository;
    
    @Autowired
    private OrdenDeServicioRepository ordenRepository;
    
    @Autowired
    private PrestacionServicioRepository prestacionRepository;
    
    @Autowired
    private IPSRepository ipsRepository;
      // RF7.1 - Consultar la agenda de disponibilidad de un servicio
    public List<Object[]> consultarDisponibilidadServicio(Long servicioId) {
        // TODO: Implementar usando MongoDB aggregation framework
        logger.warn("Método consultarDisponibilidadServicio requiere implementación con MongoDB aggregation");
        return new java.util.ArrayList<>();
    }
      // RF7.2 - Agendar un servicio de salud
    public AgendarCita agendarServicio(AgendarCita cita) {
        logger.debug("Intentando agendar cita: {}", cita);
        // Verificar si ya existe una cita con el mismo ID
        if (citaRepository.existsById(cita.getIdCita())) {
            logger.warn("Ya existe una cita con el ID: {}", cita.getIdCita());
            throw new RuntimeException("Ya existe una cita con el ID: " + cita.getIdCita());
        }
          // Verificar que el afiliado existe
        afiliadoRepository.findById(cita.getAfiliadoNumeroDocumento())
                .orElseThrow(() -> new RuntimeException("Afiliado no encontrado con documento: " + 
                        cita.getAfiliadoNumeroDocumento()));
        
        // Verificar que el médico existe
        medicoRepository.findById(cita.getMedicoNumeroDocumento())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con documento: " + 
                        cita.getMedicoNumeroDocumento()));
        
        // Verificar que el servicio existe
        ServicioDeSalud servicio = servicioRepository.findById(cita.getServicioSaludId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + 
                        cita.getServicioSaludId()));
        
        // Verificar si el servicio requiere orden
        if (!servicio.getTipo().equals("CONSULTA_GENERAL") && !servicio.getTipo().equals("CONSULTA_URGENCIAS")) {
            if (cita.getIdOrdenDeServicio() == null) {
                logger.warn("Este servicio requiere una orden de servicio");
                throw new RuntimeException("Este servicio requiere una orden de servicio");
            }
            
            OrdenDeServicio orden = ordenRepository.findById(cita.getIdOrdenDeServicio())
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + 
                            cita.getIdOrdenDeServicio()));
            
            if (!orden.getEstadoOrden().equals("VIGENTE")) {
                logger.warn("La orden debe estar vigente para agendar una cita");
                throw new RuntimeException("La orden debe estar vigente para agendar una cita");
            }
        }
        
        // Verificar que no exista otra cita para el mismo afiliado en la misma fecha y hora
        if (citaRepository.existsByAfiliadoNumeroDocumentoAndFechaHora(
                cita.getAfiliadoNumeroDocumento(), cita.getFechaHora())) {
            logger.warn("El afiliado ya tiene una cita agendada para esta fecha y hora");
            throw new RuntimeException("El afiliado ya tiene una cita agendada para esta fecha y hora");
        }
        
        AgendarCita saved = citaRepository.save(cita);
        logger.info("Cita agendada exitosamente: {}", saved);
        return saved;
    }
      // RF8 - Registrar la prestación de un servicio de salud
    public PrestacionServicio registrarPrestacionServicio(Long citaId, Long ipsNit) {
        // Verificar que la cita existe
        AgendarCita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + citaId));
          // Verificar que la IPS existe
        ipsRepository.findById(ipsNit)
                .orElseThrow(() -> new RuntimeException("IPS no encontrada con NIT: " + ipsNit));
          // Verificar que no exista ya una prestación para esta cita
        if (prestacionRepository.existsByAgendarCitaId(citaId)) {
            throw new RuntimeException("Ya existe una prestación de servicio para esta cita");
        }
        
        PrestacionServicio prestacion = new PrestacionServicio();
        prestacion.setAgendarCitaId(citaId);
        prestacion.setIpsNit(ipsNit);
        prestacion.setCitaRealizada(true);
        
        // Si la cita tiene orden asociada, actualizar el estado de la orden a completada
        if (cita.getIdOrdenDeServicio() != null) {
            OrdenDeServicio orden = ordenRepository.findById(cita.getIdOrdenDeServicio())
                    .orElse(null);
            if (orden != null) {
                orden.setEstadoOrden("COMPLETADA");
                ordenRepository.save(orden);
            }
        }
        
        return prestacionRepository.save(prestacion);
    }
      //RF9 - Agendar un servicio de salud por afiliado- RF7 transaccional
    public AgendarCita agendarServicioPorAfiliado(AgendarCita cita) {
        Long servicioId = cita.getServicioSaludId();
        List<Object[]> disponibilidad = consultarDisponibilidadServicio(servicioId);
        
        if (disponibilidad.isEmpty()) {
            logger.warn("No hay disponibilidad para el servicio con ID: {}", servicioId);
            throw new RuntimeException("No hay disponibilidad para el servicio con ID: " + servicioId);
        }

        // Obtener solo la fecha y hora de la cita solicitada (ignorando minutos y segundos)
        String fechaCitaStr = cita.getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        
        boolean fechaDisponible = disponibilidad.stream()
            .map(obj -> ((String) obj[2]).substring(0, 13)) // Obtener solo año-mes-día hora
            .anyMatch(fecha -> fecha.equals(fechaCitaStr));

        if (!fechaDisponible) {
            logger.warn("La fecha {} no está disponible para el servicio {}. Las citas deben ser al inicio de cada hora", 
                    cita.getFechaHora(), servicioId);
            throw new RuntimeException("La fecha solicitada no está disponible. Por favor seleccione una hora disponible (al inicio de cada hora, XX:00:00)");
        }

        // Ajustar la hora de la cita al inicio de la hora (minuto 00, segundo 00)
        LocalDateTime horaAjustada = cita.getFechaHora()
            .withMinute(0)
            .withSecond(0)
            .withNano(0);
        cita.setFechaHora(horaAjustada);

        return agendarServicio(cita);
    }

    // Obtener todas las citas
    public List<AgendarCita> obtenerTodasCitas() {
        return citaRepository.findAll();
    }
    
    // Obtener cita por ID
    public Optional<AgendarCita> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }
    
    // Obtener citas por afiliado
    public List<AgendarCita> obtenerCitasPorAfiliado(Long numeroDocumento) {
        return citaRepository.findByAfiliadoNumeroDocumento(numeroDocumento);
    }
    
    // Obtener citas por médico
    public List<AgendarCita> obtenerCitasPorMedico(Long numeroDocumento) {
        return citaRepository.findByMedicoNumeroDocumento(numeroDocumento);
    }
      // Eliminar cita
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    // TODO: Los siguientes métodos requieren implementación con MongoDB aggregation framework
    public List<Object[]> DisponibilidadServicio(Long servicioId, Long medicoId, String fechaInicio, String fechaFin) {
        logger.warn("Método DisponibilidadServicio no completamente implementado para MongoDB");
        return new java.util.ArrayList<>();
    }

    public List<Object[]> DisponibilidadServicioRead(Long servicioId, Long medicoId, String fechaInicio, String fechaFin) {
        logger.warn("Método DisponibilidadServicioRead no completamente implementado para MongoDB");
        return new java.util.ArrayList<>();
    }
}
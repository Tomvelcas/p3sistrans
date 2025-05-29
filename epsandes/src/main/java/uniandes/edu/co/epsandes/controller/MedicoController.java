package uniandes.edu.co.epsandes.controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import uniandes.edu.co.epsandes.modelo.Medico;
import uniandes.edu.co.epsandes.servicio.MedicoService;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    @Autowired
    private MedicoService medicoService;

    // RF4 - Registrar médico
   @PostMapping
public ResponseEntity<Medico> registrarMedico(@RequestBody Map<String, Object> request) {
    try {
        ObjectMapper mapper = new ObjectMapper();

        // Extraer y convertir la lista de servicios
        @SuppressWarnings("unchecked")
        List<Integer> serviciosIntegers = (List<Integer>) request.get("servicios");

        if (serviciosIntegers == null) {
            logger.warn("El campo 'servicios' está ausente o mal formado en la petición JSON");
            return ResponseEntity.badRequest()
                .header("X-Error", "El campo 'servicios' es obligatorio y debe ser una lista")
                .build();
        }

        List<Long> serviciosIds = serviciosIntegers.stream()
            .map(Integer::longValue)
            .collect(Collectors.toList());

        // Eliminar 'servicios' del mapa antes de convertir
        request.remove("servicios");

        // Convertir el resto del mapa a un objeto Medico
        Medico medico = mapper.convertValue(request, Medico.class);

        logger.debug("POST /api/medicos - Medico: {}, Servicios: {}", medico, serviciosIds);

        if (medico.getIpsNit() == null) {
            logger.warn("La propiedad IPS está ausente en la petición JSON");
            return ResponseEntity.badRequest()
                .header("X-Error", "El campo 'ipsNit' es obligatorio")
                .build();
        }

        Medico nuevoMedico = medicoService.registrarMedico(medico, serviciosIds);
        logger.info("Médico creado: {}", nuevoMedico);
        return new ResponseEntity<>(nuevoMedico, HttpStatus.CREATED);

    } catch (IllegalArgumentException | ClassCastException e) {
        logger.error("Error al procesar la petición: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
            .header("X-Error", "Error en el formato de la petición: " + e.getMessage())
            .build();
    } catch (RuntimeException e) {
        logger.error("Error al registrar médico: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
            .header("X-Error", e.getMessage())
            .build();
    }
}


    // Obtener todos los médicos
    @GetMapping
    public ResponseEntity<List<Medico>> obtenerTodosMedicos() {
        List<Medico> medicos = medicoService.obtenerTodosMedicos();
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }

    // Obtener médico por número de documento
    @GetMapping("/{numeroDocumento}")
    public ResponseEntity<Medico> obtenerMedicoPorDocumento(@PathVariable Long numeroDocumento) {
        try {
            return medicoService.obtenerMedicoPorDocumento(numeroDocumento)
                    .map(medico -> new ResponseEntity<>(medico, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener médicos por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Medico>> obtenerMedicosPorEspecialidad(@PathVariable String especialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosPorEspecialidad(especialidad);
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }

    // Obtener médicos por IPS
    @GetMapping("/ips/{nit}")
    public ResponseEntity<List<Medico>> obtenerMedicosPorIPS(@PathVariable Long nit) {
        List<Medico> medicos = medicoService.obtenerMedicosPorIPS(nit);
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }

    // Obtener médicos que prestan un servicio específico
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<Medico>> obtenerMedicosPorServicio(@PathVariable Long servicioId) {
        List<Medico> medicos = medicoService.obtenerMedicosPorServicio(servicioId);
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }

    // Actualizar médico
    @PutMapping("/{numeroDocumento}")
    public ResponseEntity<Medico> actualizarMedico(
            @PathVariable Long numeroDocumento,
            @RequestBody Medico medico) {
        try {
            if (!medicoService.obtenerMedicoPorDocumento(numeroDocumento).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Medico medicoActualizado = medicoService.actualizarMedico(numeroDocumento, medico);
            return new ResponseEntity<>(medicoActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar médico
    @DeleteMapping("/{numeroDocumento}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long numeroDocumento) {
        try {
            medicoService.eliminarMedico(numeroDocumento);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
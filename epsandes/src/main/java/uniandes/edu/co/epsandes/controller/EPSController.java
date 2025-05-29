package uniandes.edu.co.epsandes.controller;

import uniandes.edu.co.epsandes.modelo.EPS;
import uniandes.edu.co.epsandes.servicio.EPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eps")
public class EPSController {

    @Autowired
    private EPSService epsService;

    // Registrar nueva EPS
    @PostMapping
    public ResponseEntity<EPS> registrarEPS(@RequestBody EPS eps) {
        try {
            EPS nuevaEPS = epsService.registrarEPS(eps);
            return new ResponseEntity<>(nuevaEPS, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todas las EPS
    @GetMapping
    public ResponseEntity<List<EPS>> obtenerTodasEPS() {
        try {
            List<EPS> eps = epsService.obtenerTodasEPS();
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener EPS por NIT
    @GetMapping("/{nit}")
    public ResponseEntity<EPS> obtenerEPSPorNit(@PathVariable Long nit) {
        try {
            return epsService.obtenerEPSPorNit(nit)
                    .map(eps -> new ResponseEntity<>(eps, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener EPS por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EPS> obtenerEPSPorNombre(@PathVariable String nombre) {
        try {
            return epsService.obtenerEPSPorNombre(nombre)
                    .map(eps -> new ResponseEntity<>(eps, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener EPS por tipo
    @GetMapping("/tipo/{tipoEPS}")
    public ResponseEntity<List<EPS>> obtenerEPSPorTipo(@PathVariable String tipoEPS) {
        try {
            List<EPS> eps = epsService.obtenerEPSPorTipo(tipoEPS);
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar EPS
    @PutMapping("/{nit}")
    public ResponseEntity<EPS> actualizarEPS(@PathVariable Long nit, @RequestBody EPS epsActualizada) {
        try {
            EPS eps = epsService.actualizarEPS(nit, epsActualizada);
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar EPS
    @DeleteMapping("/{nit}")
    public ResponseEntity<HttpStatus> eliminarEPS(@PathVariable Long nit) {
        try {
            epsService.eliminarEPS(nit);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener EPS que tienen una IPS específica
    @GetMapping("/ips/{ipsId}")
    public ResponseEntity<List<EPS>> obtenerEPSPorIPS(@PathVariable Long ipsId) {
        try {
            List<EPS> eps = epsService.obtenerEPSPorIPS(ipsId);
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Asignar IPS a EPS
    @PostMapping("/{epsNit}/ips/{ipsId}")
    public ResponseEntity<EPS> asignarIPSAEPS(@PathVariable Long epsNit, @PathVariable Long ipsId) {
        try {
            EPS eps = epsService.asignarIPSAEPS(epsNit, ipsId);
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Desasignar IPS de EPS
    @DeleteMapping("/{epsNit}/ips/{ipsId}")
    public ResponseEntity<EPS> desasignarIPSDeEPS(@PathVariable Long epsNit, @PathVariable Long ipsId) {
        try {
            EPS eps = epsService.desasignarIPSDeEPS(epsNit, ipsId);
            return new ResponseEntity<>(eps, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package mingeso.proyecto.autofix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.services.ReparacionService;

@RestController
@RequestMapping("/reparaciones")
public class ReparacionController
{
	private final ReparacionService reparacionService;

	@Autowired
	public ReparacionController(ReparacionService reparacionService) {
		this.reparacionService = reparacionService;
	}

	@GetMapping
	public ResponseEntity<List<Reparacion>> getAllReparaciones() {
		List<Reparacion> reparaciones = reparacionService.getAllReparaciones();
		return ResponseEntity.ok(reparaciones);
	}

	@GetMapping("/tipos")
	public ResponseEntity<List<Reparacion.Tipo>> getAllTipoReparaciones(
		@RequestParam(required = true) Long ordenId
	) {
		try{
			List<Reparacion.Tipo> reparaciones = reparacionService.getAllTipoReparaciones(ordenId);
			return ResponseEntity.ok(reparaciones);
		}
		catch(Exception err){
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reparacion> getReparacionById(@PathVariable Long id) {
		Reparacion reparacion = reparacionService.getReparacionById(id);
		if (reparacion != null) {
			return ResponseEntity.ok(reparacion);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Orden> createReparacion(@RequestBody Reparacion reparacion) {
		try{
			Orden orden = reparacionService.createReparacion(reparacion);
			return ResponseEntity.status(HttpStatus.CREATED).body(orden);
		}
		catch(Exception err){
			err.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Reparacion> updateReparacion(@PathVariable Long id, @RequestBody Reparacion updatedReparacion) {
		Reparacion updatedEntity = reparacionService.updateReparacion(updatedReparacion);
		if (updatedEntity != null) {
			return ResponseEntity.ok(updatedEntity);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReparacion(@PathVariable Long id) {
		reparacionService.deleteReparacion(id);
		return ResponseEntity.noContent().build();
	}
}

package mingeso.proyecto.autofix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	public ResponseEntity<Reparacion> createReparacion(@RequestBody Reparacion reparacion) {
		Reparacion createdReparacion = reparacionService.createReparacion(reparacion);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdReparacion);
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<Reparacion> updateReparacion(@PathVariable Long id, @RequestBody Reparacion updatedReparacion) {
		Reparacion updatedEntity = reparacionService.updateReparacion(id, updatedReparacion);
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

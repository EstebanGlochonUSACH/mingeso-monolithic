package mingeso.proyecto.autofix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.services.OrdenService;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController
{
	private final OrdenService ordenService;

	@Autowired
	public OrdenController(OrdenService ordenService) {
		this.ordenService = ordenService;
	}

	@GetMapping
	public ResponseEntity<List<Orden>> getAllOrdenes() {
		List<Orden> ordenes = ordenService.getAllOrdenes();
		return ResponseEntity.ok(ordenes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Orden> getOrdenById(@PathVariable Long id) {
		Orden orden = ordenService.getOrdenById(id);
		if (orden != null) {
			return ResponseEntity.ok(orden);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Orden> createOrden(@RequestBody Orden orden) {
		Orden createdOrden = ordenService.createOrden(orden);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrden);
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<Orden> updateOrden(@PathVariable Long id, @RequestBody Orden updatedOrden) {
		Orden updatedEntity = ordenService.updateOrden(id, updatedOrden);
		if (updatedEntity != null) {
			return ResponseEntity.ok(updatedEntity);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
		ordenService.deleteOrden(id);
		return ResponseEntity.noContent().build();
	}
}

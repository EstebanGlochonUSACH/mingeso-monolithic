package mingeso.proyecto.autofix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.models.ResponseObject;
import mingeso.proyecto.autofix.services.OrdenService;

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
	public ResponseEntity<ResponseObject<Orden>> createOrden(@RequestBody Orden orden) {
		try{
			Orden createdOrden = ordenService.createOrden(orden);
			ResponseObject<Orden> response = new ResponseObject<Orden>(null, createdOrden);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		catch(Exception err){
			ResponseObject<Orden> response = new ResponseObject<Orden>(err.getMessage(), null);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<ResponseObject<Orden>> updateOrden(@PathVariable Long id, @RequestBody Orden updatedOrden) {
		try{
			Orden updatedEntity = ordenService.updateOrden(updatedOrden);
			if (updatedEntity != null) {
				ResponseObject<Orden> response = new ResponseObject<Orden>(null, updatedEntity);
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.notFound().build();
			}
		}
		catch(Exception err){
			ResponseObject<Orden> response = new ResponseObject<Orden>(err.getMessage(), null);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
		ordenService.deleteOrden(id);
		return ResponseEntity.noContent().build();
	}
}

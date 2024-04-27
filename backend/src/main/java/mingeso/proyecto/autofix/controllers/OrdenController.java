package mingeso.proyecto.autofix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.models.ResponseObject;
import mingeso.proyecto.autofix.services.AutoService;
import mingeso.proyecto.autofix.services.OrdenService;

@RestController
@RequestMapping("/ordenes")
public class OrdenController
{
	private final OrdenService ordenService;
	private final AutoService autoService;

	@Autowired
	public OrdenController(OrdenService ordenService, AutoService autoService) {
		this.ordenService = ordenService;
		this.autoService = autoService;
	}

	@GetMapping
	public ResponseEntity<Page<Orden>> getAllOrdenes(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "100") int limit,
		@RequestParam(required = false) Long auto,
		@RequestParam(required = false) String patente
	) {
		if(auto != null){
			Auto autoEntity = autoService.getAutoById(auto);
			Pageable pageable = PageRequest.of(page, limit);
			Page<Orden> ordenes = ordenService.getAllOrdenes(autoEntity, pageable);
			return ResponseEntity.ok(ordenes);
		}
		else if(patente != null){
			Pageable pageable = PageRequest.of(page, limit);
			Page<Orden> ordenes = ordenService.getAllOrdenesByPatente(patente, pageable);
			return ResponseEntity.ok(ordenes);
		}
		else{
			Pageable pageable = PageRequest.of(page, limit);
			Page<Orden> ordenes = ordenService.getAllOrdenes(null, pageable);
			return ResponseEntity.ok(ordenes);
		}
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

	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject<Orden>> updateOrden(@PathVariable Long id, @RequestBody Orden updatedOrden) {
		try{
			Integer totalReparaciones = updatedOrden.getReparaciones().size();
			Orden updatedEntity = ordenService.updateOrden(updatedOrden, totalReparaciones);
			if (updatedEntity != null) {
				ResponseObject<Orden> response = new ResponseObject<Orden>(null, updatedEntity);
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.notFound().build();
			}
		}
		catch(Exception err){
			err.printStackTrace();
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

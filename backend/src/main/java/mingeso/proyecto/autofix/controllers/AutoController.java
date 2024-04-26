package mingeso.proyecto.autofix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.services.AutoService;

@RestController
@RequestMapping("/autos")
public class AutoController
{
	private final AutoService autoService;

	@Autowired
	public AutoController(AutoService autoService) {
		this.autoService = autoService;
	}

	@GetMapping
	public ResponseEntity<Page<Auto>> getAllAutos(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "100") int limit,
		@RequestParam(required = false) String patente
	) {
		Pageable pageable = PageRequest.of(page, limit);
		Page<Auto> result = autoService.getAllAutos(patente, pageable);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Auto> getAutoById(@PathVariable Long id) {
		Auto auto = autoService.getAutoById(id);
		if (auto != null) {
			return ResponseEntity.ok(auto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Auto> createAuto(@RequestBody Auto auto) {
		Auto savedAuto = autoService.saveAuto(auto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAuto);
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<Auto> updateAuto(@PathVariable Long id, @RequestBody Auto updatedAuto) {
		Auto updatedEntity = autoService.updateAuto(id, updatedAuto);
		if (updatedEntity != null) {
			return ResponseEntity.ok(updatedEntity);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAuto(@PathVariable Long id) {
		autoService.deleteAuto(id);
		return ResponseEntity.noContent().build();
	}
}

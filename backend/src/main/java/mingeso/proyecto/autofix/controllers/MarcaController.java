package mingeso.proyecto.autofix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.models.MarcaDTO;
import mingeso.proyecto.autofix.services.MarcaService;

@RestController
@RequestMapping("/marcas")
public class MarcaController
{
	private final MarcaService marcaService;

	@Autowired
	public MarcaController(MarcaService marcaService) {
		this.marcaService = marcaService;
	}

	@GetMapping
	public ResponseEntity<List<MarcaDTO>> getAllMarcas() {
		List<MarcaDTO> marcas = marcaService.getAllMarcas();
		return ResponseEntity.ok(marcas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Marca> getMarcaById(@PathVariable Long id) {
		Marca marca = marcaService.getMarcaById(id);
		if (marca != null) {
			return ResponseEntity.ok(marca);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Marca> createMarca(@RequestBody MarcaDTO marcaDTO) {
		String nombre = marcaDTO.getNombre(); 
		Marca marca = marcaService.createMarca(nombre);
		return ResponseEntity.status(HttpStatus.CREATED).body(marca);
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<Marca> updateMarca(@PathVariable Long id, @RequestParam String nombre) {
		Marca updatedMarca = marcaService.updateMarca(id, nombre);
		if (updatedMarca != null) {
			return ResponseEntity.ok(updatedMarca);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMarca(@PathVariable Long id) {
		marcaService.deleteMarca(id);
		return ResponseEntity.noContent().build();
	}
}

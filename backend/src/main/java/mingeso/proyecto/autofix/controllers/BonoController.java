package mingeso.proyecto.autofix.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.services.BonoService;
import mingeso.proyecto.autofix.services.MarcaService;

@RestController
@RequestMapping("/bonos")
public class BonoController
{
	private final BonoService bonoService;
	private final MarcaService marcaService;

	@Autowired
	public BonoController(BonoService bonoService, MarcaService marcaService) {
		this.bonoService = bonoService;
		this.marcaService = marcaService;
	}

	@GetMapping
	public ResponseEntity<List<Bono>> getAllBonos() {
		List<Bono> bonos = bonoService.getAllBonos();
		return ResponseEntity.ok(bonos);
	}

	@PostMapping("/create")
	@Transactional
	public ResponseEntity<List<Bono>> createBono(@RequestParam Long marcaId, @RequestParam Integer monto, @RequestParam Integer cantidad) {
		Marca marca = marcaService.getMarcaById(marcaId);
		if (marca != null) {
			List<Bono> bonos = new ArrayList<>();
			for(int i = 0; i < cantidad; ++i){
				Bono bono = bonoService.createBono(marca, monto);
				bonos.add(bono);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(bonos);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<Bono> updateBono(@PathVariable Long id, @RequestParam Boolean usado) {
		Bono updatedBono = bonoService.updateBono(id, usado);
		if (updatedBono != null) {
			return ResponseEntity.ok(updatedBono);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

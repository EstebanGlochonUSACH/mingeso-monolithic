package mingeso.proyecto.autofix.controllers;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO;
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
	public ResponseEntity<List<Bono>> getAllBonos(
        @RequestParam(required = false) Long marcaId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha
	) {
		if(marcaId != null){
			Marca marca = marcaService.getMarcaById(marcaId);
			List<Bono> bonos = bonoService.getFilteredBono(marca, fecha);
			return ResponseEntity.ok(bonos);
		}
		else{
			List<Bono> bonos = bonoService.getAllBonos();
			return ResponseEntity.ok(bonos);
		}
	}

	@GetMapping("/grouped-by-fecha-inicio")
    public List<BonoGroupedByFechaInicioDTO> getBonosGroupedByFechaInicio() {
        return bonoService.getAllBonosByGroup();
    }

	@PostMapping("/create")
	@Transactional
	public ResponseEntity<List<Bono>> createBono(@RequestParam Long marcaId, @RequestParam Integer monto, @RequestParam Integer cantidad) {
		Marca marca = marcaService.getMarcaById(marcaId);
		if (marca != null) {
			List<Bono> bonos = bonoService.createBonos(marca, monto, cantidad, null);
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

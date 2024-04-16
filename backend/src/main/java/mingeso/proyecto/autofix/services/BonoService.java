package mingeso.proyecto.autofix.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.repositories.BonoRepository;

@Service
public class BonoService
{
	private final BonoRepository bonoRepository;

	@Autowired
	public BonoService(BonoRepository bonoRepository) {
		this.bonoRepository = bonoRepository;
	}

	public List<Bono> getAllBonos() {
		return bonoRepository.findAll();
	}

	public Bono createBono(Marca marca, Integer monto) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fechaInicio = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
		LocalDateTime fechaTermino = fechaInicio.plusMonths(1);
		Bono bono = new Bono(marca, monto, fechaInicio, fechaTermino);
		return bonoRepository.save(bono);
	}

	public List<Bono> createBonos(Marca marca, Integer monto, Integer cantidad) {
		List<Bono> bonos = new ArrayList<>();
		Bono bono;
		for(int i = 0; i < cantidad; ++i){
			bono = createBono(marca, monto);
			bonos.add(bono);
		}
		return bonos;
	}

	public Bono updateBono(Long id, Boolean usado) {
		Bono bono = bonoRepository.findById(id).orElse(null);
		if (bono != null) {
			bono.setUsado(usado);
			return bonoRepository.save(bono);
		}
		return null;
	}
}

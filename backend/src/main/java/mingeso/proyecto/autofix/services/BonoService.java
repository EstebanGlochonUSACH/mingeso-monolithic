package mingeso.proyecto.autofix.services;

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
		Bono bono = new Bono(marca, monto);
		return bonoRepository.save(bono);
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

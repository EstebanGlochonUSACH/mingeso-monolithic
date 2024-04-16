package mingeso.proyecto.autofix.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.repositories.ReparacionRepository;

@Service
public class ReparacionService
{
	private final ReparacionRepository reparacionRepository;

	@Autowired
	public ReparacionService(ReparacionRepository reparacionRepository) {
		this.reparacionRepository = reparacionRepository;
	}

	public List<Reparacion> getAllReparaciones() {
		return reparacionRepository.findAll();
	}

	public Reparacion getReparacionById(Long id) {
		return reparacionRepository.findById(id).orElse(null);
	}

	// TODO: hay ciertas reparaciones que no son validas para cierto tipo de autos (MontoReparacionConfig = 0)!

	public Reparacion createReparacion(Reparacion reparacion) {
		return reparacionRepository.save(reparacion);
	}

	public Reparacion updateReparacion(Long id, Reparacion updatedReparacion) {
		Reparacion existingReparacion = reparacionRepository.findById(id).orElse(null);
		if (existingReparacion != null) {
			existingReparacion.setTipo(updatedReparacion.getTipo());
			existingReparacion.setMonto(updatedReparacion.getMonto());
			return reparacionRepository.save(existingReparacion);
		}
		return null;
	}

	public void deleteReparacion(Long id) {
		reparacionRepository.deleteById(id);
	}
}

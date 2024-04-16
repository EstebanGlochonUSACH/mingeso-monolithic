package mingeso.proyecto.autofix.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.repositories.AutoRepository;

@Service
public class AutoService
{
	private final AutoRepository autoRepository;

	@Autowired
	public AutoService(AutoRepository autoRepository) {
		this.autoRepository = autoRepository;
	}

	public List<Auto> getAllAutos() {
		return autoRepository.findAll();
	}

	public Auto getAutoById(Long id) {
		return autoRepository.findById(id).orElse(null);
	}

	public Auto getAutoByPatente(String patente) {
		return autoRepository.findByPatente(patente).orElse(null);
	}

	public Boolean existsByPatente(String patente) {
		return autoRepository.existsByPatente(patente);
	}

	public Auto saveAuto(Auto auto) {
		return autoRepository.save(auto);
	}

	public Auto updateAuto(Long id, Auto updatedAuto) {
		Auto existingAuto = autoRepository.findById(id).orElse(null);
		if (existingAuto != null) {
			existingAuto.setPatente(updatedAuto.getPatente());
			existingAuto.setMarca(updatedAuto.getMarca());
			existingAuto.setModelo(updatedAuto.getModelo());
			existingAuto.setTipo(updatedAuto.getTipo());
			existingAuto.setAnio(updatedAuto.getAnio());
			existingAuto.setMotor(updatedAuto.getMotor());
			existingAuto.setAsientos(updatedAuto.getAsientos());
			existingAuto.setKilometraje(updatedAuto.getKilometraje());
			return autoRepository.save(existingAuto);
		}
		return null;
	}

	public void deleteAuto(Long id) {
		autoRepository.deleteById(id);
	}

	public void deleteAutoByPatente(String patente) {
		Auto auto = autoRepository.findByPatente(patente).orElse(null);
		if(auto != null){
			autoRepository.deleteById(auto.getId());
		}
	}
}

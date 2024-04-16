package mingeso.proyecto.autofix.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.repositories.MarcaRepository;

@Service
public class MarcaService
{
	private final MarcaRepository marcaRepository;

	@Autowired
	public MarcaService(MarcaRepository marcaRepository) {
		this.marcaRepository = marcaRepository;
	}

	public List<Marca> getAllMarcas() {
		return marcaRepository.findAll();
	}

	public Marca getMarcaById(Long id) {
		return marcaRepository.findById(id).orElse(null);
	}

	public Marca getMarcaByNombre(String nombre) {
		return marcaRepository.findByNombre(nombre).orElse(null);
	}

	public Marca createMarca(String nombre) {
		Marca marca = new Marca(nombre);
		return marcaRepository.save(marca);
	}

	public Marca getOrCreateMarca(String nombre){
		Marca marca = getMarcaByNombre(nombre);
		if(marca == null){
			return createMarca(nombre);
		}
		return marca;
	}

	public Marca updateMarca(Long id, String nombre) {
		Marca marca = marcaRepository.findById(id).orElse(null);
		if (marca != null) {
			marca.setNombre(nombre);
			return marcaRepository.save(marca);
		}
		return null;
	}

	public void deleteMarca(Long id) {
		marcaRepository.deleteById(id);
	}
}

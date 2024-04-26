package mingeso.proyecto.autofix.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.models.MarcaDTO;
import mingeso.proyecto.autofix.repositories.AutoRepository;
import mingeso.proyecto.autofix.repositories.MarcaRepository;

@Service
public class MarcaService
{
	private final MarcaRepository marcaRepository;
	private final AutoRepository autoRepository;

	@Autowired
	public MarcaService(MarcaRepository marcaRepository, AutoRepository autoRepository) {
		this.marcaRepository = marcaRepository;
		this.autoRepository = autoRepository;
	}

	public List<MarcaDTO> getAllMarcas() {
		List<Marca> marcas = marcaRepository.findAllSorted();
		List<MarcaDTO> marcasDtos = new ArrayList<>();
		MarcaDTO marcaDTO;

		for (Marca marca : marcas) {
			Long totalAutos = autoRepository.countByMarca(marca);
			marcaDTO = new MarcaDTO(marca.getId(), marca.getNombre(), totalAutos);
			marcasDtos.add(marcaDTO);
		}

		return marcasDtos;
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

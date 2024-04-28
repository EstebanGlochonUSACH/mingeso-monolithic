package mingeso.proyecto.autofix.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mingeso.proyecto.autofix.config.MontoReparacionConfig;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.repositories.ReparacionRepository;

@Service
public class ReparacionService
{
	private final ReparacionRepository reparacionRepository;
	private final OrdenService ordenService;

	@Autowired
	public ReparacionService(ReparacionRepository reparacionRepository, OrdenService ordenService) {
		this.reparacionRepository = reparacionRepository;
		this.ordenService = ordenService;
	}

	public List<Reparacion> getAllReparaciones() {
		return reparacionRepository.findAll();
	}

	public List<Reparacion.Tipo> getAllTipoReparaciones(Long ordenId) throws Exception {
		Orden orden = ordenService.getOrdenById(ordenId);
		if(orden == null){
			return null;
		}

		Auto.Motor tipoMotor = orden.getAuto().getMotor();
		List<Reparacion.Tipo> tipos = new ArrayList<>();
		Integer monto;
		
		for(Reparacion.Tipo tipoReparacion : Reparacion.Tipo.values()){
			monto = MontoReparacionConfig.getMonto(tipoMotor, tipoReparacion);
			if(monto > 0){
				tipos.add(tipoReparacion);
			}
		}
		
		return tipos;
	}

	public Reparacion getReparacionById(Long id) {
		return reparacionRepository.findById(id).orElse(null);
	}

	private Orden actualizarOrden(Long ordenId) throws Exception {
		Orden orden = ordenService.getOrdenById(ordenId);
		if(orden == null) return orden;
		Long montoReparaciones = 0L;
		Integer totalReparaciones = 0;
		for(Reparacion rep : orden.getReparaciones()){
			totalReparaciones += 1;
			montoReparaciones += rep.getMonto();
		}
		orden.setMontoReparaciones(montoReparaciones);
		return ordenService.updateOrden(orden, totalReparaciones);
	}

	public Orden createReparacion(Reparacion reparacion) throws Exception {
		// Definir bien el monto
		Orden orden = reparacion.getOrden();
		if(orden == null){
			throw new Exception("La reparacion no tiene \"orden\".");
		}

		Auto auto = orden.getAuto();
		Integer monto = MontoReparacionConfig.getMonto(auto.getMotor(), reparacion.getTipo());
		reparacion.setMonto(monto);
		reparacion = reparacionRepository.save(reparacion);

		return actualizarOrden(orden.getId());
	}

	public Reparacion updateReparacion(Reparacion updatedReparacion) {
		Reparacion existingReparacion = reparacionRepository.findById(updatedReparacion.getId()).orElse(null);
		if (existingReparacion != null) {
			existingReparacion.setTipo(updatedReparacion.getTipo());
			existingReparacion.setMonto(updatedReparacion.getMonto());
			return reparacionRepository.save(existingReparacion);
		}
		return null;
	}

	public Orden deleteReparacion(Long id) throws Exception {
		Reparacion reparacion = reparacionRepository.findById(id).orElse(null);
		if(reparacion == null) return null;

		// Definir bien el monto
		Orden orden = reparacion.getOrden();
		if(orden == null){
			throw new Exception("La reparacion no tiene \"orden\".");
		}

		// Eliminar reparacion
		reparacionRepository.deleteById(id);

		return actualizarOrden(orden.getId());
	}
}

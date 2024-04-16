package mingeso.proyecto.autofix.services;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.repositories.BonoRepository;
import mingeso.proyecto.autofix.repositories.OrdenRepository;

@Service
public class OrdenService
{
	private final OrdenRepository ordenRepository;
	private final BonoRepository bonoRepository;

	@Autowired
	public OrdenService(OrdenRepository ordenRepository, BonoRepository bonoRepository) {
		this.ordenRepository = ordenRepository;
		this.bonoRepository = bonoRepository;
	}

	public List<Orden> getAllOrdenes() {
		return ordenRepository.findAll();
	}

	public Orden getOrdenById(Long id) {
		return ordenRepository.findById(id).orElse(null);
	}

	@Transactional
	public Orden createOrden(Orden orden) throws Exception {
		LocalDateTime now = LocalDateTime.now();

		DayOfWeek dayOfWeek = now.getDayOfWeek();
		boolean isMondayOrThursday = (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.THURSDAY);

		LocalTime time = now.toLocalTime();
		boolean isBetween9And12 = time.isAfter(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(12, 0));

		orden.setDescuentoDiaAtencion(isMondayOrThursday && isBetween9And12);

		Auto auto = orden.getAuto();
		Bono bono = orden.getBono();
		if(bono != null){
			if(!bono.getMarca().equals(auto.getMarca())){
				throw new Exception("No se puede registrar un bono de una Marca distinta a la del auto!");
			}
			else if(bono.getUsado()){
				throw new Exception("No se puede registrar un bono que ya fue canjeado!");
			}
			else{
				bono.setUsado(true);
				bonoRepository.save(bono);
				orden.setBono(bono);
			}
		}

		return ordenRepository.save(orden);
	}

	@Transactional
	public Orden updateOrden(Orden updatedOrden) throws Exception {
		Orden existingOrden = ordenRepository.findById(updatedOrden.getId()).orElse(null);
		if (existingOrden != null) {
			// Validar que el bono no este usado y que la orden no tenga bono
			Auto auto = existingOrden.getAuto();
			Bono bono = updatedOrden.getBono();

			if(bono != null){
				if(!bono.getMarca().equals(auto.getMarca())){
					throw new Exception("No se puede registrar un bono de una Marca distinta a la del auto!");
				}
				else if(existingOrden.getBono() == null){
					if(bono.getUsado()){
						throw new Exception("No se puede registrar un bono que ya fue canjeado!");
					}
					bono.setUsado(true);
					bonoRepository.save(bono);
					existingOrden.setBono(bono);
				}
			}

			existingOrden.setMontoReparaciones(updatedOrden.getMontoReparaciones());

			existingOrden.setDescuentoReparaciones(updatedOrden.getDescuentoReparaciones());
			existingOrden.setRecargaAntiguedad(updatedOrden.getRecargaAntiguedad());
			existingOrden.setRecargaKilometraje(updatedOrden.getRecargaKilometraje());

			existingOrden.setFechaIngreso(updatedOrden.getFechaIngreso());
			existingOrden.setFechaSalida(updatedOrden.getFechaSalida());
			existingOrden.setFechaEntrega(updatedOrden.getFechaEntrega());
			
			return ordenRepository.save(existingOrden);
		}
		return null;
	}

	public void deleteOrden(Long id) {
		ordenRepository.deleteById(id);
	}
}

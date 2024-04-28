package mingeso.proyecto.autofix.repositories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import mingeso.proyecto.autofix.dtos.ReparacionMotorSummary;
import mingeso.proyecto.autofix.dtos.ReparacionTipoSummary;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Marca;

import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
public class ReparacionRepositoryTest {

	@Autowired
	private ReparacionRepository reparacionRepository;

	@Autowired
	private AutoRepository autoRepository;

	@Autowired
	private OrdenRepository ordenRepository;

	@Autowired
	private MarcaRepository marcaRepository;

	@Test
	public void testFindReparacionTipoSummary() {
		// Prepare test data
        Marca marca = new Marca("Toyota");
        marcaRepository.save(marca);

        Auto auto = new Auto("REPAIR123", marca, "Camry", Auto.Tipo.SUV, 2021, Auto.Motor.GASOLINA, 5);
		autoRepository.save(auto);

		Orden orden = new Orden();
		orden.setAuto(auto);
		orden.setFechaIngreso(LocalDateTime.now().minusDays(10));
		orden.setFechaSalida(LocalDateTime.now().minusDays(1));
		ordenRepository.save(orden);

		Reparacion reparacion = new Reparacion();
		reparacion.setOrden(orden);
		reparacion.setTipo(Reparacion.Tipo.AIRE_ACONDICIONADO);
		reparacion.setMonto(100);
		reparacionRepository.save(reparacion);

		// Test the custom query for ReparacionTipoSummary
		List<ReparacionTipoSummary> result = reparacionRepository.findReparacionTipoSummary();
		assertNotNull(result, "The result should not be null");
		assertEquals(1, result.size(), "Should find one ReparacionTipoSummary");
		
		ReparacionTipoSummary summary = result.get(0);
		assertEquals(Reparacion.Tipo.AIRE_ACONDICIONADO, summary.getTipoReparacion(), "Should return the correct tipo");
		assertEquals(Auto.Tipo.SUV, summary.getTipoAuto(), "Should return the correct auto tipo");
		assertEquals(1, summary.getCountVehiculos(), "Should count one distinct auto");
		assertEquals(100, summary.getMontoTotal(), "Should return the correct monto total");
	}

	@Test
	public void testFindReparacionMotorSummary() {
		// Prepare test data
        Marca marca = new Marca("Toyota");
        marcaRepository.save(marca);

        Auto auto = new Auto("XYZ123", marca, "Camry", Auto.Tipo.SUV, 2021, Auto.Motor.ELECTRICO, 5);
		autoRepository.save(auto);

		Orden orden = new Orden();
		orden.setAuto(auto);
		orden.setFechaIngreso(LocalDateTime.now().minusDays(10));
		orden.setFechaSalida(LocalDateTime.now().minusDays(1));
		ordenRepository.save(orden);

		Reparacion reparacion = new Reparacion();
		reparacion.setOrden(orden);
		reparacion.setTipo(Reparacion.Tipo.FRENOS);
		reparacion.setMonto(200);
		reparacionRepository.save(reparacion);

		// Test the custom query for ReparacionMotorSummary
		List<ReparacionMotorSummary> result = reparacionRepository.findReparacionMotorSummary();
		assertNotNull(result, "The result should not be null");
		assertEquals(1, result.size(), "Should find one ReparacionMotorSummary");
		
		ReparacionMotorSummary summary = result.get(0);
		assertEquals(Reparacion.Tipo.FRENOS, summary.getTipoReparacion(), "Should return the correct tipo");
		assertEquals(Auto.Motor.ELECTRICO, summary.getTipoMotor(), "Should return the correct auto motor");
		assertEquals(1, summary.getCountVehiculos(), "Should count one distinct auto");
		assertEquals(200, summary.getMontoTotal(), "Should return the correct monto total");
	}
}

package mingeso.proyecto.autofix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.repositories.ReparacionRepository;

@ActiveProfiles("test")
public class ReparacionServiceTest {

	@Mock
	private ReparacionRepository reparacionRepository;

	@Mock
	private OrdenService ordenService;

	@InjectMocks
	private ReparacionService reparacionService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllReparaciones() {
		List<Reparacion> reparaciones = new ArrayList<>();
		Reparacion reparacion = new Reparacion();
		reparacion.setId(1L);
		reparaciones.add(reparacion);

		when(reparacionRepository.findAll()).thenReturn(reparaciones);

		List<Reparacion> result = reparacionService.getAllReparaciones();

		assertEquals(1, result.size());
		assertEquals(reparacion, result.get(0));
		verify(reparacionRepository, times(1)).findAll();
	}

	@Test
	public void testGetAllTipoReparaciones() throws Exception {
		Orden orden = new Orden();
		orden.setId(1L);
		Auto auto = new Auto();
		auto.setMotor(Auto.Motor.GASOLINA);
		orden.setAuto(auto);

		when(ordenService.getOrdenById(1L)).thenReturn(orden);

		List<Reparacion.Tipo> expectedTipos = Arrays.asList(Reparacion.Tipo.FRENOS, Reparacion.Tipo.MOTOR);

		// Use real method calls if mocking static methods isn't possible
		List<Reparacion.Tipo> result = reparacionService.getAllTipoReparaciones(1L);

		assertNotNull(result);
		assertTrue(result.containsAll(expectedTipos)); // Check expected result
		verify(ordenService, times(1)).getOrdenById(1L); // Verify interaction with ordenService
	}

	@Test
	public void testGetReparacionById() {
		Reparacion expectedReparacion = new Reparacion();
		expectedReparacion.setId(1L);

		when(reparacionRepository.findById(1L)).thenReturn(Optional.of(expectedReparacion));

		Reparacion result = reparacionService.getReparacionById(1L);

		assertNotNull(result);
		assertEquals(expectedReparacion, result);
		verify(reparacionRepository, times(1)).findById(1L);
	}

	@Test
	public void testCreateReparacion_withValidOrden() throws Exception {
		// Test setup
		Orden orden = new Orden();
		orden.setId(1L);
		Auto auto = new Auto();
		auto.setMotor(Auto.Motor.GASOLINA);
		orden.setAuto(auto);

		Reparacion reparacion = new Reparacion();
		reparacion.setOrden(orden);
		reparacion.setTipo(Reparacion.Tipo.MOTOR);

		when(ordenService.getOrdenById(eq(1L))).thenReturn(orden); // Mock expected behavior
		when(reparacionRepository.save(eq(reparacion))).thenReturn(reparacion);
		when(ordenService.updateOrden(any(), anyInt())).thenReturn(orden);

		// Execute the method under test
		Orden result = reparacionService.createReparacion(reparacion);

		// Assertions
		assertNotNull(result); // Ensure result is not null
		assertEquals(1L, result.getId()); // Check if the returned Orden has the expected ID
		verify(reparacionRepository, times(1)).save(eq(reparacion)); // Ensure repository save is called once
		verify(ordenService, times(1)).getOrdenById(eq(1L)); // Verify correct interaction with OrdenService
	}

	@Test
	public void testCreateReparacion_withoutOrden() {
		Reparacion reparacion = new Reparacion();

		Exception exception = assertThrows(Exception.class, () -> {
			reparacionService.createReparacion(reparacion);
		});

		assertEquals("La reparacion no tiene \"orden\".", exception.getMessage());
	}

	@Test
	public void testUpdateReparacion_withExistingReparacion() {
		Reparacion existingReparacion = new Reparacion();
		existingReparacion.setId(1L);

		Reparacion updatedReparacion = new Reparacion();
		updatedReparacion.setId(1L);
		updatedReparacion.setMonto(2000);

		when(reparacionRepository.findById(1L)).thenReturn(Optional.of(existingReparacion));
		when(reparacionRepository.save(existingReparacion)).thenReturn(existingReparacion);

		Reparacion result = reparacionService.updateReparacion(updatedReparacion);

		assertNotNull(result);
		assertEquals(2000, result.getMonto());
		verify(reparacionRepository, times(1)).findById(1L);
		verify(reparacionRepository, times(1)).save(existingReparacion);
	}

	@Test
	public void testDeleteReparacion() throws Exception {
		Reparacion reparacion = new Reparacion();
		reparacion.setId(1L);

		Orden orden = new Orden();
		orden.setId(1L);
		reparacion.setOrden(orden);

		when(reparacionRepository.findById(eq(1L))).thenReturn(Optional.of(reparacion));
		when(ordenService.getOrdenById(eq(1L))).thenReturn(orden);
		when(ordenService.updateOrden(any(), anyInt())).thenReturn(orden);
		doNothing().when(reparacionRepository).deleteById(eq(1L));

		Orden result = reparacionService.deleteReparacion(1L);

		assertNotNull(result);
		verify(reparacionRepository, times(1)).deleteById(eq(1L));
		verify(ordenService, times(1)).updateOrden(eq(orden), anyInt());
	}
}

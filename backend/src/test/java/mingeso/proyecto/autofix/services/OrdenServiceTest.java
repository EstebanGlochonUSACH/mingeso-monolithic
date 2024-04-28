package mingeso.proyecto.autofix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.repositories.BonoRepository;
import mingeso.proyecto.autofix.repositories.OrdenRepository;

@ActiveProfiles("test")
public class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private BonoRepository bonoRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrdenes_withAuto() {
        Pageable pageable = mock(Pageable.class);
        Page<Orden> mockPage = mock(Page.class);
        Auto auto = new Auto();
        auto.setPatente("ABC123");

        when(ordenRepository.findByAuto(auto, pageable)).thenReturn(mockPage);

        Page<Orden> result = ordenService.getAllOrdenes(auto, pageable);

        assertEquals(mockPage, result);
        verify(ordenRepository, times(1)).findByAuto(auto, pageable);
    }

    @Test
    public void testGetAllOrdenes_withoutAuto() {
        Pageable pageable = mock(Pageable.class);
        Page<Orden> mockPage = mock(Page.class);

        when(ordenRepository.findAllSorted(pageable)).thenReturn(mockPage);

        Page<Orden> result = ordenService.getAllOrdenes(null, pageable);

        assertEquals(mockPage, result);
        verify(ordenRepository, times(1)).findAllSorted(pageable);
    }

    @Test
    public void testGetOrdenById() {
        Orden expectedOrden = new Orden();
        expectedOrden.setId(1L);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(expectedOrden));

        Orden result = ordenService.getOrdenById(1L);

        assertNotNull(result);
        assertEquals(expectedOrden, result);
        verify(ordenRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateOrden_Valid() throws Exception {
        Orden orden = new Orden();
        Auto auto = new Auto("ABC123", new Marca("Toyota"), "Camry", Auto.Tipo.SEDAN, 2021, Auto.Motor.GASOLINA, 5);
        orden.setAuto(auto);
        
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden createdOrden = ordenService.createOrden(orden);

        assertNotNull(createdOrden); // The created Orden should not be null
        assertThat(createdOrden.getAuto().getPatente()).isEqualTo("ABC123"); // Check the Auto's patente
        verify(ordenRepository, times(1)).save(orden); // Ensure repository save is called once
    }

    @Test
    public void testCreateOrden_InvalidBonoMarca() throws Exception {
        Orden orden = new Orden();
        Auto auto = new Auto("ABC123", new Marca("Toyota"), "Camry", Auto.Tipo.SEDAN, 2021, Auto.Motor.GASOLINA, 5);
        orden.setAuto(auto);

        Bono bono = new Bono(new Marca("Honda"), 100, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        orden.setBono(bono);

        assertThrows(Exception.class, () -> {
            ordenService.createOrden(orden);
        }, "No se puede registrar un bono de una Marca distinta a la del auto!"); // Expected exception
    }

    @Test
    public void testUpdateOrden_AutoNull() {
        Orden updatedOrden = new Orden();
        updatedOrden.setId(1L);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(updatedOrden));

        assertThrows(Exception.class, () -> {
            ordenService.updateOrden(updatedOrden, 1);
        }, "La orden no tiene auto!"); // Expected exception if Auto is null
    }

    @Test
    public void testCreateOrden_withValidBono() throws Exception {
        Orden orden = new Orden();
        Auto auto = new Auto();
        auto.setPatente("ABC123");
        orden.setAuto(auto);

        Bono bono = new Bono();
        bono.setMarca(auto.getMarca());
        bono.setUsado(false);
        orden.setBono(bono);

        when(bonoRepository.save(bono)).thenReturn(bono);
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden result = ordenService.createOrden(orden);

        assertNotNull(result);
        assertTrue(result.getBono().getUsado());
        verify(bonoRepository, times(1)).save(bono);
        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    public void testCreateOrden_withUsedBono() {
        Orden orden = new Orden();
        Auto auto = new Auto();
        auto.setPatente("ABC123");
        orden.setAuto(auto);

        Bono bono = new Bono();
        bono.setMarca(auto.getMarca());
        bono.setUsado(true);
        orden.setBono(bono);

        assertThrows(Exception.class, () -> {
            ordenService.createOrden(orden);
        }, "No se puede registrar un bono que ya fue canjeado!");
    }

    @Test
    public void testUpdateOrden_withExistingOrden() throws Exception {
        Auto auto = new Auto("ABC123", new Marca("Toyota"), "Camry", Auto.Tipo.SEDAN, 2021, Auto.Motor.GASOLINA, 5);

        Orden existingOrden = new Orden();
        existingOrden.setId(1L);
        existingOrden.setAuto(auto);

        Orden updatedOrden = new Orden();
        updatedOrden.setId(1L);
        updatedOrden.setAuto(auto);
        updatedOrden.setMontoReparaciones(5000L);

        when(ordenRepository.findById(1L)).thenReturn(Optional.of(existingOrden));
        when(ordenRepository.save(existingOrden)).thenReturn(existingOrden);

        Orden result = ordenService.updateOrden(updatedOrden, 1);

        assertNotNull(result);
        assertEquals(5000L, result.getMontoReparaciones());
        verify(ordenRepository, times(1)).findById(1L);
        verify(ordenRepository, times(1)).save(existingOrden);
    }

    @Test
    public void testDeleteOrden() {
        doNothing().when(ordenRepository).deleteById(1L);

        assertDoesNotThrow(() -> ordenService.deleteOrden(1L));
        verify(ordenRepository, times(1)).deleteById(1L);
    }
}

package mingeso.proyecto.autofix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.repositories.BonoRepository;

@ActiveProfiles("test")
public class BonoServiceTest {

    @Mock
    private BonoRepository bonoRepository;

    @InjectMocks
    private BonoService bonoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBonos() {
        List<Bono> bonos = new ArrayList<>();
        bonos.add(new Bono());

        when(bonoRepository.findAll()).thenReturn(bonos);

        List<Bono> result = bonoService.getAllBonos();

        assertEquals(bonos.size(), result.size());
        verify(bonoRepository, times(1)).findAll();
    }

    @Test
    public void testGetFilteredBono_withMarcaAndFecha() {
        Marca marca = new Marca("Toyota");
        LocalDateTime now = LocalDateTime.now();
		LocalDateTime fechaInicio = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
		LocalDateTime fechaTermino = fechaInicio.plusMonths(1);

        List<Bono> bonos = new ArrayList<>();
        bonos.add(new Bono(marca, 1000, fechaInicio, fechaTermino));

        when(bonoRepository.findAllByMarcaAndFecha(marca, now)).thenReturn(bonos);

        List<Bono> result = bonoService.getFilteredBono(marca, now);

        assertEquals(bonos.size(), result.size());
        assertEquals(bonos.get(0).getMarca(), result.get(0).getMarca());
        verify(bonoRepository, times(1)).findAllByMarcaAndFecha(marca, now);
    }

    @Test
    public void testGetFilteredBono_withMarcaOnly() {
        Marca marca = new Marca("Toyota");
        LocalDateTime now = LocalDateTime.now();
		LocalDateTime fechaInicio = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
		LocalDateTime fechaTermino = fechaInicio.plusMonths(1);

        List<Bono> bonos = new ArrayList<>();
        bonos.add(new Bono(marca, 1000, fechaInicio, fechaTermino));

        when(bonoRepository.findAllByMarcaAndFecha(marca, null)).thenReturn(bonos);

        List<Bono> result = bonoService.getFilteredBono(marca, null);

        assertEquals(bonos.size(), result.size());
        verify(bonoRepository, times(1)).findAllByMarcaAndFecha(marca, null);
    }

    @Test
    public void testGetAllBonosByGroup() {
        List<BonoGroupedByFechaInicioDTO> groupedBonos = new ArrayList<>();
        groupedBonos.add(new BonoGroupedByFechaInicioDTO(new Marca("Toyota"), 1000, LocalDateTime.now(), LocalDateTime.now().plusMonths(1), 1L));

        when(bonoRepository.groupByFechaInicio()).thenReturn(groupedBonos);

        List<BonoGroupedByFechaInicioDTO> result = bonoService.getAllBonosByGroup();

        assertEquals(groupedBonos.size(), result.size());
        verify(bonoRepository, times(1)).groupByFechaInicio();
    }

    @Test
    public void testCreateBono() {
        Marca marca = new Marca("Toyota");
        Integer monto = 1000;
        LocalDateTime fecha = LocalDateTime.of(2024, 5, 1, 0, 0);

        Bono expectedBono = new Bono(marca, monto, fecha, fecha.plusMonths(1));

        when(bonoRepository.save(any(Bono.class))).thenReturn(expectedBono);

        Bono result = bonoService.createBono(marca, monto, fecha);

        assertNotNull(result);
        assertEquals(expectedBono, result);
        verify(bonoRepository, times(1)).save(any(Bono.class));
    }

    @Test
    public void testUpdateBono() {
        Bono existingBono = new Bono();
        existingBono.setId(1L);

        when(bonoRepository.findById(1L)).thenReturn(Optional.of(existingBono));
        when(bonoRepository.save(existingBono)).thenReturn(existingBono);

        Bono result = bonoService.updateBono(1L, true);

        assertNotNull(result);
        assertTrue(result.getUsado());
        verify(bonoRepository, times(1)).findById(1L);
        verify(bonoRepository, times(1)).save(existingBono);
    }

    @Test
    public void testCreateBonos() {
        Marca marca = new Marca("Toyota");
        Integer monto = 1000;
        Integer cantidad = 3;

        List<Bono> expectedBonos = new ArrayList<>();
        expectedBonos.add(new Bono(marca, monto, LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
        expectedBonos.add(new Bono(marca, monto, LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
        expectedBonos.add(new Bono(marca, monto, LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));

        when(bonoRepository.save(any(Bono.class))).thenAnswer(invocation -> {
            Bono bono = (Bono) invocation.getArguments()[0];
            expectedBonos.add(bono);
            return bono;
        });

        List<Bono> result = bonoService.createBonos(marca, monto, cantidad, LocalDateTime.now());

        assertEquals(cantidad, result.size());
        assertEquals(expectedBonos.get(0).getMarca(), result.get(0).getMarca());
        verify(bonoRepository, times(3)).save(any(Bono.class));
    }
}

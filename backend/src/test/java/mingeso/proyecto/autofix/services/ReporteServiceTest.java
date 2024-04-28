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
import java.util.List;

import mingeso.proyecto.autofix.dtos.AvgRepairTimeByMarcaDTO;
import mingeso.proyecto.autofix.dtos.ReparacionMotorSummary;
import mingeso.proyecto.autofix.dtos.ReparacionTipoSummary;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.repositories.OrdenRepository;
import mingeso.proyecto.autofix.repositories.ReparacionRepository;

@ActiveProfiles("test")
public class ReporteServiceTest {

    @Mock
    private ReparacionRepository reparacionRepository;

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReparacionTipoSummary() {
        List<ReparacionTipoSummary> summaries = new ArrayList<>();
        ReparacionTipoSummary summary = new ReparacionTipoSummary(Reparacion.Tipo.MOTOR, Auto.Tipo.SEDAN, 5L, 20000L);
        summaries.add(summary);

        when(reparacionRepository.findReparacionTipoSummary()).thenReturn(summaries);

        List<ReparacionTipoSummary> result = reporteService.getReparacionTipoSummary();

        assertNotNull(result);
        assertEquals(summaries, result);
        verify(reparacionRepository, times(1)).findReparacionTipoSummary();
    }

    @Test
    public void testGetReparacionMotorSummary() {
        List<ReparacionMotorSummary> summaries = new ArrayList<>();
        ReparacionMotorSummary summary = new ReparacionMotorSummary(Reparacion.Tipo.FRENOS, Auto.Motor.GASOLINA, 10L, 30000L);
        summaries.add(summary);

        when(reparacionRepository.findReparacionMotorSummary()).thenReturn(summaries);

        List<ReparacionMotorSummary> result = reporteService.getReparacionMotorSummary();

        assertNotNull(result);
        assertEquals(summaries, result);
        verify(reparacionRepository, times(1)).findReparacionMotorSummary();
    }

    @Test
    public void testGetAvgRepairTimeByMarca() {
        List<AvgRepairTimeByMarcaDTO> summaries = new ArrayList<>();
        AvgRepairTimeByMarcaDTO summary = new AvgRepairTimeByMarcaDTO("Toyota", 1200.0);
        summaries.add(summary);

        when(ordenRepository.findAvgRepairTimeByMarca()).thenReturn(summaries);

        List<AvgRepairTimeByMarcaDTO> result = reporteService.getAvgRepairTimeByMarca();

        assertNotNull(result);
        assertEquals(summaries, result);
        verify(ordenRepository, times(1)).findAvgRepairTimeByMarca();
    }
}

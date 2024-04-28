package mingeso.proyecto.autofix.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import mingeso.proyecto.autofix.dtos.AvgRepairTimeByMarcaDTO;
import mingeso.proyecto.autofix.dtos.ReparacionMotorSummary;
import mingeso.proyecto.autofix.dtos.ReparacionTipoSummary;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.services.ReporteService;

@ActiveProfiles("test")
public class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reporteController).build();
    }

    @Test
    public void testGetReparacionSummary() throws Exception {
        List<ReparacionTipoSummary> summary = new ArrayList<>();
        ReparacionTipoSummary reparacionTipoSummary = new ReparacionTipoSummary(Reparacion.Tipo.MOTOR, Auto.Tipo.SEDAN, 5L, 10000L);
        summary.add(reparacionTipoSummary);

        when(reporteService.getReparacionTipoSummary()).thenReturn(summary);

        mockMvc.perform(get("/reportes/tipo-auto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].montoTotal").value(10000L)); // Example assertion for expected data
    }

    @Test
    public void testGetReparacionMotorSummary() throws Exception {
        List<ReparacionMotorSummary> summary = new ArrayList<>();
        ReparacionMotorSummary reparacionMotorSummary = new ReparacionMotorSummary(Reparacion.Tipo.FRENOS, Auto.Motor.GASOLINA, 10L, 20000L);
        summary.add(reparacionMotorSummary);

        when(reporteService.getReparacionMotorSummary()).thenReturn(summary);

        mockMvc.perform(get("/reportes/tipo-motor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].montoTotal").value(20000L)); // Example assertion for expected data
    }

    @Test
    public void testGetAvgRepairTimeByMarca() throws Exception {
        List<AvgRepairTimeByMarcaDTO> summary = new ArrayList<>();
        AvgRepairTimeByMarcaDTO avgRepairTimeByMarca = new AvgRepairTimeByMarcaDTO("Toyota", 1200.0);
        summary.add(avgRepairTimeByMarca);

        when(reporteService.getAvgRepairTimeByMarca()).thenReturn(summary);

        mockMvc.perform(get("/reportes/tiempo-reparacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].avgRepairTime").value(1200.0)); // Example assertion for expected data
    }
}

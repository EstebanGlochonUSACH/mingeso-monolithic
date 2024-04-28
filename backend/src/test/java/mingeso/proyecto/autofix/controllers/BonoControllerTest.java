package mingeso.proyecto.autofix.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.services.BonoService;
import mingeso.proyecto.autofix.services.MarcaService;

@ActiveProfiles("test")
public class BonoControllerTest {

    @Mock
    private BonoService bonoService;

    @Mock
    private MarcaService marcaService;

    @InjectMocks
    private BonoController bonoController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bonoController).build();
    }

    @Test
    public void testGetAllBonos() throws Exception {
        List<Bono> bonos = new ArrayList<>();
        Bono bono = new Bono();
        bonos.add(bono);

        when(bonoService.getAllBonos()).thenReturn(bonos);

        mockMvc.perform(get("/bonos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetAllBonos_withMarcaId() throws Exception {
        Long marcaId = 1L;
        Marca marca = new Marca("Toyota");
        List<Bono> bonos = new ArrayList<>();
        Bono bono = new Bono();
        bonos.add(bono);

        when(marcaService.getMarcaById(marcaId)).thenReturn(marca);
        when(bonoService.getFilteredBono(marca, null)).thenReturn(bonos);

        mockMvc.perform(get("/bonos").param("marcaId", String.valueOf(marcaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetBonosGroupedByFechaInicio() throws Exception {
        List<BonoGroupedByFechaInicioDTO> groupedBonos = new ArrayList<>();
        BonoGroupedByFechaInicioDTO dto = new BonoGroupedByFechaInicioDTO(new Marca("Toyota"), 1000, null, null, 1L);
        groupedBonos.add(dto);

        when(bonoService.getAllBonosByGroup()).thenReturn(groupedBonos);

        mockMvc.perform(get("/bonos/grouped-by-fecha-inicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Transactional
    public void testCreateBono() throws Exception {
        Long marcaId = 1L;
        Marca marca = new Marca("Toyota");
        List<Bono> bonos = new ArrayList<>();
        Bono bono = new Bono();
        bonos.add(bono);

        when(marcaService.getMarcaById(marcaId)).thenReturn(marca);
        when(bonoService.createBonos(marca, 1000, 1, null)).thenReturn(bonos);

        mockMvc.perform(post("/bonos/create")
                        .param("marcaId", String.valueOf(marcaId))
                        .param("monto", "1000")
                        .param("cantidad", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Transactional
    public void testCreateBono_withInvalidMarca() throws Exception {
        Long marcaId = 1L;

        when(marcaService.getMarcaById(marcaId)).thenReturn(null);

        mockMvc.perform(post("/bonos/create")
                        .param("marcaId", String.valueOf(marcaId))
                        .param("monto", "1000")
                        .param("cantidad", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBono() throws Exception {
        Long bonoId = 1L;
        Bono bono = new Bono();
        bono.setId(bonoId);

        when(bonoService.updateBono(bonoId, true)).thenReturn(bono);

        mockMvc.perform(put("/bonos/" + bonoId + "/update")
                        .param("usado", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateBono_notFound() throws Exception {
        Long bonoId = 1L;

        when(bonoService.updateBono(bonoId, true)).thenReturn(null);

        mockMvc.perform(put("/bonos/" + bonoId + "/update")
                        .param("usado", "true"))
                .andExpect(status().isNotFound());
    }
}

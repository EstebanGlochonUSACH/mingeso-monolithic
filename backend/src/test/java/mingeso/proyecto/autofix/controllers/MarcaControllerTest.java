package mingeso.proyecto.autofix.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import mingeso.proyecto.autofix.dtos.MarcaDTO;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.services.MarcaService;

@ActiveProfiles("test")
public class MarcaControllerTest {

    @Mock
    private MarcaService marcaService;

    @InjectMocks
    private MarcaController marcaController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(marcaController).build();
    }

    @Test
    public void testGetAllMarcas() throws Exception {
        List<MarcaDTO> marcas = new ArrayList<>();
        MarcaDTO marcaDTO = new MarcaDTO(1L, "Toyota", 10L);
        marcas.add(marcaDTO);

        when(marcaService.getAllMarcas()).thenReturn(marcas);

        mockMvc.perform(get("/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Toyota"));
    }

    @Test
    public void testGetMarcaById() throws Exception {
        Marca expectedMarca = new Marca("Toyota");
        expectedMarca.setId(1L);

        when(marcaService.getMarcaById(1L)).thenReturn(expectedMarca);

        mockMvc.perform(get("/marcas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Toyota"));
    }

    @Test
    public void testGetMarcaById_notFound() throws Exception {
        when(marcaService.getMarcaById(1L)).thenReturn(null);

        mockMvc.perform(get("/marcas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMarca() throws Exception {
        // MarcaDTO marcaDTO = new MarcaDTO(null, "Toyota", 0L);
        Marca expectedMarca = new Marca("Toyota");
        expectedMarca.setId(1L);

        when(marcaService.createMarca("Toyota")).thenReturn(expectedMarca);

        String marcaJson = String.format("{\"nombre\":\"%s\", \"totalAutos\": %d}", "Toyota", 0L);

        mockMvc.perform(post("/marcas/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(marcaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Toyota"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateMarca() throws Exception {
        // Create an existing Marca to be updated
        Marca existingMarca = new Marca("Toyota");
        existingMarca.setId(1L);

        // Set up the expected updated Marca
        Marca updatedMarca = new Marca("Toyota Updated");
        updatedMarca.setId(1L);

        // Mock the service behavior for finding and updating Marca
        when(marcaService.updateMarca(1L, "Toyota Updated")).thenReturn(updatedMarca);

        // Simulate HTTP PUT request to update Marca
        mockMvc.perform(put("/marcas/1/update")
                .param("nombre", "Toyota Updated")) // HTTP parameters
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.nombre").value("Toyota Updated")) // Check the updated name
                .andExpect(jsonPath("$.id").value(1L)); // Check the ID

        // Verify that the service update method is called once
        verify(marcaService, times(1)).updateMarca(1L, "Toyota Updated");
    }

    @Test
    public void testUpdateMarca_notFound() throws Exception {
        when(marcaService.updateMarca(1L, "Nissan")).thenReturn(null);

        mockMvc.perform(put("/marcas/1/update")
                        .param("nombre", "Nissan"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMarca() throws Exception {
        doNothing().when(marcaService).deleteMarca(1L);

        mockMvc.perform(delete("/marcas/1"))
                .andExpect(status().isNoContent());
    }
}

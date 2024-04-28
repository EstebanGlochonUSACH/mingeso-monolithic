package mingeso.proyecto.autofix.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.services.ReparacionService;

@WebMvcTest(ReparacionController.class)
@ActiveProfiles("test")
public class ReparacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReparacionService reparacionService;

    @Test
    public void testGetAllReparaciones() throws Exception {
        List<Reparacion> reparaciones = new ArrayList<>();
        Reparacion reparacion = new Reparacion();
        reparaciones.add(reparacion);

        when(reparacionService.getAllReparaciones()).thenReturn(reparaciones);

        mockMvc.perform(get("/reparaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetReparacionById() throws Exception {
        Reparacion reparacion = new Reparacion();
        reparacion.setId(1L);

        when(reparacionService.getReparacionById(1L)).thenReturn(reparacion);

        mockMvc.perform(get("/reparaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetReparacionById_notFound() throws Exception {
        when(reparacionService.getReparacionById(1L)).thenReturn(null);

        mockMvc.perform(get("/reparaciones/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTipoReparaciones() throws Exception {
        List<Reparacion.Tipo> tipos = List.of(Reparacion.Tipo.MOTOR, Reparacion.Tipo.FRENOS);

        when(reparacionService.getAllTipoReparaciones(1L)).thenReturn(tipos);

        mockMvc.perform(get("/reparaciones/tipos").param("ordenId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetAllTipoReparaciones_badRequest() throws Exception {
        when(reparacionService.getAllTipoReparaciones(1L)).thenThrow(new Exception("Invalid Order ID"));

        mockMvc.perform(get("/reparaciones/tipos").param("ordenId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReparacion() throws Exception {
        Reparacion reparacion = new Reparacion();
        reparacion.setId(1L);

        Orden orden = new Orden();
        orden.setId(1L);

        when(reparacionService.createReparacion(any(Reparacion.class))).thenReturn(orden);

        String reparacionJson = "{\"id\": 1}";

        mockMvc.perform(post("/reparaciones/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reparacionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateReparacion_badRequest() throws Exception {
        when(reparacionService.createReparacion(any(Reparacion.class))).thenThrow(new Exception("Testing Exception"));

        String reparacionJson = "{\"id\": 1}";

        mockMvc.perform(post("/reparaciones/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reparacionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReparacion() throws Exception {
        Reparacion reparacion = new Reparacion();
        reparacion.setId(1L);

        when(reparacionService.updateReparacion(any(Reparacion.class))).thenReturn(reparacion);

        String reparacionJson = "{\"id\": 1}";

        mockMvc.perform(put("/reparaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reparacionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateReparacion_notFound() throws Exception {
        when(reparacionService.updateReparacion(any(Reparacion.class))).thenReturn(null);

        String reparacionJson = "{\"id\": 1}";

        mockMvc.perform(put("/reparaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reparacionJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteReparacion() throws Exception {
        Long reparacionId = 1L;

        // Prepare the mock response
        Orden expectedOrden = new Orden();
        expectedOrden.setId(1L);

        // Mock the required service interaction
        when(reparacionService.deleteReparacion(reparacionId))
            .thenReturn(expectedOrden);

        // Perform a DELETE request and validate the response
        mockMvc.perform(delete("/reparaciones/{id}", reparacionId)
                .contentType(MediaType.APPLICATION_JSON)) // Content type for the request
            .andExpect(status().isOk()) // Expect HTTP 200 (OK)
            .andExpect(jsonPath("$.id").value(1L));

        // Verify that the service was called once with the expected ID
        verify(reparacionService, times(1)).deleteReparacion(eq(reparacionId));
    }
}

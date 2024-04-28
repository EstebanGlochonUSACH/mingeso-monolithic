package mingeso.proyecto.autofix.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import java.util.Collections;

import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.services.OrdenService;
import mingeso.proyecto.autofix.services.AutoService;

@WebMvcTest(OrdenController.class)
@ActiveProfiles("test")
public class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @MockBean
    private AutoService autoService;

    @Test
    public void testGetAllOrdenes() throws Exception {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Orden> page = new PageImpl<>(Collections.singletonList(new Orden()));

        // Mock the service method to return the expected result
        given(ordenService.getAllOrdenes(null, pageable)).willReturn(page);

        // Simulate HTTP GET request to fetch all Ordenes
        mockMvc.perform(get("/ordenes")
                        .param("page", "0") // Set parameters
                        .param("limit", "100") // Limit
                        .contentType(MediaType.APPLICATION_JSON)) // Correct content type
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.content.length()").value(1)); // Verify the expected response
    }

    @Test
    public void testGetOrdenById() throws Exception {
        Orden orden = new Orden();
        orden.setId(1L);

        when(ordenService.getOrdenById(1L)).thenReturn(orden);

        mockMvc.perform(get("/ordenes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetOrdenById_notFound() throws Exception {
        when(ordenService.getOrdenById(1L)).thenReturn(null);

        mockMvc.perform(get("/ordenes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrden() throws Exception {
        // Create an Orden to be returned by the service
        Orden orden = new Orden();
        orden.setId(1L); // Set the expected ID

        // Mock the service's createOrden method
        when(ordenService.createOrden(any(Orden.class))).thenReturn(orden);

        // Example JSON representation of the request body
        String ordenJson = "{\"id\": 1}";

        // Simulate HTTP POST request to create an Orden
        mockMvc.perform(post("/ordenes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ordenJson)) // Use the JSON content
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.entity.id").value(1L)); // Check if the expected ID is returned
    }

    @Test
    public void testUpdateOrden() throws Exception {
        // Set up the mock service and expected data
        Orden updatedOrden = new Orden();
        updatedOrden.setId(1L); // Expected ID

        // Mock the updateOrden method to return the updated Orden
        when(ordenService.updateOrden(any(Orden.class), anyInt())).thenReturn(updatedOrden);

        // Create JSON to represent the updated Orden request
        String updatedOrdenJson = "{\"id\": 1}";

        // Simulate HTTP PUT request to update the Orden
        mockMvc.perform(put("/ordenes/1")
                        .contentType(MediaType.APPLICATION_JSON) // Ensure correct content type
                        .content(updatedOrdenJson)) // Add the content to the request
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.entity.id").value(1L)); // Verify the expected ID
    }

    @Test
    public void testDeleteOrden() throws Exception {
        doNothing().when(ordenService).deleteOrden(1L);

        mockMvc.perform(delete("/ordenes/1"))
                .andExpect(status().isNoContent());

        verify(ordenService, times(1)).deleteOrden(1L);
    }
}

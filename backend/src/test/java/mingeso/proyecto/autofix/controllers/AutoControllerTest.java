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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

import java.util.Collections;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.services.AutoService;

@WebMvcTest(AutoController.class)
@ActiveProfiles("test")
public class AutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutoService autoService;

    @Test
    public void testGetAllAutos() throws Exception {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Auto> page = new PageImpl<>(Collections.singletonList(new Auto()));

        given(autoService.getAllAutos(null, pageable)).willReturn(page);

        mockMvc.perform(get("/autos")
                        .param("page", "0")
                        .param("limit", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}

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
import java.util.Optional;

import mingeso.proyecto.autofix.dtos.MarcaDTO;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.repositories.AutoRepository;
import mingeso.proyecto.autofix.repositories.MarcaRepository;

@ActiveProfiles("test")
public class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private MarcaService marcaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMarcas() {
        List<Marca> marcas = new ArrayList<>();
        Marca marca1 = new Marca("Toyota");
        Marca marca2 = new Marca("Honda");
        marcas.add(marca1);
        marcas.add(marca2);
    
        // Mock the expected behavior of the repositories
        when(marcaRepository.findAllSorted()).thenReturn(marcas); // Expected sorted list of brands
        when(autoRepository.countByMarca(marca1)).thenReturn(10L); // Expected count for Toyota
        when(autoRepository.countByMarca(marca2)).thenReturn(5L); // Expected count for Honda
    
        // Execute the method under test
        List<MarcaDTO> result = marcaService.getAllMarcas();
    
        // Assertions
        assertEquals(2, result.size()); // Ensure the correct number of brands
        assertEquals("Toyota", result.get(0).getNombre()); // Check the name of the first brand
        assertEquals(10L, result.get(0).getTotalAutos()); // Check the total autos for the first brand
        assertEquals("Honda", result.get(1).getNombre()); // Check the name of the second brand
        assertEquals(5L, result.get(1).getTotalAutos()); // Check the total autos for the second brand
    
        // Verify interactions with mocks
        verify(marcaRepository, times(1)).findAllSorted(); // Verify the repository interaction
        verify(autoRepository, times(1)).countByMarca(marca1); // Verify the count for the first brand
        verify(autoRepository, times(1)).countByMarca(marca2); // Verify the count for the second brand
    }

    @Test
    public void testGetMarcaById() {
        Marca expectedMarca = new Marca("Toyota");
        expectedMarca.setId(1L);

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(expectedMarca));

        Marca result = marcaService.getMarcaById(1L);

        assertNotNull(result);
        assertEquals(expectedMarca, result);
        verify(marcaRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMarcaByNombre() {
        Marca expectedMarca = new Marca("Toyota");

        when(marcaRepository.findByNombre("Toyota")).thenReturn(Optional.of(expectedMarca));

        Marca result = marcaService.getMarcaByNombre("Toyota");

        assertNotNull(result);
        assertEquals(expectedMarca, result);
        verify(marcaRepository, times(1)).findByNombre("Toyota");
    }

    @Test
    public void testCreateMarca() {
        Marca expectedMarca = new Marca("Toyota");

        when(marcaRepository.save(any(Marca.class))).thenReturn(expectedMarca);

        Marca result = marcaService.createMarca("Toyota");

        assertNotNull(result);
        assertEquals(expectedMarca, result);
        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    public void testUpdateMarca() {
        Marca existingMarca = new Marca("Toyota");
        existingMarca.setId(1L);

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(existingMarca));
        when(marcaRepository.save(existingMarca)).thenReturn(existingMarca);

        Marca result = marcaService.updateMarca(1L, "Nissan");

        assertNotNull(result);
        assertEquals("Nissan", result.getNombre());
        verify(marcaRepository, times(1)).findById(1L);
        verify(marcaRepository, times(1)).save(existingMarca);
    }

    @Test
    public void testDeleteMarca() {
        doNothing().when(marcaRepository).deleteById(1L);

        assertDoesNotThrow(() -> marcaService.deleteMarca(1L));
        verify(marcaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetOrCreateMarca() {
        Marca expectedMarca = new Marca("Toyota");

        when(marcaRepository.findByNombre("Toyota")).thenReturn(Optional.of(expectedMarca));
        when(marcaRepository.save(any(Marca.class))).thenReturn(expectedMarca);

        Marca result = marcaService.getOrCreateMarca("Toyota");

        assertNotNull(result);
        assertEquals(expectedMarca, result);
        verify(marcaRepository, times(1)).findByNombre("Toyota");
    }
}

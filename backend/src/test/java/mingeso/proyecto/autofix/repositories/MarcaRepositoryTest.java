package mingeso.proyecto.autofix.repositories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import mingeso.proyecto.autofix.entities.Marca;

@DataJpaTest
@ActiveProfiles("test")
public class MarcaRepositoryTest {

    @Autowired
    private MarcaRepository marcaRepository;

    @Test
    public void testFindByNombre() {
        // Prepare test data
        Marca marca = new Marca();
        marca.setNombre("Toyota");
        marcaRepository.save(marca);

        // Test the findByNombre method
        Optional<Marca> found = marcaRepository.findByNombre("Toyota");
        assertTrue(found.isPresent(), "Marca should be found");
        assertEquals("Toyota", found.get().getNombre());
    }

    @Test
    public void testFindAllSorted() {
        // Prepare test data
        Marca marca1 = new Marca();
        marca1.setNombre("BMW");
        marcaRepository.save(marca1);

        Marca marca2 = new Marca();
        marca2.setNombre("Audi");
        marcaRepository.save(marca2);

        Marca marca3 = new Marca();
        marca3.setNombre("Toyota");
        marcaRepository.save(marca3);

        // Test the findAllSorted method
        List<Marca> sortedMarcas = marcaRepository.findAllSorted();
        assertNotNull(sortedMarcas, "The result should not be null");
        assertEquals(3, sortedMarcas.size(), "Should find all three Marca entities");

        // Check if they are sorted by 'nombre'
        assertEquals("Audi", sortedMarcas.get(0).getNombre(), "First should be Audi");
        assertEquals("BMW", sortedMarcas.get(1).getNombre(), "Second should be BMW");
        assertEquals("Toyota", sortedMarcas.get(2).getNombre(), "Third should be Toyota");
    }
}

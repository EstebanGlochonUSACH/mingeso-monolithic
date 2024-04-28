package mingeso.proyecto.autofix.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Marca;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class AutoRepositoryTest {

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Test
    public void testFindAllMatchPatente() {
        // Prepare data
        Auto auto1 = new Auto();
        auto1.setPatente("ABC123");
        auto1.setKilometraje(1);
        autoRepository.save(auto1);

        Auto auto2 = new Auto();
        auto2.setPatente("XYZ456");
        auto2.setKilometraje(1);
        autoRepository.save(auto2);

        // Test findAllMatchPatente
        Page<Auto> result = autoRepository.findAllMatchPatente("ABC", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("ABC123", result.getContent().get(0).getPatente());
    }

    @Test
    public void testFindByPatente() {
        // Prepare data
        Auto auto = new Auto();
        auto.setPatente("ABC123");
        auto.setKilometraje(1);
        autoRepository.save(auto);

        // Test findByPatente
        Optional<Auto> result = autoRepository.findByPatente("ABC123");
        assertTrue(result.isPresent());
        assertEquals("ABC123", result.get().getPatente());
    }

    @Test
    public void testExistsByPatente() {
        // Prepare data
        Auto auto = new Auto();
        auto.setPatente("ABC123");
        auto.setKilometraje(1);
        autoRepository.save(auto);

        // Test existsByPatente
        assertTrue(autoRepository.existsByPatente("ABC123"));
        assertFalse(autoRepository.existsByPatente("NONEXISTENT"));
    }

    @Test
    public void testCountByMarca() {
        // Prepare data
        Marca marca = new Marca();
        marca.setNombre("Ford");
        marcaRepository.save(marca);

        Auto auto1 = new Auto();
        auto1.setPatente("ABC123");
        auto1.setMarca(marca);
        auto1.setKilometraje(1);
        autoRepository.save(auto1);

        Auto auto2 = new Auto();
        auto2.setPatente("XYZ456");
        auto2.setMarca(marca);
        auto2.setKilometraje(1);
        autoRepository.save(auto2);

        // Test countByMarca
        Long count = autoRepository.countByMarca(marca);
        assertEquals(2, count);
    }
}

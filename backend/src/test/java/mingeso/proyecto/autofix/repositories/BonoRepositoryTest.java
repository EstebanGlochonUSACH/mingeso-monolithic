package mingeso.proyecto.autofix.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class BonoRepositoryTest {

    @Autowired
    private BonoRepository bonoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Test
    public void testGroupByFechaInicio() {
        // Prepare test data
        Marca marca = new Marca();
        marca.setNombre("Toyota");
        marcaRepository.save(marca);

        Bono bono1 = new Bono();
        bono1.setMarca(marca);
        bono1.setMonto(100);
        bono1.setFechaInicio(LocalDateTime.now().minusDays(10));
        bono1.setFechaTermino(LocalDateTime.now().plusDays(10));
        bono1.setUsado(false);
        bonoRepository.save(bono1);

        Bono bono2 = new Bono();
        bono2.setMarca(marca);
        bono2.setMonto(200);
        bono2.setFechaInicio(LocalDateTime.now().minusDays(5));
        bono2.setFechaTermino(LocalDateTime.now().plusDays(15));
        bono2.setUsado(false);
        bonoRepository.save(bono2);

        // Test the groupByFechaInicio method
        List<BonoGroupedByFechaInicioDTO> result = bonoRepository.groupByFechaInicio();
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "Should find two grouped records");
        
        // Check grouping and ordering
        assertTrue(result.get(0).getFechaInicio().isAfter(result.get(1).getFechaInicio()), "Should be ordered by fechaInicio descending");
    }

    @Test
    public void testFindAllByMarca() {
        // Prepare test data
        Marca marca = new Marca();
        marca.setNombre("Ford");
        marcaRepository.save(marca);

        Bono bono = new Bono();
        bono.setMarca(marca);
        bono.setMonto(150);
        bono.setFechaInicio(LocalDateTime.now().minusDays(1));
        bono.setFechaTermino(LocalDateTime.now().plusDays(10));
        bono.setUsado(false);
        bonoRepository.save(bono);

        // Test the findAllByMarca method
        List<Bono> result = bonoRepository.findAllByMarca(marca);
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.size(), "Should find one Bono");
        assertEquals(150, result.get(0).getMonto(), "Bono should have the correct monto");
    }

    @Test
    public void testFindAllByMarcaAndFecha() {
        // Prepare test data
        Marca marca = new Marca();
        marca.setNombre("Audi");
        marcaRepository.save(marca);

        LocalDateTime now = LocalDateTime.now();

        Bono bono = new Bono();
        bono.setMarca(marca);
        bono.setMonto(300);
        bono.setFechaInicio(now.minusDays(1));
        bono.setFechaTermino(now.plusDays(10));
        bono.setUsado(false);
        bonoRepository.save(bono);

        // Test the findAllByMarcaAndFecha method
        List<Bono> result = bonoRepository.findAllByMarcaAndFecha(marca, now);
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.size(), "Should find one Bono");
        assertEquals(300, result.get(0).getMonto(), "Bono should have the correct monto");

        // Test with a date outside the Bono's validity period
        List<Bono> noResult = bonoRepository.findAllByMarcaAndFecha(marca, now.plusDays(20));
        assertTrue(noResult.isEmpty(), "Should find no Bonos");
    }
}

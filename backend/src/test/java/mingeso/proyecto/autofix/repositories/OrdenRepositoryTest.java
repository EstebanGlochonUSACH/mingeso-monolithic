package mingeso.proyecto.autofix.repositories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.dtos.AvgRepairTimeByMarcaDTO;

import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
public class OrdenRepositoryTest {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Test
    public void testFindAllSorted() {
        // Prepare test data
        Auto auto1 = new Auto();
        auto1.setPatente("ABC123");
        auto1.setKilometraje(1);
        autoRepository.save(auto1);

        Auto auto2 = new Auto();
        auto2.setPatente("XYZ456");
        auto2.setKilometraje(1);
        autoRepository.save(auto2);

        Orden orden1 = new Orden();
        orden1.setAuto(auto1);
        orden1.setFechaIngreso(LocalDateTime.now().minusDays(10));
        ordenRepository.save(orden1);

        Orden orden2 = new Orden();
        orden2.setAuto(auto2);
        orden2.setFechaIngreso(LocalDateTime.now().minusDays(5));
        ordenRepository.save(orden2);

        // Test findAllSorted with pagination
        Page<Orden> result = ordenRepository.findAllSorted(PageRequest.of(0, 10));
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.getTotalElements(), "Should find all Orden records");
        assertEquals("ABC123", result.getContent().get(0).getAuto().getPatente(), "First should be ABC123");
        assertEquals("XYZ456", result.getContent().get(1).getAuto().getPatente(), "Second should be XYZ456");
    }

    @Test
    public void testFindByAutoPatente() {
        // Prepare test data
        Auto auto = new Auto();
        auto.setPatente("TEST123");
        auto.setKilometraje(1);
        autoRepository.save(auto);

        Orden orden = new Orden();
        orden.setAuto(auto);
        orden.setFechaIngreso(LocalDateTime.now());
        ordenRepository.save(orden);

        // Test findByAutoPatente with pagination
        Page<Orden> result = ordenRepository.findByAutoPatente("TEST", PageRequest.of(0, 10));
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "Should find one Orden");
        assertEquals("TEST123", result.getContent().get(0).getAuto().getPatente(), "Patente should be TEST123");
    }

    @Test
    public void testFindByAuto() {
        // Prepare test data
        Auto auto = new Auto();
        auto.setPatente("AUTO123");
        auto.setKilometraje(1);
        autoRepository.save(auto);

        Orden orden = new Orden();
        orden.setAuto(auto);
        orden.setFechaIngreso(LocalDateTime.now().minusDays(1));
        ordenRepository.save(orden);

        // Test findByAuto with pagination
        Page<Orden> result = ordenRepository.findByAuto(auto, PageRequest.of(0, 10));
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "Should find one Orden");
        assertEquals("AUTO123", result.getContent().get(0).getAuto().getPatente(), "Patente should be AUTO123");
    }

    @Test
    public void testFindAvgRepairTimeByMarca() {
        // Prepare test data
        Marca marca = new Marca("Toyota");
        marcaRepository.save(marca);

        Auto auto = new Auto("REPAIR123", marca, "Camry", Auto.Tipo.SEDAN, 2021, Auto.Motor.GASOLINA, 5);
        autoRepository.save(auto);

        Orden orden = new Orden();
        orden.setAuto(auto);
        orden.setFechaIngreso(LocalDateTime.now().minusDays(1));
        orden.setFechaSalida(LocalDateTime.now());
        ordenRepository.save(orden);

        // Test findAvgRepairTimeByMarca
        List<AvgRepairTimeByMarcaDTO> result = ordenRepository.findAvgRepairTimeByMarca();
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one AvgRepairTimeByMarcaDTO");
    }
}

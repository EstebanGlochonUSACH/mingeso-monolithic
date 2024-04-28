package mingeso.proyecto.autofix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.repositories.AutoRepository;

import java.util.Optional;

@ActiveProfiles("test")
public class AutoServiceTest {

    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private AutoService autoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAutos() {
        Pageable pageable = mock(Pageable.class);
        Page<Auto> mockPage = mock(Page.class);

        when(autoRepository.findAllMatchPatente(anyString(), eq(pageable))).thenReturn(mockPage);

        Page<Auto> result = autoService.getAllAutos("ABC123", pageable);

        assertEquals(mockPage, result);
        verify(autoRepository, times(1)).findAllMatchPatente("ABC123", pageable);
    }

    @Test
    public void testGetAutoById() {
        Auto expectedAuto = new Auto();
        expectedAuto.setId(1L);

        when(autoRepository.findById(1L)).thenReturn(Optional.of(expectedAuto));

        Auto result = autoService.getAutoById(1L);

        assertNotNull(result);
        assertEquals(expectedAuto, result);
        verify(autoRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAutoByPatente() {
        Auto expectedAuto = new Auto();
        expectedAuto.setPatente("ABC123");

        when(autoRepository.findByPatente("ABC123")).thenReturn(Optional.of(expectedAuto));

        Auto result = autoService.getAutoByPatente("ABC123");

        assertNotNull(result);
        assertEquals(expectedAuto, result);
        verify(autoRepository, times(1)).findByPatente("ABC123");
    }

    @Test
    public void testSaveAuto() {
        Auto auto = new Auto();
        auto.setPatente("ABC123");

        when(autoRepository.save(auto)).thenReturn(auto);

        Auto savedAuto = autoService.saveAuto(auto);

        assertNotNull(savedAuto);
        assertEquals(auto, savedAuto);
        verify(autoRepository, times(1)).save(auto);
    }

    @Test
    public void testUpdateAuto() {
        Auto existingAuto = new Auto();
        existingAuto.setId(1L);

        Auto updatedAuto = new Auto();
        updatedAuto.setPatente("XYZ987");

        when(autoRepository.findById(1L)).thenReturn(Optional.of(existingAuto));
        when(autoRepository.save(existingAuto)).thenReturn(existingAuto);

        Auto result = autoService.updateAuto(1L, updatedAuto);

        assertNotNull(result);
        assertEquals("XYZ987", result.getPatente());
        verify(autoRepository, times(1)).findById(1L);
        verify(autoRepository, times(1)).save(existingAuto);
    }

    @Test
    public void testDeleteAuto() {
        doNothing().when(autoRepository).deleteById(1L);

        assertDoesNotThrow(() -> autoService.deleteAuto(1L));
        verify(autoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAutoByPatente() {
        Auto auto = new Auto();
        auto.setId(1L);
        auto.setPatente("ABC123");

        when(autoRepository.findByPatente("ABC123")).thenReturn(Optional.of(auto));
        doNothing().when(autoRepository).deleteById(1L);

        assertDoesNotThrow(() -> autoService.deleteAutoByPatente("ABC123"));
        verify(autoRepository, times(1)).deleteById(1L);
    }
}

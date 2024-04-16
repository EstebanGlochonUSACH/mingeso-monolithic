package mingeso.proyecto.autofix.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    public Optional<Marca> findByNombre(String nombre);
}

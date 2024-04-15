package mingeso.proyecto.autofix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
}

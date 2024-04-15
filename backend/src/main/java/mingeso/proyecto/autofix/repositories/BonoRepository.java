package mingeso.proyecto.autofix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Bono;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {
}

package mingeso.proyecto.autofix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Reparacion;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {
}

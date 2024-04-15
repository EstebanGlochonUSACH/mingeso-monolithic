package mingeso.proyecto.autofix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
}

package mingeso.proyecto.autofix.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Auto;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long>
{
	public Optional<Auto> findByPatente(String patente);
	public Boolean existsByPatente(String patente);
}

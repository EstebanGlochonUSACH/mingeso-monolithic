package mingeso.proyecto.autofix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long>
{
	public Page<Orden> findAll(Pageable pageable);
	public Page<Orden> findByAuto(Auto auto, Pageable pageable);
}

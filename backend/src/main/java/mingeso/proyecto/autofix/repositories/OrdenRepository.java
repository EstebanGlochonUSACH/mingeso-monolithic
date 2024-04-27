package mingeso.proyecto.autofix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long>
{
	public Page<Orden> findAll(Pageable pageable);

    @Query("SELECT o FROM Orden o ORDER BY o.fechaIngreso ASC")
    public Page<Orden> findAllSorted(Pageable pageable);

	@Query("SELECT o FROM Orden o WHERE o.auto.patente LIKE %:patente% ORDER BY o.fechaIngreso ASC")
    public Page<Orden> findByAutoPatente(@Param("patente") String patente, Pageable pageable);

	public Page<Orden> findByAuto(Auto auto, Pageable pageable);
}

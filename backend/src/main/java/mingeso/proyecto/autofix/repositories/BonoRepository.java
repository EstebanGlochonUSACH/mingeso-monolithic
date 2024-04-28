package mingeso.proyecto.autofix.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long>
{
	@Query("SELECT new mingeso.proyecto.autofix.dtos.BonoGroupedByFechaInicioDTO(b.marca, b.monto, b.fechaInicio, b.fechaTermino, COUNT(b)) " +
		   "FROM Bono b GROUP BY b.marca, b.monto, b.fechaInicio, b.fechaTermino " +
		   "ORDER BY b.fechaInicio DESC")
	public List<BonoGroupedByFechaInicioDTO> groupByFechaInicio();

	@Query("SELECT b FROM Bono b WHERE b.marca = :marca AND b.usado IS FALSE")
	List<Bono> findAllByMarca(Marca marca);

	@Query("SELECT b FROM Bono b WHERE b.marca = :marca " +
		   "AND (b.fechaInicio <= COALESCE(:fecha, CURRENT_DATE) AND b.fechaTermino >= COALESCE(:fecha, CURRENT_DATE)) " +
		   "AND b.usado IS FALSE")
	List<Bono> findAllByMarcaAndFecha(
		@Param("marca") Marca marca,
		@Param("fecha") LocalDateTime fecha
	);
}

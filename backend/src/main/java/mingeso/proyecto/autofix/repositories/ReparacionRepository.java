package mingeso.proyecto.autofix.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mingeso.proyecto.autofix.dtos.ReparacionMotorSummary;
import mingeso.proyecto.autofix.dtos.ReparacionTipoSummary;
import mingeso.proyecto.autofix.entities.Reparacion;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Long>
{
	@Query("SELECT new mingeso.proyecto.autofix.dtos.ReparacionTipoSummary(r.tipo, r.orden.auto.tipo, COUNT(DISTINCT r.orden.auto), SUM(r.monto)) " +
		  "FROM Reparacion r " +
		  "WHERE r.orden.fechaSalida IS NOT NULL " +
		  "GROUP BY r.tipo, r.orden.auto.tipo " +
		  "ORDER BY SUM(r.monto) DESC")
	List<ReparacionTipoSummary> findReparacionTipoSummary();

	@Query("SELECT new mingeso.proyecto.autofix.dtos.ReparacionMotorSummary(r.tipo, r.orden.auto.motor, COUNT(DISTINCT r.orden.auto), SUM(r.monto)) " +
		   "FROM Reparacion r " +
		   "WHERE r.orden.fechaSalida IS NOT NULL " +
		   "GROUP BY r.tipo, r.orden.auto.motor " +
		   "ORDER BY SUM(r.monto) DESC")
	List<ReparacionMotorSummary> findReparacionMotorSummary();
}

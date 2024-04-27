package mingeso.proyecto.autofix.dtos;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Reparacion;

public class ReparacionTipoSummary
{
	private Reparacion.Tipo tipoReparacion;
	private Auto.Tipo tipoAuto;
	private Long countVehiculos;
	private Long montoTotal;

	public ReparacionTipoSummary(Reparacion.Tipo tipoReparacion, Auto.Tipo tipoAuto, Long countVehiculos, Long montoTotal) {
		this.tipoReparacion = tipoReparacion;
		this.tipoAuto = tipoAuto;
		this.countVehiculos = countVehiculos;
		this.montoTotal = montoTotal;
	}

	public Reparacion.Tipo getTipoReparacion() {
		return tipoReparacion;
	}

	public Auto.Tipo getTipoAuto() {
		return tipoAuto;
	}

	public Long getCountVehiculos() {
		return countVehiculos;
	}

	public Long getMontoTotal() {
		return montoTotal;
	}
}

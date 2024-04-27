package mingeso.proyecto.autofix.dtos;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Reparacion;

public class ReparacionMotorSummary
{
	private Reparacion.Tipo tipoReparacion;
	private Auto.Motor tipoMotor;
	private Long countVehiculos;
	private Long montoTotal;

	public ReparacionMotorSummary(Reparacion.Tipo tipoReparacion, Auto.Motor tipoMotor, Long countVehiculos, Long montoTotal) {
		this.tipoReparacion = tipoReparacion;
		this.tipoMotor = tipoMotor;
		this.countVehiculos = countVehiculos;
		this.montoTotal = montoTotal;
	}

	public Reparacion.Tipo getTipoReparacion() {
		return tipoReparacion;
	}

	public Auto.Motor getTipoMotor() {
		return tipoMotor;
	}

	public Long getCountVehiculos() {
		return countVehiculos;
	}

	public Long getMontoTotal() {
		return montoTotal;
	}
}

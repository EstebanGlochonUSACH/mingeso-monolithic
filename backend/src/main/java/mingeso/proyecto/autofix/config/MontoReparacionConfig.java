package mingeso.proyecto.autofix.config;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Reparacion;

import java.util.HashMap;
import java.util.Map;

public class MontoReparacionConfig {

	private static final Map<Auto.Motor, Map<Reparacion.Tipo, Integer>> MONTOS = new HashMap<>();

	static {
		// Para auto tipo GASOLINA:
		Map<Reparacion.Tipo, Integer> reparacionMontoMapGasolina = new HashMap<>();
		reparacionMontoMapGasolina.put(Reparacion.Tipo.FRENOS, 120_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.REFRIGERACION, 130_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.MOTOR, 350_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.TRANSMISION, 210_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.SIS_ELECTRICO, 150_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.SIS_ESCAPE, 100_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.NEUMATICOS, 100_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.SUSPENSION_DIRECCION, 180_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.AIRE_ACONDICIONADO, 150_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.COMBUSTIBLE, 130_000);
		reparacionMontoMapGasolina.put(Reparacion.Tipo.PARABRISAS, 80_000);
		MONTOS.put(Auto.Motor.GASOLINA, reparacionMontoMapGasolina);

		// Para auto tipo DIESEL:
		Map<Reparacion.Tipo, Integer> reparacionMontoMapDiesel = new HashMap<>();
		reparacionMontoMapDiesel.put(Reparacion.Tipo.FRENOS, 120_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.REFRIGERACION, 130_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.MOTOR, 450_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.TRANSMISION, 210_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.SIS_ELECTRICO, 150_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.SIS_ESCAPE, 120_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.NEUMATICOS, 100_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.SUSPENSION_DIRECCION, 180_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.AIRE_ACONDICIONADO, 150_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.COMBUSTIBLE, 140_000);
		reparacionMontoMapDiesel.put(Reparacion.Tipo.PARABRISAS, 80_000);
		MONTOS.put(Auto.Motor.GASOLINA, reparacionMontoMapDiesel);

		// Para auto tipo HIBRIDO:
		Map<Reparacion.Tipo, Integer> reparacionMontoMapHibrido = new HashMap<>();
		reparacionMontoMapHibrido.put(Reparacion.Tipo.FRENOS, 180_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.REFRIGERACION, 190_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.MOTOR, 700_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.TRANSMISION, 300_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.SIS_ELECTRICO, 200_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.SIS_ESCAPE, 450_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.NEUMATICOS, 100_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.SUSPENSION_DIRECCION, 210_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.AIRE_ACONDICIONADO, 180_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.COMBUSTIBLE, 220_000);
		reparacionMontoMapHibrido.put(Reparacion.Tipo.PARABRISAS, 80_000);
		MONTOS.put(Auto.Motor.HIBRIDO, reparacionMontoMapHibrido);

		// Para auto tipo ELECTRICO:
		Map<Reparacion.Tipo, Integer> reparacionMontoMapElectrico = new HashMap<>();
		reparacionMontoMapElectrico.put(Reparacion.Tipo.FRENOS, 220_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.REFRIGERACION, 230_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.MOTOR, 800_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.TRANSMISION, 300_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.SIS_ELECTRICO, 250_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.SIS_ESCAPE, 0);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.NEUMATICOS, 100_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.SUSPENSION_DIRECCION, 250_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.AIRE_ACONDICIONADO, 180_000);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.COMBUSTIBLE, 0);
		reparacionMontoMapElectrico.put(Reparacion.Tipo.PARABRISAS, 80_000);
		MONTOS.put(Auto.Motor.ELECTRICO, reparacionMontoMapElectrico);
	}

	public static Integer getMonto(Auto.Motor tipoMotor, Reparacion.Tipo tipoReparacion) {
		Map<Reparacion.Tipo, Integer> reparacionMontoMap = MONTOS.get(tipoMotor);
		if (reparacionMontoMap != null) {
			return reparacionMontoMap.get(tipoReparacion);
		}
		return null;
	}
}

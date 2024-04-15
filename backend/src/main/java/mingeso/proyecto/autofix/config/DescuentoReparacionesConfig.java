package mingeso.proyecto.autofix.config;

import mingeso.proyecto.autofix.entities.Auto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescuentoReparacionesConfig {

	private static final Map<Auto.Motor, List<Double>> DESCUENTOS = new HashMap<>();

	static {
		// Para auto tipo GASOLINA:
		List<Double> listGasolina = new ArrayList<>();
		listGasolina.add(0.05);
		listGasolina.add(0.10);
		listGasolina.add(0.15);
		listGasolina.add(0.20);
		DESCUENTOS.put(Auto.Motor.GASOLINA, listGasolina);

		// Para auto tipo DIESEL:
		List<Double> listDiesel = new ArrayList<>();
		listDiesel.add(0.07);
		listDiesel.add(0.12);
		listDiesel.add(0.17);
		listDiesel.add(0.22);
		DESCUENTOS.put(Auto.Motor.GASOLINA, listDiesel);

		// Para auto tipo HIBRIDO:
		List<Double> listHibrido = new ArrayList<>();
		listHibrido.add(0.10);
		listHibrido.add(0.15);
		listHibrido.add(0.20);
		listHibrido.add(0.25);
		DESCUENTOS.put(Auto.Motor.HIBRIDO, listHibrido);

		// Para auto tipo ELECTRICO:
		List<Double> listElectrico = new ArrayList<>();
		listElectrico.add(0.08);
		listElectrico.add(0.13);
		listElectrico.add(0.18);
		listElectrico.add(0.23);
		DESCUENTOS.put(Auto.Motor.ELECTRICO, listElectrico);
	}

	public static Double getDescuento(Auto.Motor tipoMotor, Integer reparaciones) {
		List<Double> descuentos = DESCUENTOS.get(tipoMotor);
		if (descuentos != null) {
			if(reparaciones < 3){
				return descuentos.get(0);
			}
			else if(reparaciones >= 3 && reparaciones < 6){
				return descuentos.get(1);
			}
			else if(reparaciones >= 6 && reparaciones < 10){
				return descuentos.get(2);
			}
			return descuentos.get(3);
		}
		return null;
	}
}

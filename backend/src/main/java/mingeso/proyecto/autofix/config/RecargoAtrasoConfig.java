package mingeso.proyecto.autofix.config;

public class RecargoAtrasoConfig
{
	private static final Double RECARGO = 0.05;

	public static Double getRecargo(Integer monto, Integer dias) {
		return monto * (RECARGO * dias);
	}

	public static Double getRecargo(Long monto, Integer dias) {
		return monto * (RECARGO * dias);
	}
}

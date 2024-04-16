package mingeso.proyecto.autofix.config;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidadorPatenteConfig
{
	static final String EXP_AUTO_VIEJO = "[A-Z]{2}[1-9]{1}[0-9]{3}";
	static final String EXP_AUTO_NUEVO = "[BCDFGHJKLPRSTVWXYZ]{4}[0-9]{2}";

	static final String EXP_MOTO_VIEJA_5 = "[A-Z]{2}[0-9]{3}";
	static final String EXP_MOTO_NUEVA_5 = "[BCDFGHJKLPRSTVWXYZ]{3}[0-9]{2}";

	static final String EXP_MOTO_VIEJA_6 = "[A-Z]{2}[0]{1}[0-9]{3}";
	static final String EXP_MOTO_NUEVA_6 = "[BCDFGHJKLPRSTVWXYZ]{3}[0]{1}[0-9]{2}";

	static final String EXP_MOTO_VIEJA_CON_O = "[A-Z]{2}[O]{1}[0-9]{3}";
	static final String EXP_MOTO_NUEVA_CON_O = "[BCDFGHJKLPRSTVWXYZ]{3}[O]{1}[0-9]{2}";

	public static Boolean isPatternMatcher(String exp, String val) throws NullPointerException {
		Pattern p = Pattern.compile(exp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	public static Boolean isPatenteAutoViejo(String patente) {
		try {
			return isPatternMatcher(EXP_AUTO_VIEJO, patente);
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteAutoNuevo(String patente) {
		try {
			return isPatternMatcher(EXP_AUTO_NUEVO, patente);
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteAuto(String patente) {
		try {
			return (isPatenteAutoViejo(patente) || isPatenteAutoNuevo(patente));
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteMotoVieja(String patente) {
		try {
			return (isPatternMatcher(EXP_MOTO_VIEJA_5, patente) || isPatternMatcher(EXP_MOTO_VIEJA_6, patente));
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteMotoNueva(String patente) {
		try {
			return (isPatternMatcher(EXP_MOTO_NUEVA_5, patente) || isPatternMatcher(EXP_MOTO_NUEVA_6, patente));
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteMoto(String patente) {
		try {
			return (isPatenteMotoVieja(patente) || isPatenteMotoNueva(patente));
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean isPatenteMotoNotComplete(String patente) {
		try {
			return (isPatternMatcher(EXP_MOTO_VIEJA_5, patente) || isPatternMatcher(EXP_MOTO_VIEJA_5, patente));
		} catch (Exception e) {
			return false;
		}
	}

	public static String getComplete(String patente) {
			return patenteMotoComplete(patente);
	}

	public static String patenteMotoComplete(String patente) {
		return (patente.substring(0, 3) + "0" + patente.substring(3, 5));
	}

	public static String getNormalized(String patente) {
		return getNormalizedMatch(patente);
	}

	public static Boolean isBeNormalized(String patente) {
		try {
			String retorno = null;

			retorno = getNormalizedMatch(patente);

			if (retorno != null)
				return Boolean.TRUE;

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static String getNormalizedMatch(String patente) {
		if (isPatternMatcher(EXP_MOTO_NUEVA_CON_O, patente))
			return patente.substring(0, 3) + "0" + patente.substring(4, 6);
		else if (isPatternMatcher(EXP_MOTO_VIEJA_CON_O, patente))
			return patente.substring(0, 3) + "0" + patente.substring(3, 6);
		else
			return null;
	}
}

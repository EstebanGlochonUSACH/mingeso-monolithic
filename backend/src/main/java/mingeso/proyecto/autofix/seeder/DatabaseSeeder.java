package mingeso.proyecto.autofix.seeder;

import mingeso.proyecto.autofix.config.MontoReparacionConfig;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;

import mingeso.proyecto.autofix.repositories.ReparacionRepository;

import mingeso.proyecto.autofix.services.AutoService;
import mingeso.proyecto.autofix.services.MarcaService;
import mingeso.proyecto.autofix.services.BonoService;
import mingeso.proyecto.autofix.services.OrdenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
public class DatabaseSeeder implements CommandLineRunner
{
	private static Integer MIN_YEAR = 2005;
	private static Integer MAX_YEAR = 2024;
	private static final Random RANDOM = new Random();

	// Lista de modelos de autos
	private static final List<String> autoModelos = new ArrayList<>();
	static {
		autoModelos.add("Corolla");
		autoModelos.add("Civic");
		autoModelos.add("Focus");
		autoModelos.add("3 Series");
		autoModelos.add("C-Class");
		autoModelos.add("A4");
		autoModelos.add("Golf");
		autoModelos.add("Model S");
	}

	private final AutoService autoService;
	private final ReparacionRepository reparacionRepository;

	private final BonoService bonoService;
	private final MarcaService marcaService;
	private final OrdenService ordenService;

	@Autowired
	public DatabaseSeeder(
		AutoService autoService,
		BonoService bonoService,
		MarcaService marcaService,
		OrdenService ordenService,
		ReparacionRepository reparacionRepository
	) {
		this.autoService = autoService;
		this.marcaService = marcaService;
		this.ordenService = ordenService;
		this.reparacionRepository = reparacionRepository;

		this.bonoService = bonoService;

		Integer currentYear = LocalDate.now().getYear();
		if(currentYear > MIN_YEAR + 10){
			MAX_YEAR = currentYear;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		seedData();
	}

	private static Integer getRandomBetween(Integer first, Integer last) {
		Integer randomYear = RANDOM.nextInt(last - first + 1) + first;
		return randomYear;
	}

	private static Integer getRandomYear() {
		return getRandomBetween(MIN_YEAR, MAX_YEAR);
	}

	private static String createPatente(String alphaBase, Integer number) {
		return alphaBase + String.format("%03d", number);
	}

	public static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
		T[] enumValues = enumClass.getEnumConstants();
		int randomIndex = RANDOM.nextInt(enumValues.length);
		return enumValues[randomIndex];
	}

	public static String getRandomAutoModelo() {
		int randomIndex = RANDOM.nextInt(autoModelos.size());
		return autoModelos.get(randomIndex);
	}

	private static Integer getRandomAsientos() {
		return getRandomBetween(5, 8);
	}

	private static Integer getRandomKilometraje() {
		return getRandomBetween(100, 200_000);
	}

	public static <T> T getRandomFromList(List<T> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("List must not be null or empty");
		}
		int randomIndex = RANDOM.nextInt(list.size());
		return list.get(randomIndex);
	}

	public static <T> T getRandomAndRemove(List<T> list) {
		if (list == null || list.isEmpty()) return null;
		int randomIndex = RANDOM.nextInt(list.size());
		T randomElement = list.get(randomIndex);
		list.remove(randomIndex);
		return randomElement;
	}

	private void createAutos(List<Auto> autos, Marca marca, Integer total, String basePatente) {
		Auto auto;
		String patente;
		for(int i = 1; i <= total; ++i){
			patente = createPatente(basePatente, i);
			auto = autoService.getAutoByPatente(patente);
			if(auto == null){
				auto = new Auto(
					patente,
					marca,
					getRandomAutoModelo(),
					getRandomEnumValue(Auto.Tipo.class),
					getRandomYear(),
					getRandomEnumValue(Auto.Motor.class),
					getRandomAsientos(),
					getRandomKilometraje()
				);
				auto = autoService.saveAuto(auto);
				autos.add(auto);
			}
			else{
				autos.add(auto);
			}
		}
	}

	private Orden createOrden(Auto auto, Bono bono, LocalDateTime now, LocalDateTime currentDay) throws Exception {
		// Crear Orden
		Orden orden = new Orden(auto);
		if(bono != null){
			orden.setBono(bono);
		}
		ordenService.createOrden(orden);

		// Crear Reparaciones & Ingreso de la orden
		Long montoReparaciones = 0L;
		Reparacion reparacion;
		Integer totalReparaciones = getRandomBetween(1, 11);
		Integer countReparaciones = 0;
		do {
			for(int i = 0; i < totalReparaciones; ++i){
				Reparacion.Tipo tipoReparacion = getRandomEnumValue(Reparacion.Tipo.class);
				Integer monto = MontoReparacionConfig.getMonto(auto.getMotor(), tipoReparacion);
				if(monto > 0){
					montoReparaciones = montoReparaciones + monto.longValue();
					reparacion = new Reparacion(orden, tipoReparacion, monto);
					reparacionRepository.save(reparacion);
					++countReparaciones;
				}
			}
		}
		while(countReparaciones == 0);

		orden.setFechaIngreso(currentDay);
		orden.setMontoReparaciones(montoReparaciones);
		ordenService.updateOrden(orden, totalReparaciones);

		// Simular Salida
		LocalDateTime fechaSalida = currentDay.plusDays(totalReparaciones);
		orden.setFechaSalida(fechaSalida);
		if(fechaSalida.isAfter(now)) return orden;
		ordenService.updateOrden(orden, totalReparaciones);

		// Simular Entrega (con o sin atraso)
		Integer diasAtraso = getRandomBetween(0, 10);
		LocalDateTime fechaEntrega = fechaSalida.plusDays(diasAtraso);
		orden.setFechaEntrega(fechaEntrega);
		ordenService.updateOrden(orden, totalReparaciones);

		return orden;
	}

	private void seedData() throws Exception {
		// Registrar Marcas
		Marca marcaToyota = marcaService.getOrCreateMarca("Toyota");
		Marca marcaFord = marcaService.getOrCreateMarca("Ford");
		Marca marcaHyundai = marcaService.getOrCreateMarca("Hyundai");
		Marca marcaHonda = marcaService.getOrCreateMarca("Honda");
		Marca marcaKia = marcaService.getOrCreateMarca("Kia");
		Marca marcaChevrolet = marcaService.getOrCreateMarca("Chevrolet");

		List<Bono> bonosToyota = bonoService.createBonos(marcaToyota, 70_000, 5);
		List<Bono> bonosFord = bonoService.createBonos(marcaFord, 50_000, 2);
		List<Bono> bonosHyundai = bonoService.createBonos(marcaHyundai, 30_000, 1);
		List<Bono> bonosHonda = bonoService.createBonos(marcaHonda, 40_000, 7);

		List<Auto> autos = new ArrayList<>();
		createAutos(autos, marcaToyota, 40, "TOY");
		createAutos(autos, marcaFord, 40, "FRD");
		createAutos(autos, marcaHyundai, 30, "HYD");
		createAutos(autos, marcaHonda, 30, "HON");
		createAutos(autos, marcaKia, 10, "KIA");
		createAutos(autos, marcaChevrolet, 50, "CHV");

		Long totalOrdenes = Math.round(autos.size() * 2.5);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime firstDay = now.minusYears(1);
		firstDay = LocalDateTime.of(firstDay.getYear(), 1, 1, firstDay.getHour(), firstDay.getMinute());

		Auto auto;
		Marca marca;
		Bono bono = null;
		LocalDateTime currentDay = LocalDateTime.from(firstDay);
		Month lastMonth = currentDay.getMonth();
		for(int i = 0; i < totalOrdenes; ++i){
			currentDay = currentDay.plusDays(1);
			currentDay = LocalDateTime.of(
				currentDay.getYear(),
				currentDay.getMonth(),
				currentDay.getDayOfMonth(),
				getRandomBetween(8, 18),
				getRandomBetween(1, 59)
			);

			if(currentDay.getMonth() != lastMonth){
				lastMonth = currentDay.getMonth();
				bonosToyota = bonoService.createBonos(marcaToyota, 70_000, 5);
				bonosFord = bonoService.createBonos(marcaFord, 50_000, 2);
				bonosHyundai = bonoService.createBonos(marcaHyundai, 30_000, 1);
				bonosHonda = bonoService.createBonos(marcaHonda, 40_000, 7);
			}

			bono = null;
			auto = getRandomFromList(autos);
			marca = auto.getMarca();
			if(marca.equals(marcaToyota)){
				bono = getRandomAndRemove(bonosToyota);
			}
			else if(marca.equals(marcaFord)){
				bono = getRandomAndRemove(bonosFord);
			}
			else if(marca.equals(marcaHyundai)){
				bono = getRandomAndRemove(bonosHyundai);
			}
			else if(marca.equals(marcaHonda)){
				bono = getRandomAndRemove(bonosHonda);
			}
			createOrden(auto, bono, now, currentDay);
		}
	}
}

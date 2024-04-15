package mingeso.proyecto.autofix.seeder;

import mingeso.proyecto.autofix.entities.Auto;
import mingeso.proyecto.autofix.entities.Marca;
import mingeso.proyecto.autofix.entities.Bono;
import mingeso.proyecto.autofix.entities.Orden;
import mingeso.proyecto.autofix.entities.Reparacion;
import mingeso.proyecto.autofix.entities.Reparacion.Tipo;

import mingeso.proyecto.autofix.repositories.AutoRepository;
import mingeso.proyecto.autofix.repositories.MarcaRepository;
import mingeso.proyecto.autofix.repositories.BonoRepository;
import mingeso.proyecto.autofix.repositories.OrdenRepository;
import mingeso.proyecto.autofix.repositories.ReparacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final AutoRepository autoRepository;
    private final MarcaRepository marcaRepository;
    private final BonoRepository bonoRepository;
    private final OrdenRepository ordenRepository;
    private final ReparacionRepository reparacionRepository;

    @Autowired
    public DatabaseSeeder(AutoRepository autoRepository, MarcaRepository marcaRepository, BonoRepository bonoRepository, OrdenRepository ordenRepository, ReparacionRepository reparacionRepository) {
        this.autoRepository = autoRepository;
        this.marcaRepository = marcaRepository;
        this.bonoRepository = bonoRepository;
        this.ordenRepository = ordenRepository;
        this.reparacionRepository = reparacionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        Marca toyota = new Marca("Toyota");
        marcaRepository.save(toyota);

        Auto auto = new Auto("ABC123", toyota, "Corolla", Auto.Tipo.SEDAN, 2020, Auto.Motor.GASOLINA, 5);
        autoRepository.save(auto);

        Bono bono = new Bono(toyota, 10000);
        bonoRepository.save(bono);

        Orden orden = new Orden();
        orden.setAuto(auto);
        orden.setBono(bono);
        orden.setMonto(5000);
        orden.setDescuentoDiaAtencion(true);
        ordenRepository.save(orden);

        Reparacion reparacion = new Reparacion();
        reparacion.setTipo(Tipo.MOTOR);
        reparacion.setMonto(2000);
        reparacion.setOrden(orden);
        reparacionRepository.save(reparacion);
    }
}

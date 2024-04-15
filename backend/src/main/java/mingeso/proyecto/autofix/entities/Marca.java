package mingeso.proyecto.autofix.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Marca
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id_marca;

	private String nombre;

	public Marca(String nombre){
		this.nombre = nombre;
	}

	public String getNombre(){
		return nombre;
	}

	public void setNombre(String nombre){
		this.nombre = nombre;
	}
}

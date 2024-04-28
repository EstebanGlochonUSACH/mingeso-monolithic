package mingeso.proyecto.autofix.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Marca
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("id")
	private Long id_marca;

	@Column(unique = true)
	private String nombre;

	public Marca() {}

	public Marca(String nombre){
		this.nombre = nombre;
	}

	public Long getId(){
		return id_marca;
	}

	public void setId(Long id_marca){
		this.id_marca = id_marca;
	}

	public String getNombre(){
		return nombre;
	}

	public void setNombre(String nombre){
		this.nombre = nombre;
	}
}

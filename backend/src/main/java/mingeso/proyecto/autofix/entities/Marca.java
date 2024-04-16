package mingeso.proyecto.autofix.entities;

import java.util.Objects;

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

	public String getNombre(){
		return nombre;
	}

	public void setNombre(String nombre){
		this.nombre = nombre;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marca marca = (Marca)o;
        return Objects.equals(id_marca, marca.id_marca);
    }

}

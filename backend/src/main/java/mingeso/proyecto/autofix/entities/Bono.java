package mingeso.proyecto.autofix.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Bono
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id_bono;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_marca")
	public Marca marca;

	private Integer monto;

	@Column(name = "fecha_inicio")
	private LocalDateTime fechaInicio;

	@Column(name = "fecha_termino")
	private LocalDateTime fechaTermino;

	@OneToOne(mappedBy = "bono", fetch = FetchType.LAZY)
	private Orden orden;

	private Boolean usado;

	public Bono() {}

	public Bono(Marca marca, Integer monto, LocalDateTime fechaInicio, LocalDateTime fechaTermino){
		this.marca = marca;
		this.monto = monto;
		this.usado = false;
		this.fechaInicio = fechaInicio;
		this.fechaTermino = fechaTermino;
	}

	public Long getId(){
		return id_bono;
	}

	public Marca getMarca(){
		return marca;
	}

	public Integer getMonto(){
		return monto;
	}

	public Boolean getUsado(){
		return usado;
	}

	public void setUsado(Boolean usado){
		this.usado = usado;
	}

	public LocalDateTime getFechaInicio(){
		return fechaInicio;
	}

	public LocalDateTime getFechaTermino(){
		return fechaTermino;
	}
}

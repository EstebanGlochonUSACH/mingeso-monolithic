package mingeso.proyecto.autofix.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Orden
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id_orden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_auto")
	public Auto auto;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bono", nullable = true)
	public Bono bono;

	@Column(nullable = true)
	private Integer monto;

	private Boolean descuento_dia_atencion;

	@Column(name = "fecha_ingreso")
	private LocalDateTime fechaIngreso;

	@Column(name = "fecha_salida", nullable = true)
	private LocalDateTime fechaSalida;

	@Column(name = "fecha_entrega", nullable = true)
	private LocalDateTime fechaEntrega;

	@OneToMany(mappedBy = "orden", fetch = FetchType.LAZY)
	private List<Reparacion> reparaciones = new ArrayList<Reparacion>();

	public Integer getMonto(){
		return monto;
	}

	public Bono getBono() {
		return bono;
	}

	public void setBono(Bono bono) {
		this.bono = bono;
	}

	public void setMonto(Integer monto){
		this.monto = monto;
	}

	public Boolean getDescuentoDiaAtencion(){
		return descuento_dia_atencion;
	}

	public void setDescuentoDiaAtencion(Boolean descuento_dia_atencion){
		this.descuento_dia_atencion = descuento_dia_atencion;
	}

	public List<Reparacion> getReparacions() {
		return reparaciones;
	}

	public LocalDateTime getFechaIngreso(){
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDateTime fechaIngreso){
		this.fechaIngreso = fechaIngreso;
	}

	public LocalDateTime getFechaSalida(){
		return fechaSalida;
	}

	public void setFechaSalida(LocalDateTime fechaSalida){
		this.fechaSalida = fechaSalida;
	}

	public LocalDateTime getFechaEntrega(){
		return fechaEntrega;
	}

	public void setFechaEntrega(LocalDateTime fechaEntrega){
		this.fechaEntrega = fechaEntrega;
	}
}

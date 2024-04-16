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

	@Column(name = "monto_reparaciones", nullable = true)
	private Long montoReparaciones;

	@Column(name = "descuento_dia_atencion")
	private Boolean descuentoDiaAtencion;

	@Column(name = "descuento_reparaciones", nullable = true)
	private Long descuentoReparaciones;

	@Column(name = "recarga_antiguedad", nullable = true)
	private Long recargaAntiguedad;

	@Column(name = "recarga_kilometraje", nullable = true)
	private Long recargaKilometraje;

	@Column(name = "recarga_atraso", nullable = true)
	private Long recargaAtraso;

	@Column(name = "fecha_ingreso")
	private LocalDateTime fechaIngreso;

	@Column(name = "fecha_salida", nullable = true)
	private LocalDateTime fechaSalida;

	@Column(name = "fecha_entrega", nullable = true)
	private LocalDateTime fechaEntrega;

	@OneToMany(mappedBy = "orden", fetch = FetchType.LAZY)
	private List<Reparacion> reparaciones = new ArrayList<Reparacion>();

	public Orden() {}

	public Orden(Auto auto, LocalDateTime fechaIngreso){
		this.auto = auto;
		this.fechaIngreso = fechaIngreso;
		this.descuentoDiaAtencion = false;
	}

	public Long getId(){
		return id_orden;
	}

	public Long getMontoReparaciones(){
		return montoReparaciones;
	}

	public void setMontoReparaciones(Long montoReparaciones){
		this.montoReparaciones = montoReparaciones;
	}

	public Auto getAuto() {
		return auto;
	}

	public void setAuto(Auto auto) {
		this.auto = auto;
	}

	public Bono getBono() {
		return bono;
	}

	public void setBono(Bono bono) {
		this.bono = bono;
	}

	public Boolean getDescuentoDiaAtencion(){
		return descuentoDiaAtencion;
	}

	public void setDescuentoDiaAtencion(Boolean descuentoDiaAtencion){
		this.descuentoDiaAtencion = descuentoDiaAtencion;
	}

	public Long getDescuentoReparaciones(){
		return descuentoReparaciones;
	}

	public void setDescuentoReparaciones(Long descuentoReparaciones){
		this.descuentoReparaciones = descuentoReparaciones;
	}

	public void setDescuentoReparaciones(Double descuentoReparaciones){
		this.descuentoReparaciones = Math.round(descuentoReparaciones);
	}

	public Long getRecargaAntiguedad(){
		return recargaAntiguedad;
	}

	public void setRecargaAntiguedad(Long recargaAntiguedad){
		this.recargaAntiguedad = recargaAntiguedad;
	}

	public void setRecargaAntiguedad(Double recargaAntiguedad){
		this.recargaAntiguedad = Math.round(recargaAntiguedad);
	}

	public Long getRecargaKilometraje(){
		return recargaKilometraje;
	}

	public void setRecargaKilometraje(Long recargaKilometraje){
		this.recargaKilometraje = recargaKilometraje;
	}

	public void setRecargaKilometraje(Double recargaKilometraje){
		this.recargaKilometraje = Math.round(recargaKilometraje);
	}

	public Long getRecargaAtraso(){
		return recargaAtraso;
	}

	public void setRecargaAtraso(Long recargaAtraso){
		this.recargaAtraso = recargaAtraso;
	}

	public void setRecargaAtraso(Double recargaAtraso){
		this.recargaAtraso = Math.round(recargaAtraso);
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

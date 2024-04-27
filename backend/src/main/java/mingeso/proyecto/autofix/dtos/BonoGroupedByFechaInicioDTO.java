package mingeso.proyecto.autofix.dtos;

import java.time.LocalDateTime;
import mingeso.proyecto.autofix.entities.Marca;

public class BonoGroupedByFechaInicioDTO {
	private Marca marca;
	private Integer monto;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaTermino;
	private Long count;

	public BonoGroupedByFechaInicioDTO(Marca marca, Integer monto, LocalDateTime fechaInicio, LocalDateTime fechaTermino, Long count) {
		this.marca = marca;
		this.monto = monto;
		this.fechaInicio = fechaInicio;
		this.fechaTermino = fechaTermino;
		this.count = count;
	}

	public Marca getMarca() {
		return marca;
	}

	public Integer getMonto() {
		return monto;
	}

	public LocalDateTime getFechaTermino() {
		return fechaTermino;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public Long getCount() {
		return count;
	}
};

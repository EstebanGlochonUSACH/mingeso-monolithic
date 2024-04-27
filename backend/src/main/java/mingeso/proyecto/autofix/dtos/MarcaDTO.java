package mingeso.proyecto.autofix.dtos;

public class MarcaDTO {
	private Long id;
	private String nombre;
	private Long totalAutos;

	public MarcaDTO(Long id, String nombre, Long totalAutos) {
		this.id = id;
		this.nombre = nombre;
		this.totalAutos = totalAutos;
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Long getTotalAutos() {
		return totalAutos;
	}
}

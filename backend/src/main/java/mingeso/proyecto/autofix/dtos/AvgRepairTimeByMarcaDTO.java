package mingeso.proyecto.autofix.dtos;

public class AvgRepairTimeByMarcaDTO {
	private String marca;
	private double avgRepairTime;

	public AvgRepairTimeByMarcaDTO(String marca, double avgRepairTime) {
		this.marca = marca;
		this.avgRepairTime = avgRepairTime;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public double getAvgRepairTime() {
		return avgRepairTime;
	}

	public void setAvgRepairTime(double avgRepairTime) {
		this.avgRepairTime = avgRepairTime;
	}
}


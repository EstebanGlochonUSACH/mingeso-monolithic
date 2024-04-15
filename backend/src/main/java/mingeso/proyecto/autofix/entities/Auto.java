package mingeso.proyecto.autofix.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import mingeso.proyecto.autofix.serialization.EnumToStringSerializer;
import mingeso.proyecto.autofix.serialization.StringToEnumDeserializer;

@Entity
public class Auto
{
	public enum Tipo { SEDAN, HATCHBACK, SUV, PICKUP, FURGONETA };
	public enum Motor { GASOLINA, DIESEL, HIBRIDO, ELECTRICO };

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id_auto;

	private String patente;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_marca")
	public Marca marca;

	private String modelo;

	@JsonSerialize(using = EnumToStringSerializer.class)
	@JsonDeserialize(using = StringToEnumDeserializer.class)
	private Tipo tipo;

	private Integer anio;

	@JsonSerialize(using = EnumToStringSerializer.class)
	@JsonDeserialize(using = StringToEnumDeserializer.class)
	private Motor motor;

	private Integer asientos;

	@Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer kilometraje;

	public Auto(String patente, Marca marca, String modelo, Tipo tipo, Integer anio, Motor motor, Integer asientos) {
		this.patente = patente;
		this.marca = marca;
		this.tipo = tipo;
		this.anio = anio;
		this.motor = motor;
		this.asientos = asientos;
		this.kilometraje = 0;
	}

	public Auto(String patente, Marca marca, String modelo, Tipo tipo, Integer anio, Motor motor, Integer asientos, Integer kilometraje) {
		this.patente = patente;
		this.marca = marca;
		this.tipo = tipo;
		this.anio = anio;
		this.motor = motor;
		this.asientos = asientos;
		this.kilometraje = kilometraje;
	}

	public Long getId() {
		return id_auto;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public static boolean validarPatente(String patente){
		return(patente.length() == 6);
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public Integer getAsientos() {
		return asientos;
	}

	public void setAsientos(Integer asientos) {
		this.asientos = asientos;
	}

	public Integer getKilometraje() {
		return kilometraje;
	}

	public void setKilometraje(Integer kilometraje) {
		this.kilometraje = kilometraje;
	}

	@Override
	public String toString() {
		return String.format("Auto[id=%d, patente='%s']", id_auto, patente);
	}
}

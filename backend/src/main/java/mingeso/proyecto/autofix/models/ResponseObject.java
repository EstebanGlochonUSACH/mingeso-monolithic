package mingeso.proyecto.autofix.models;

public class ResponseObject<T> {
	private Boolean error;
	private String message;
	private T entity;

	public ResponseObject(String message, T entity) {
		this.message = message;
		this.entity = entity;
		this.error = (entity != null);
	}

	public Boolean getError(){
		return error;
	}

	public String getMessage(){
		return message;
	}

	public T getEntity(){
		return entity;
	}
}

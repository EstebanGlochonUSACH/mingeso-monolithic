package mingeso.proyecto.autofix.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class StringToEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T>
{
	private final Class<T> enumClass;

	public StringToEnumDeserializer(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getValueAsString();
		return Enum.valueOf(enumClass, value);
	}
}

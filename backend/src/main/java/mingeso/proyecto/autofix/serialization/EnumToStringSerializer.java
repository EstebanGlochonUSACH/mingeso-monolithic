package mingeso.proyecto.autofix.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class EnumToStringSerializer extends JsonSerializer<Enum<?>>
{
	@Override
	public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.name());
	}
}

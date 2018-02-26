package cn.bjzfgcjs.idefense.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sun.jna.NativeLong;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class NativeLongMixSerializer  {

    public static class NativeLongJsonSerilzer extends JsonSerializer<NativeLong> {

        @Override
        public void serialize(NativeLong value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartObject();
            gen.writeNumber(value.longValue());
            gen.writeEndObject();

        }
    }

    public static class NativeLongJsonDeserializer extends JsonDeserializer<NativeLong> {

        @Override
        public NativeLong deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            NativeLong number = new NativeLong(p.getNumberValue().longValue());
            return number;
        }
    }
}

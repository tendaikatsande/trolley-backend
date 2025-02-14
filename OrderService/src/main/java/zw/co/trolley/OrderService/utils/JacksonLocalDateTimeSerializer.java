package zw.co.trolley.OrderService.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JacksonLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public JacksonLocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        long epochMilli = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        jsonGenerator.writeNumber(epochMilli); // Send as milliseconds
    }
}

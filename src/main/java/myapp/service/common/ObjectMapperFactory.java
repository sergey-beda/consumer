package myapp.service.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.regex.Pattern;

public final class ObjectMapperFactory {
    private static final String DATE_WITH_ZONE_REF_EXP = "^\\d{4}-\\d{2}-\\d{2}T.+";
    private static final String DATE_MILLI_WITHOUT_ZONE_REG_EXP = "^\\d{4}-\\d{2}-\\d{2}\\s([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).(\\d+)";
    private static final String DATE_MILLI_WITH_ZONE_REG_EXP = "^\\d{4}-\\d{2}-\\d{2}T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).(\\d+)";
    private static final String BOOLEAN_IS_WRONG = "Некорректнок значение Boolean %s";

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, Boolean.TRUE);
        objectMapper.registerModules(getJavaTimeModule(), getStringToBooleanModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        return objectMapper;
    }

    static JavaTimeModule getJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addKeySerializer(Instant.class, new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.getEpochSecond());
            }
        });
        javaTimeModule.addDeserializer(Instant.class, new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                try {
                    if (StringUtils.isEmpty(p.getValueAsString()) || StringUtils.isEmpty(p.getValueAsString().trim())) {
                        return  null;
                    }
                    String value = p.getValueAsString().trim();
                    if (Pattern.matches("-?\\d*", value)) {
                        return Instant.ofEpochSecond(p.getValueAsLong());
                    }
                    if (Pattern.matches(DATE_MILLI_WITH_ZONE_REG_EXP, value)) {
                        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(value).toInstant();
                    }
                    if (Pattern.matches(DATE_WITH_ZONE_REF_EXP, value)) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(value.substring(0,23)).toInstant();
                    }
                    if (Pattern.matches(DATE_MILLI_WITHOUT_ZONE_REG_EXP, value)) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.substring(0,23)).toInstant();
                    }
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value).toInstant();
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return javaTimeModule;
    }

    static SimpleModule getStringToBooleanModule() {
        SimpleModule strToBooleanModule = new SimpleModule();
        strToBooleanModule.addSerializer(Boolean.class, new JsonSerializer<Boolean>() {
            @Override
            public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeBoolean(value);
            }
        });
        strToBooleanModule.addDeserializer(Boolean.class, new JsonDeserializer<Boolean>() {
            @Override
            public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                String valueAsString = p.getValueAsString().toLowerCase();
                if ("0".equals(valueAsString) || "false".equals(valueAsString)) {
                    return false;
                }
                if ("1".equals(valueAsString) || "-1".equals(valueAsString) || "true".equals(valueAsString)) {
                    return true;
                }
                throw new IllegalArgumentException(String.format(BOOLEAN_IS_WRONG, valueAsString));
            }
        });
        return strToBooleanModule;
    }


    private ObjectMapperFactory(){}
}

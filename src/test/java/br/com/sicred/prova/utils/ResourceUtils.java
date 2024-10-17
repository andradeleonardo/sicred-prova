package br.com.sicred.prova.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceUtils {

    private static final Logger log = LoggerFactory.getLogger(ResourceUtils.class);

    private ResourceUtils() {
    }

    public static String objectToJson(final Object value) {
        String text = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
        }

        return text;
    }

    public static Object getObject(final String arquivo, final TypeReference<?> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object value = null;

        try {
            value = objectMapper.readValue(getResource(arquivo), typeReference);
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
        }

        return value;
    }

    public static Object getObject(final String arquivo, final Class clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Object value = null;

        try {
            value = objectMapper.readValue(getResource(arquivo), clazz);
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
        }

        return value;
    }

    public static <T> List<T> getListObject(final String arquivo, final Class clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T[] value = null;

        try {
            Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + clazz.getName() + ";");
            value = objectMapper.readValue(getResource(arquivo), arrayClass);
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
        }

        return Arrays.asList(value);
    }

    public static String getResource(final String arquivo) {
        String text = null;

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(arquivo)) {
            text = (new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));
        } catch (Exception var7) {
            log.error(var7.getMessage(), var7);
        }

        return text;
    }

}

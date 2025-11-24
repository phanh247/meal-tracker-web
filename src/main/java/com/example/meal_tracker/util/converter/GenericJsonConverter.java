package com.example.meal_tracker.util.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class GenericJsonConverter<T> implements AttributeConverter<T, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final TypeReference<T> typeRef;

    public GenericJsonConverter(TypeReference<T> typeRef) {
        this.typeRef = typeRef;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalStateException("JSON serialization error", e);
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return mapper.readValue("null", typeRef);
            }
            return mapper.readValue(dbData, typeRef);
        } catch (Exception e) {
            throw new IllegalStateException("JSON deserialization error", e);
        }
    }
}

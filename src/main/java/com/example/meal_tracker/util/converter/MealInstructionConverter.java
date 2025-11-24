package com.example.meal_tracker.util.converter;

import com.example.meal_tracker.dto.MealInstruction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = false)
public class MealInstructionConverter implements AttributeConverter<List<MealInstruction>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<MealInstruction> list) {
        try {
            return mapper.writeValueAsString(list);  // JSON String
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not convert meal instructions to JSON.", e);
        }
    }

    @Override
    public List<MealInstruction> convertToEntityAttribute(String json) {
        try {
            if (json == null) return new ArrayList<>();
            return mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(List.class, MealInstruction.class));
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON read error", e);
        }
    }
}

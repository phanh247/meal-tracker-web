package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.dto.request.IngredientRequest;
import com.example.meal_tracker.dto.response.IngredientResponse;
import com.example.meal_tracker.entity.Ingredient;
import com.example.meal_tracker.exception.IngredientManagementException;
import com.example.meal_tracker.repository.IngredientRepository;
import com.example.meal_tracker.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private IngredientResponse mapToResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .calories(ingredient.getCalories())
                .description(ingredient.getDescription())
                .createdAt(ingredient.getCreatedAt())
                .updatedAt(ingredient.getUpdatedAt())
                .build();
    }

    @Override
    public IngredientResponse createIngredient(IngredientRequest request) throws IngredientManagementException {
        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .calories(request.getCalories())
                .description(request.getDescription())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        ingredient = ingredientRepository.save(ingredient);
        System.out.println(ingredient);

        return mapToResponse(ingredient);
    }

    @Override
    public Page<IngredientResponse> getAllIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public IngredientResponse getIngredientById(Long id) throws IngredientManagementException {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientManagementException("Ingredient not found"));
        return mapToResponse(ingredient);
    }

    @Override
    public IngredientResponse updateIngredient(Long id, IngredientRequest request) throws IngredientManagementException {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientManagementException("Ingredient not found"));

        ingredient.setName(request.getName());
        ingredient.setCalories(request.getCalories());
        ingredient.setDescription(request.getDescription());
        ingredient.setUpdatedAt(System.currentTimeMillis());

        ingredient = ingredientRepository.save(ingredient);
        return mapToResponse(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) throws IngredientManagementException {
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientManagementException("Ingredient not found");
        }
        ingredientRepository.deleteById(id);
    }

    @Override
    public List<IngredientResponse> searchIngredients(String query) {
        if (query == null || query.isEmpty()) {
            return ingredientRepository.findAll().stream()
                    .map(IngredientResponse::fromEntity)
                    .collect(Collectors.toList());
        }
        return ingredientRepository.findByNameContainingIgnoreCase(query).stream()
                .map(IngredientResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

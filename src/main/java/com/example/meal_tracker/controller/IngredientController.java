package com.example.meal_tracker.controller;

import com.example.meal_tracker.dto.request.IngredientRequest;
import com.example.meal_tracker.dto.response.IngredientResponse;
import com.example.meal_tracker.exception.IngredientManagementException;
import com.example.meal_tracker.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestBody IngredientRequest request) throws IngredientManagementException {
        return ResponseEntity.ok(ingredientService.createIngredient(request));
    }

    @GetMapping
    public ResponseEntity<Page<IngredientResponse>> getAllIngredients(Pageable pageable) {
        return ResponseEntity.ok(ingredientService.getAllIngredients(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(@PathVariable Long id) throws IngredientManagementException {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(
            @PathVariable Long id,
            @RequestBody IngredientRequest request) throws IngredientManagementException {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) throws IngredientManagementException {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}

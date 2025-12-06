package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddMealPlanItemRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanItemRequest;
import com.example.meal_tracker.dto.response.MealPlanItemResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.entity.MealPlanItem;
import com.example.meal_tracker.repository.MealPlanRepository;
import com.example.meal_tracker.repository.MealRepository;
import com.example.meal_tracker.repository.MealPlanItemRepository;
import com.example.meal_tracker.util.converter.DtoConverter;

import com.example.meal_tracker.service.MealPlanItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.meal_tracker.common.ErrorConstant.MEAL_PLAN_ITEM_NOT_FOUND;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanItemServiceImpl implements MealPlanItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanItemServiceImpl.class);

    private final MealPlanItemRepository mealPlanItemRepository;
    private final MealPlanRepository mealPlanRepository;
    private final MealRepository mealRepository;

    @SuppressWarnings("null")
    public MealPlanItemResponse addNewMealPlanItem(AddMealPlanItemRequest addMealPlanItemRequest)
            throws BadRequestException {

        // Find if mealPlanId not found
        Optional<MealPlan> existingMealPlan = mealPlanRepository.findById(addMealPlanItemRequest.mealPlanId);
        if (existingMealPlan.isEmpty()) {
            LOGGER.info("Meal plan with id '{}' not found.", addMealPlanItemRequest.mealPlanId);
            throw new BadRequestException(
                    String.format(ErrorConstant.MEAL_PLAN_NOT_FOUND, addMealPlanItemRequest.mealPlanId));
        }

        // Find if mealId not found
        Optional<Meal> existingMeal = mealRepository.findById(addMealPlanItemRequest.mealId);
        if (existingMeal.isEmpty()) {
            LOGGER.info("Meal with id '{}' not found.", addMealPlanItemRequest.mealId);
            throw new BadRequestException(
                    String.format(ErrorConstant.MEAL_NOT_FOUND, addMealPlanItemRequest.mealId));
        }

        // Check if mealDate is invalid
        if ((addMealPlanItemRequest.getMealDate().isBefore(existingMealPlan.get().getStartDate().toLocalDate()))
                || (addMealPlanItemRequest.getMealDate().isAfter(existingMealPlan.get().getEndDate().toLocalDate()))) {
            LOGGER.info("Meal date is invalid.");
            throw new BadRequestException(
                    String.format(ErrorConstant.INVALID_MEAL_DATE_PARAM, addMealPlanItemRequest.mealDate));
        }

        MealPlanItem newMealPlanItemEntity = DtoConverter.convertToEntity(addMealPlanItemRequest);
        mealPlanItemRepository.save(newMealPlanItemEntity);
        return DtoConverter.convertToDto(newMealPlanItemEntity);
    }

    @Override
    public void updateMealPlanItem(Long mealPlanItemId, UpdateMealPlanItemRequest request)
            throws BadRequestException {

        // check if meal plan item id exists
        Optional<MealPlanItem> existingMealPlanItem = mealPlanItemRepository.findById(mealPlanItemId);
        if (existingMealPlanItem.isEmpty()) {
            LOGGER.info("Meal plan item with id '{}' not found.", mealPlanItemId);
            throw new BadRequestException(
                    String.format(ErrorConstant.MEAL_PLAN_ITEM_NOT_FOUND, mealPlanItemId));
        }

        MealPlan mealPlan = existingMealPlanItem.get().getMealPlan();
        // Check if mealDate is invalid
        if ((request.getMealDate().isBefore(mealPlan.getStartDate().toLocalDate()))
                || (request.getMealDate().isAfter(mealPlan.getEndDate().toLocalDate()))) {
            LOGGER.info("Meal date is invalid.");
            throw new BadRequestException(
                    String.format(ErrorConstant.INVALID_MEAL_DATE_PARAM, request.mealDate));
        }

        mealPlanItemRepository.findById(mealPlanItemId).map(mealPlanItem -> {
            mealPlanItem.setMealType(request.getMealType());
            mealPlanItem.setMealDate(Date.valueOf(request.getMealDate()));
            return mealPlanItemRepository.save(mealPlanItem);
        });
    }

    @Override
    public Page<MealPlanItemResponse> getMealPlanItems(Pageable pageable, Long mealPlanId, LocalDate date) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        if (date != null) {
            Page<MealPlanItem> mealPlanItems = mealPlanItemRepository.findByMealPlanIdAndDate(mealPlanId,
                    Date.valueOf(date));
            return mealPlanItems.map(DtoConverter::convertToDto);
        }
        Page<MealPlanItem> mealPlanItems = mealPlanItemRepository.findByMealPlanId(pageable, mealPlanId);
        return mealPlanItems.map(DtoConverter::convertToDto);
    }

    @Override
    public void deleteMealPlanItem(Long mealPlanItemId) throws BadRequestException {
        if (mealPlanItemRepository.findById(mealPlanItemId).isEmpty()) {
            LOGGER.info(MEAL_PLAN_ITEM_NOT_FOUND, mealPlanItemId);
            throw new BadRequestException(String.format(MEAL_PLAN_ITEM_NOT_FOUND, mealPlanItemId));
        }
        mealPlanItemRepository.deleteById(mealPlanItemId);
    };
}

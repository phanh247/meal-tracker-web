package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.repository.MealPlanRepository;
import com.example.meal_tracker.service.MealPlanService;
import com.example.meal_tracker.util.converter.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.meal_tracker.common.ErrorConstant.MEAL_PLAN_NOT_FOUND;

import java.sql.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanServiceImpl.class);

    private final MealPlanRepository mealPlanRepository;

    public MealPlanResponse addNewMealPlan(AddMealPlanRequest addMealPlanRequest) throws BadRequestException {
        MealPlan newMealPlanEntity = DtoConverter.convertToEntity(addMealPlanRequest);
        mealPlanRepository.save(newMealPlanEntity);
        return DtoConverter.convertToDto(newMealPlanEntity);
    }

    @Override
    public void updateMealPlan(Long mealPlanId, UpdateMealPlanRequest updateMealPlanRequest)
            throws BadRequestException {

        if (mealPlanRepository.findById(mealPlanId).isEmpty()) {
            LOGGER.info(MEAL_PLAN_NOT_FOUND, mealPlanId);
            throw new BadRequestException(String.format(MEAL_PLAN_NOT_FOUND, mealPlanId));
        }

        mealPlanRepository.findById(mealPlanId).map(mealPlan -> {
            mealPlan.setName(updateMealPlanRequest.getName());
            mealPlan.setTargetCalories(updateMealPlanRequest.getTargetCalories());
            mealPlan.setStartDate(Date.valueOf(updateMealPlanRequest.getStartDate()));
            mealPlan.setEndDate(Date.valueOf(updateMealPlanRequest.getEndDate()));
            mealPlan.setNote(updateMealPlanRequest.getNote());
            mealPlan.setPlanType(updateMealPlanRequest.getPlanType());
            mealPlan.setIsActive(updateMealPlanRequest.getIsActive());

            return mealPlanRepository.save(mealPlan);
        });

    }

    @Override
    public void deleteMealPlan(Long mealPlanId) throws BadRequestException {
        if (mealPlanRepository.findById(mealPlanId).isEmpty()) {
            LOGGER.info(MEAL_PLAN_NOT_FOUND, mealPlanId);
            throw new BadRequestException(String.format(MEAL_PLAN_NOT_FOUND, mealPlanId));
        }
        mealPlanRepository.deleteById(mealPlanId);
    }

//    @Override
//    public Page<MealPlanResponse> getMealPlans(Pageable pageable, Long userId) {
//        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
//        Page<MealPlan> mealPlans = mealPlanRepository.findByUserId(pageable, userId);
//        return mealPlans.map(DtoConverter::convertToDto);
//    }
}

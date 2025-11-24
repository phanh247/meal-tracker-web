package com.example.meal_tracker.service.impl;

import com.example.meal_tracker.common.ErrorConstant;
import com.example.meal_tracker.dto.request.AddMealPlanRequest;
import com.example.meal_tracker.dto.request.AddMealPlanDayRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanRequest;
import com.example.meal_tracker.dto.request.UpdateMealPlanDayRequest;
import com.example.meal_tracker.dto.response.CategoryResponse;
import com.example.meal_tracker.dto.response.MealPlanResponse;
import com.example.meal_tracker.dto.response.MealPlanDayResponse;
import com.example.meal_tracker.entity.Meal;
import com.example.meal_tracker.entity.MealPlan;
import com.example.meal_tracker.entity.MealPlanDay;
import com.example.meal_tracker.repository.MealPlanRepository;
import com.example.meal_tracker.repository.MealPlanDayRepository;
import com.example.meal_tracker.service.MealPlanService;
import com.example.meal_tracker.service.MealPlanDayService;
import com.example.meal_tracker.util.ConverterUtil;
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
public class MealPlanDayServiceImpl implements MealPlanDayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanDayServiceImpl.class);

    private final MealPlanDayRepository mealPlanDayRepository;
    private final MealPlanRepository mealPlanRepository;

    @SuppressWarnings("null")
    public MealPlanDayResponse addNewMealPlanDay(AddMealPlanDayRequest addMealPlanDayRequest)
            throws BadRequestException {

        // Find if mealPlanId not found
        Optional<MealPlan> existingMealPlan = mealPlanRepository.findById(addMealPlanDayRequest.mealPlanId);
        if (existingMealPlan == null) {
            LOGGER.info("Meal plan with id '{}' not found.", addMealPlanDayRequest.mealPlanId);
            throw new BadRequestException(
                    String.format(ErrorConstant.MEAL_PLAN_NOT_FOUND, addMealPlanDayRequest.mealPlanId));
        }

        MealPlanDay newMealPlanDayEntity = ConverterUtil.convertToEntity(addMealPlanDayRequest);
        mealPlanDayRepository.save(newMealPlanDayEntity);
        return ConverterUtil.convertToDto(newMealPlanDayEntity);
    }

    @SuppressWarnings("null")
    @Override
    public void updateMealPlanDay(Long mealPlanDayId, UpdateMealPlanDayRequest request)
            throws BadRequestException {

        // check if meal plan id exists
        if (mealPlanRepository.findById(request.mealPlanId).isEmpty()) {
            LOGGER.info("Meal plan with id '{}' not found.", request.mealPlanId);
            throw new BadRequestException(String.format(ErrorConstant.MEAL_PLAN_NOT_FOUND, request.mealPlanId));
        }

        // Tìm mealPlanDay có mealPlanId đó
        Optional<MealPlanDay> mealPlanDay = mealPlanDayRepository.findById(mealPlanDayId);

        // Kiểm tra mealPlanDayId
        if (mealPlanDay.isEmpty()) {
            // Log thông tin
            LOGGER.info("Meal plan day with id '{}' not found.", mealPlanDayId);
            // Trả lỗi
            throw new BadRequestException(String.format(ErrorConstant.MEAL_PLAN_DAY_NOT_FOUND, mealPlanDayId));
        }

        // cập nhật lại giá trị mới
        MealPlanDay existingMealPlanDay = mealPlanDay.get();
        existingMealPlanDay.setDate(Date.valueOf(request.getDate()));
        existingMealPlanDay.setMealPlanId(request.getMealPlanId());

        // lưu lại giá trị mới xuống db
        mealPlanDayRepository.save(existingMealPlanDay);

    }

    @SuppressWarnings("null")
    @Override
    public void deleteMealPlanDay(Long mealPlanDayId) throws BadRequestException {
        if (mealPlanDayRepository.findById(mealPlanDayId).isEmpty()) {
            LOGGER.info(MEAL_PLAN_DAY_NOT_FOUND, mealPlanDayId);
            throw new BadRequestException(String.format(MEAL_PLAN_NOT_FOUND, mealPlanDayId));
        }
        mealPlanRepository.deleteById(mealPlanDayId);
    }

    @Override
    public Page<MealPlanDayResponse> getMealPlans(Pageable pageable, Long userId) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<MealPlan> mealPlans = mealPlanRepository.findByUserId(pageable, userId);
        return mealPlans.map(ConverterUtil::convertToDto);
    }
}

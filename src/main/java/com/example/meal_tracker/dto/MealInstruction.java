package com.example.meal_tracker.dto;

public class MealInstruction {
    private int step;
    private String instruction;

    public MealInstruction() {
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "MealInstruction{" +
                "instruction='" + instruction + '\'' +
                ", step=" + step +
                '}';
    }
}

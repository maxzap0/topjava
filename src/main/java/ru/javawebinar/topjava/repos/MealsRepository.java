package ru.javawebinar.topjava.repos;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealsRepository {
    void save(MealTo mealTo);
    void delete(Integer id);
    List<Meal> getAll();
    Meal getOne(Integer id);
}

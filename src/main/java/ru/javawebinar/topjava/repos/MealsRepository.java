package ru.javawebinar.topjava.repos;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepository {
    void save(Meal meal);
    void delete(Integer id);
    List<Meal> getAll();
    Meal getOne(Integer id);
}

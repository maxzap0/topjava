package ru.javawebinar.topjava.repos;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsListInMemoryRepositoryImp implements MealsRepository{

    List<Meal> meals = new CopyOnWriteArrayList<>();
    AtomicInteger ids = new AtomicInteger(0);

    @Override
    public synchronized void save(Meal mealTo) {
        if (mealTo.getId() == null){
            Meal meal = new Meal(mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
            meal.setId(ids.incrementAndGet());
            meals.add(meal);
        } else {
            Meal meal = getOne(mealTo.getId());
            meal.setCalories(mealTo.getCalories());
            meal.setDateTime(mealTo.getDateTime());
            meal.setDescription(mealTo.getDescription());
        }

    }

    @Override
    public synchronized void delete(Integer id) {
        meals.removeIf(meal -> meal.getId().equals(id));
    }

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public Meal getOne(Integer id) {
        return meals.stream()
                .filter(meal -> meal.getId().equals(id))
                .findFirst().orElseThrow(()->new NoSuchElementException("no element with id"));
    }
}

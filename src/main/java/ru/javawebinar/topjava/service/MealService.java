package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
       return repository.save(new Meal(
               meal.getId(),
               meal.getDateTime(),
               meal.getDescription(),
               meal.getCalories(),
               userId
       ));
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id) ;
    }

    public void delete(int id, int userId){
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Collection<Meal> getAll(int userId){
        return repository.getAll(userId);
    }

    public List<MealTo> getAll(String startDate, String startTime, String endDate, String endTime, int authUserId) {
        LocalTime start = (startTime==null || startTime.isEmpty()) ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime end = endTime==null || endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime);
        return MealsUtil.getTos(repository.getAll(authUserId, startDate, endDate), SecurityUtil.authUserCaloriesPerDay()).stream()
                .filter(m-> DateTimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), start, end))
                .collect(Collectors.toList());
    }

    public void update(Meal meal, int userId){
        if (meal.getUserId()==userId) {
            checkNotFoundWithId(repository.save(meal), meal.getId());
        }
    }
}
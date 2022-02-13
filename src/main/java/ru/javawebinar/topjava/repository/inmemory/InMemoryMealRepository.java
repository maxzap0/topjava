package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(meal.getId()).getUserId() == meal.getUserId() ?
                repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) :
                null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.get(id).getUserId() == userId && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(id).getUserId()==userId ? repository.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(m->m.getUserId()==userId)
                .sorted((m1,m2)->m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(int authUserId, String startDate, String endDate) {
        LocalDate start = startDate==null || startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate end = endDate==null || endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate);
        return getAll(authUserId)
                .stream()
                .filter(m->m.getDateTime().toLocalDate().isAfter(start) || m.getDateTime().toLocalDate().equals(start))
                .filter(m->m.getDateTime().toLocalDate().isBefore(end) || m.getDateTime().toLocalDate().equals(end))
                .collect(Collectors.toList());
    }
}
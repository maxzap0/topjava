package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

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
        synchronized (this) {
            return repository.get(meal.getId()).getUserId() == meal.getUserId() ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
        }
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
}


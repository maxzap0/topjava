package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

import java.util.List;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAllDateTimeFilter(String startDate, String startTime, String endDate, String endTime){
        log.info("get all with filter");
        return service.getAll(startDate, startTime, endDate, endTime, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll(){
        log.info("get all meals");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public MealTo get(int id){
        log.info("get id={}", id);
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),MealsUtil.DEFAULT_CALORIES_PER_DAY).stream()
                .filter(m->m.getId()==id)
                .findFirst()
                .orElseThrow(()-> new NotFoundException("meal not found with id"+id));
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id){
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }
}
package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm")LocalTime startTime,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endDate,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm")LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    //@GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return super.getBetween(start.toLocalDate(), start.toLocalTime(), end.toLocalDate(), end.toLocalTime());
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }
}
package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
        })
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(100003, 100000);
    }

    @Test
    public void delete() {
        mealService.delete(100003, 100000);
        assertThrows(NotFoundException.class, ()->mealService.get(100003, 100000));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.parse("2022-02-20");
        LocalDate end = LocalDate.parse("2022-02-20");
        System.out.println(mealService.getBetweenInclusive(start, end, 100000));
    }

    @Test
    public void getAll() {
        System.out.println(mealService.getAll(100000));
        assertNotNull(mealService.getAll(1));
    }

    @Test
    public void update() {
        Meal meal = new Meal(100003, LocalDateTime.now(), "Завтрак из Теста", 500);
        mealService.update(meal, 100000);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.now(), "ТестовыйОбед", 500);
        mealService.create(meal, 100000);
    }
}
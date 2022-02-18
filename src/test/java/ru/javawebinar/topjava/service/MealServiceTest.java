package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

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
        Meal meal = mealService.get(MEAL1_ID, USER_ID);
        assertThat(meal).usingRecursiveComparison().isEqualTo(MEAL1);
    }

    @Test
    public void getNotOwnerUser() {
        Assert.assertThrows(NotFoundException.class, ()->mealService.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getWithIncorrectId() {
        Assert.assertThrows(NotFoundException.class, ()->mealService.get(INCORRECT_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        mealService.delete(MEAL2_ID, USER_ID);
        assertThrows(NotFoundException.class, ()->mealService.get(MEAL2_ID, USER_ID));
    }

    @Test
    public void deleteNotOwner() {
        assertThrows(NotFoundException.class, ()->mealService.get(MEAL2_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = mealService.getBetweenInclusive(START, END, USER_ID);
        assertThat(meals).usingRecursiveComparison().isEqualTo(FILTER_DATE_LIST);
    }

    @Test()
    public void getBetweenInclusiveEmptyResult() {
        List<Meal> meals = mealService.getBetweenInclusive(START, END, INCORRECT_ID);
        assertThat(meals).usingRecursiveComparison().isEqualTo(Collections.emptyList());
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertThat(meals).usingRecursiveComparison().isEqualTo(ALL_LIST);
    }

    @Test
    public void getAllIncorrectId() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        assertThat(meals).usingRecursiveComparison().isNotEqualTo(ALL_LIST);
    }

    @Test
    public void create() {
        Meal created = mealService.create(newMeal(), ADMIN_ID);
        Meal newMeal = newMeal();
        Integer createdId = created.getId();
        newMeal.setId(createdId);
        assertThat(newMeal).usingRecursiveComparison().isEqualTo(created);
    }

    @Test
    public void duplicateDateTimeAndUserId() {
        mealService.create(newMeal(), ADMIN_ID);
        Meal meal = newMeal();
        assertThrows(DataAccessException.class, () ->
                mealService.create(meal, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = updateMeal();
        mealService.update(updated, USER_ID);
        Meal meal = mealService.get(updated.getId(), USER_ID);
        assertThat(meal).usingRecursiveComparison().isEqualTo(updated);
    }

    @Test
    public void updateNotOwner() {
        Meal updated = updateMeal();
        Assert.assertThrows(NotFoundException.class, ()->mealService.update(updated, ADMIN_ID));
    }

}
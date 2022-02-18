package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

public class MealTestData {
    public static final int MEAL1_ID = GUEST_ID+1;
    public static final int MEAL2_ID = GUEST_ID+2;
    public static final int MEAL3_ID = GUEST_ID+3;
    public static final int MEAL4_ID = GUEST_ID+4;
    public static final int INCORRECT_ID = 999;

    public static final Meal MEAL1 = new Meal(MEAL1_ID,LocalDateTime.parse("2022-02-19T09:19:55"),"Завтрак",1000);
    public static final Meal MEAL2 = new Meal(MEAL2_ID,LocalDateTime.parse("2022-02-19T12:00:55"),"Обед",1500);
    public static final Meal MEAL3 = new Meal(MEAL3_ID,LocalDateTime.parse("2022-02-19T20:00:55"),"Ужин",900);
    public static final Meal MEAL4 = new Meal(MEAL4_ID,LocalDateTime.parse("2022-02-20T20:00:55"),"Ужин",900);

    public static final List<Meal> FILTER_DATE_LIST = new ArrayList<>(Arrays.asList(MEAL1,MEAL2,MEAL3));
    public static final List<Meal> ALL_LIST = new ArrayList<>(Arrays.asList(MEAL1,MEAL2,MEAL3,MEAL4));
    public static final LocalDate START = LocalDate.parse("2022-02-19");
    public static final LocalDate END = LocalDate.parse("2022-02-19");

    public static Meal newMeal() {return new Meal(null, LocalDateTime.parse("1995-01-01T20:20:20"), "НоваяЕда", 1000);}
    public static Meal updateMeal(){
        return new Meal(MEAL4_ID,LocalDateTime.parse("2022-02-20T20:00:55"),"УжинUpdate",900);
    }
}

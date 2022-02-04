package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repos.MealsListInMemoryRepositoryImp;
import ru.javawebinar.topjava.repos.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    public static final Logger log = getLogger(MealServlet.class);
    MealsRepository mealsRepository = new MealsListInMemoryRepositoryImp();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward = "";
        String action = request.getParameter("action");

        if (action!=null) {
            if (action.equalsIgnoreCase("delete")) {
                mealsRepository.delete(Integer.valueOf(request.getParameter("mealId")));
                forward = "meals.jsp";
                request.setAttribute("meals",
                        MealsUtil.filteredByStreams( mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
            } else if (action.equalsIgnoreCase("update")) {
                forward = "meal.jsp";
                Integer id = Integer.valueOf(request.getParameter("mealId"));
                Meal meal = mealsRepository.getOne(id);
                request.setAttribute("meal", meal);
            }
        } else {
            //ToDo delete this after added metod save
            mealsRepository.save(new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                    "Завтрак", 500, false));
            mealsRepository.save(new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                    "Обед", 1000, false));
            forward = "/meals.jsp";
            request.setAttribute("meals",
                    MealsUtil.filteredByStreams( mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
        }

        log.debug("do Get from {}",
                MealsUtil.filteredByStreams(
                        mealsRepository.getAll(),
                        LocalTime.of(0, 0),
                        LocalTime.of(23, 59),
                        2000));
            request.getRequestDispatcher(forward).forward(request, response);

        //request.set
        //response.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repos.MealsListInMemoryRepositoryImp;
import ru.javawebinar.topjava.repos.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final String DESCRIPTION = "description";
    private static final String CALORIES = "calories";
    private static final String DATE_TIME = "dateTime";
    private static final String ID = "id";
    private static final String MEALS_PAGE = "meals.jsp";
    private static final String MEAL_PAGE = "meal.jsp";
    private static final String DELETE_ACTION = "delete";
    private static final String UPDATE_ACTION = "update";
    private static final String CREATE_ACTION = "create";
    private static final String NAME_ATRR_MEALS = "meals";
    private static final String SERVLET_MAPPING = "meals";

    public static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final MealsRepository mealsRepository = new MealsListInMemoryRepositoryImp();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward;
        String action = request.getParameter("action");

        if (action != null) {
            if (action.equalsIgnoreCase(DELETE_ACTION)) {
                mealsRepository.delete(Integer.valueOf(request.getParameter(ID)));
                log.debug("delete meal id={}", Integer.valueOf(request.getParameter(ID)));
                request.setAttribute(NAME_ATRR_MEALS,
                        MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
                response.sendRedirect(SERVLET_MAPPING);
            } else if (action.equalsIgnoreCase(UPDATE_ACTION)) {
                forward = MEAL_PAGE;
                Integer id = Integer.valueOf(request.getParameter(ID));
                Meal meal = mealsRepository.getOne(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(forward).forward(request, response);
            } else if (action.equalsIgnoreCase(CREATE_ACTION)) {
                forward = MEAL_PAGE;
                request.getRequestDispatcher(forward).forward(request, response);
            }
        } else {
            forward = MEALS_PAGE;
            request.setAttribute(NAME_ATRR_MEALS,
                    MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int calories;
        try {
            calories = Integer.parseInt(request.getParameter(CALORIES));
        } catch (Exception e) {
            calories = 0;
        }

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter(DATE_TIME), dateTimeFormatter),
                request.getParameter(DESCRIPTION),
                calories
        );

        if (request.getParameter(ID) != null && !request.getParameter(ID).isEmpty()) {
            meal.setId(Integer.valueOf(request.getParameter(ID)));
        }
        log.debug("save meal {} ", meal);
        mealsRepository.save(meal);
        request.setAttribute(NAME_ATRR_MEALS,
                MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
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

    private final static String DESCRIPTION = "description";
    private final static String CALORIES = "calories";
    private final static String DATE_TIME = "dateTime";
    private final static String ID = "id";
    private final static String MEALS_PAGE = "meals.jsp";
    private final static String MEAL_PAGE = "meal.jsp";
    private final static String DELETE_ACTION = "delete";
    private final static String UPDATE_ACTION = "update";
    private final static String CREATE_ACTION = "create";

    public static final Logger log = getLogger(MealServlet.class);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    MealsRepository mealsRepository = new MealsListInMemoryRepositoryImp();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward;
        String action = request.getParameter("action");

        if (action != null) {
            if (action.equalsIgnoreCase(DELETE_ACTION)) {
                mealsRepository.delete(Integer.valueOf(request.getParameter(ID)));
                request.setAttribute("meals",
                        MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
                response.sendRedirect("meals");
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
            request.setAttribute("meals",
                    MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("datetime {}", request.getParameter(DATE_TIME));
        log.debug("description {}", request.getParameter(DESCRIPTION));
        log.debug("calories {}", request.getParameter(CALORIES));
        log.debug("id {}", request.getParameter(ID));
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter(DATE_TIME), dateTimeFormatter),
                request.getParameter(DESCRIPTION),
                Integer.parseInt(request.getParameter(CALORIES))
        );

        if (request.getParameter(ID)!=null && !request.getParameter(ID).isEmpty()) {
            meal.setId(Integer.valueOf(request.getParameter(ID)));
        }
        mealsRepository.save(meal);
        request.setAttribute("meals",
                MealsUtil.filteredByStreams(mealsRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/topjava">Home</a>
<br/>
<hr/>
Edit Meal
<br/>
<form method="POST" action='meals' name="frmAddMeal">
    <table>
        <tbody>
        <tr>
            <td>
                Date :
            </td>
            <td>
                <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}" />"/>
            </td>
        </tr>
        <tr>
            <td>
                Description :
            </td>
            <td>
             <%--   <input type="text" name="description" value="<c:out value="${meal.description}" />"/>--%>
                <input type="text" name="description" value="${meal.description}"/>
            </td>
        </tr>
        <tr>
            <td>
                Calories :
            </td>
            <td>
                <input type="text" name="calories" value="<c:out value="${meal.calories}" />"/>
            </td>
        </tr>
        <tr style="display: none">
            <td>
                id :
            </td>
            <td>
                <input type="text" name="id" value="<c:out value="${meal.id}" />"/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Save"/>
                <a href="/meals">
                    <button> Cancel</button>
                </a>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>

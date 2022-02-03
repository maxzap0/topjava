<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <p><h2>Meals</h2></p>
    <p><a href="UserController?action=insert">Add Meals</a></p>
    <tbody>
    <c:forEach items="${meals}" var="meals">

        <tr style="color: red">

            <td>
                <fmt:parseDate value="${ meals.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }"/>
            </td>
            <td><c:out value="${meals.description}"/></td>
            <td><c:out value="${meals.calories}"/></td>
            <td>Update</td>
            <td>Delete</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

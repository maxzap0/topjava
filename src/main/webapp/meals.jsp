<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<a href="/topjava">Home</a>
<br/>
<hr/>
<h2>Meals</h2>
<a href="UserController?action=insert">Add Meals</a>
<br/>
<br/>
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
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meals">

        <c:if test="${meals.excess eq true}">
            <tr style="color: red">
        </c:if>
        <c:if test="${meals.excess ne true}">
            <tr style="color: green">
        </c:if>
        <td>
            <fmt:parseDate value="${meals.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                           type="both"/>
            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
        </td>
        <td><c:out value="${meals.description}"/></td>
        <td><c:out value="${meals.calories}"/></td>
        <td><a href="meals?action=delete&mealId=<c:out value="${meals.id}"/>">Delete</a></td>
        <td><a href="meals?action=update&mealId=<c:out value="${meals.id}"/>">Update</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

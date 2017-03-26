<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Result of XML-Parsing</title>
</head>
<body>
<p>Тип парсера:"${parser}"</p>
<table border="1">
    <tr>
        <th>Название</th>
        <th>Тип</th>
        <th>Начинка</th>
        <th>Ингредиенты</th>
        <th>Калорийность</th>
        <th>Энергетическая ценность</th>
    </tr>
    <c:forEach var="candy" items="${candySet}">
        <tr>
            <td>${candy.name}</td>
            <td>${candy.type}</td>
            <td>${candy.filled}</td>
            <td>${candy.ingredients}</td>
            <td>${candy.value}</td>
            <td>${candy.energy}</td>
        </tr>
    </c:forEach>
</table>
<a href="index.jsp">Назад</a>
</body>
</html>

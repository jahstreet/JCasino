<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${player == null}">
        <jsp:forward page="${pageContext.request.contextPath}/controller?command=goto_game_fruits"/>
    </c:when>
    <c:when test="${player != null && (
         player.account.status.status.toString().equals('BAN') ||
         player.account.status.status.toString().equals('UNACTIVE')
    )}">
        <c:choose>
            <c:when test="${locale!=null && locale.equals('en_US')}">
                <c:set var="errorMessage" value="Game is not allowed to your status." scope="request"/>
            </c:when>
            <c:otherwise>
                <c:set var="errorMessage" value="Игра не доступна для вашего статуса." scope="request"/>
            </c:otherwise>
        </c:choose>
        <jsp:forward page="${pageContext.request.contextPath}/controller?command=goto_index"/>
    </c:when>
    <c:otherwise>
        <form class="custom-form clearfix" name="setDemo" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="goto_game_fruits">
            <div class="input-inline-block">
                <input class="custom-checkbox" id="demo" type="checkbox" name="demo_play" value="demo"
                       title="<fmt:message key="fruits.setup.checkbox.demo.title"/>">
                <label for="demo"><fmt:message key="fruits.setup.checkbox.demo.label"/></label>
            </div>
            <input type="submit" value="<fmt:message key="fruits.setup.submit"/>">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
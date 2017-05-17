<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${player == null}">
    <jsp:forward page="${pageContext.request.contextPath}/controller?command=goto_game_fruits"/>
</c:if>
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
</body>
</html>
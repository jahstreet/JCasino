<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${admin != null}">
    <jsp:forward page="/admin/jcasino_admin.jsp"/>
</c:if>
<jsp:forward page="/pages/main.jsp"/>
</body>
</html>
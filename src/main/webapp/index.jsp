<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${admin != null}">
    <jsp:forward page="${pageContext.request.contextPath}/admin/jcasino_admin.jsp"/>
</c:if>
<jsp:forward page="${pageContext.request.contextPath}/pages/main.jsp"/>
</body>
</html>
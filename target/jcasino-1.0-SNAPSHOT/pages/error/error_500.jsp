<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Request from ${pageContext.errorData.requestURI} is failed<br/>
Servlet name or type: ${pageContext.errorData.servletName}<br/>
Status code: ${pageContext.errorData.statusCode}<br/>
Exception: ${pageContext.errorData.throwable}<br/>
Message from exception: ${pageContext.exception.message}<br/>
Error message: ${errorMessage}<br/>
<a href="${pageContext.request.contextPath}/controller?command=back_from_error">Back</a>
</body>
</html>
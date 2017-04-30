<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="ru_RU" scope="session"/>
<fmt:setBundle basename="prop.messages"/>
<html>
<head>
    <title><fmt:message key="name"/></title>
</head>
<body>
<form name="XML Parser" action="DoXMLParsing" method="POST" enctype="multipart/form-data">
    XML<input type="file" name="xmlfile"/>
    XSD<input type="file" name="xsdfile"/>
    <input type="radio" name="parser" value="sax" title="SAX"/>SAX
    <input type="radio" name="parser" value="dom" title="DOM"/>DOM
    <input type="radio" name="parser" value="stax" title="StAX"/>StAX
    <input type="submit" value="Parse">
</form>

</body>
</html>
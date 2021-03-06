<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="verify_passport cols col-8">
        <h2><fmt:message key="upload.passport.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="uploadPassport" method="POST" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/controller">
            <input type="file" name="scan" title="<fmt:message key="upload.passport.title"/>"
                   required>
            <input type="submit" value="<fmt:message key="upload.passport.submit"/>">
        </form>
    </section>
</main>
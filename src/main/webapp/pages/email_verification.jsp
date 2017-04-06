<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="email-verification cols col-8">
        <h2><fmt:message key="emailverification.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="verifyEmailForm" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="verify_email"/>
            <input type="text" name="email_code" value="" placeholder="<fmt:message key="emailverification.code.holder"/>"
                   pattern="[0-9]{8}" title="<fmt:message key="emailverification.code.title"/>" required>
            <input type="submit" value="<fmt:message key="emailverification.submit"/>">
        </form>
        <div class="custom-link">
            <a href="${pageContext.request.contextPath}/controller?command=send_email_code">
                <fmt:message key="emailverification.again"/>
            </a>
        </div>
    </section>
</main>
<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="replenish-account cols col-8">
        <h2><fmt:message key="replenishacc.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="replenishAccount" method="POST"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="replenish_account"/>
            <input type="text" name="amount" value="${amount_input}"
                   pattern="^[0-9]{1,7}\.?[0-9]{0,2}$"
                   title="<fmt:message key="replenishacc.amount.title"/>"
                   placeholder="<fmt:message key="replenishacc.amount.holder"/>"
                   required>
            <input id="password-input" type="password" name="password" value=""
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="replenishacc.password.title"/>"
                   placeholder="<fmt:message key="replenishacc.password.holder"/>"
                   required/>
            <input type="submit" value="<fmt:message key="replenishacc.submit"/>">
        </form>
    </section>
</main>
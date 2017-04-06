<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="pay-loan cols col-8">
        <h2><fmt:message key="payloan.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="payLoan" method="POST"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="pay_loan"/>
            <input type="text" name="amount" value="
    <c:choose>
        <c:when test="${player.account.currentLoan.rest.compareTo(player.account.balance) < 0}">
           ${player.account.currentLoan.rest}
        </c:when>
        <c:otherwise>
            ${player.account.balance}
        </c:otherwise>
    </c:choose>"
                   pattern="^[0-9]{1,7}\.?[0-9]{0,2}$"
                   title="<fmt:message key="payloan.amount.title"/>"
                   placeholder="<fmt:message key="payloan.amount.holder"/>"
                   required>
            <input id="password-input" type="password" name="password" value=""
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="payloan.password.title"/>"
                   placeholder="<fmt:message key="payloan.password.holder"/>"
                   required autofocus/>
            <input type="submit" value="<fmt:message key="payloan.submit"/>">
        </form>
        <script>
            var input = document.payLoan.elements[1],
                    value = input.value;
            input.value = value.trim();
        </script>
    </section>
</main>
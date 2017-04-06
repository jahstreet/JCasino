<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="take-loan cols col-8">
        <h2><fmt:message key="takeloan.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="takeLoan" method="POST"
              action="${pageContext.request.contextPath}/controller"
              onsubmit="return validateLoan()">
            <input type="hidden" name="command" value="take_loan"/>
            <input type="text" name="amount" value="${amount_input}"
                   pattern="^[0-9]{1,7}\.?[0-9]{0,2}$"
                   title="<fmt:message key="takeloan.amount.title"/> ${player.account.status.maxLoan}"
                   placeholder="<fmt:message key="takeloan.amount.holder"/>"
                   required autofocus>
            <input id="password-input" type="password" name="password" value=""
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="takeloan.password.title"/>"
                   placeholder="<fmt:message key="takeloan.password.holder"/>"
                   required/>
            <input type="submit" value="<fmt:message key="takeloan.submit"/>">
        </form>
        <div class="warning">
            <p><fmt:message key="takeloan.maxloan"/>: ${player.account.status.maxLoan}</p>
            <p><fmt:message key="takeloan.loanpercent"/>: ${player.account.status.loanPercent}%</p>
        </div>

        <script>
            var maxLoan = ${player.account.status.maxLoan},
                    form = document.takeLoan,
                    input = form.elements.amount,
                    INVALID_AMOUNT;
            if (locale == "en_US") {
                INVALID_AMOUNT = "You can't take loan more than " + maxLoan;
            } else {
                INVALID_AMOUNT = "Вы не можете взять кредит на сумму более " + maxLoan;
            }
            input.addEventListener("change", checkAmount, false);

            function validateLoan() {
                checkAmount();
                if (!form.checkValidity()) {
                    input.focus();
                    return false;
                }
                return true;
            }

            function checkAmount() {
                var amount = input.value;
                if (amount > maxLoan) {
                    input.setCustomValidity(INVALID_AMOUNT);
                } else {
                    input.setCustomValidity('');
                }
            }
        </script>
    </section>
</main>
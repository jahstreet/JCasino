<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="withdraw-money cols col-8">
        <h2><fmt:message key="withdraw.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="withdrawMoney" method="POST"
              action="${pageContext.request.contextPath}/controller"
              onsubmit="return validateWithdrawMoney()">
            <input type="hidden" name="command" value="withdraw_money"/>
            <input type="text" name="amount" value="${amount_input}"
                   pattern="^[0-9]{1,7}\.?[0-9]{0,2}$"
                   title="<fmt:message key="withdraw.amount.title"/> ${player.account.status.withdrawalLimit.subtract(player.account.thisMonthWithdrawal)}"
                   placeholder="<fmt:message key="withdraw.amount.holder"/>"
                   required autofocus>
            <input id="password-input" type="password" name="password" value=""
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="withdraw.password.title"/>"
                   placeholder="<fmt:message key="withdraw.password.holder"/>"
                   required/>
            <input type="submit" value="<fmt:message key="withdraw.submit"/>">
        </form>
        <div class="warning">
            <p>
                <fmt:message key="withdraw.maxwithdraw"/>: ${player.account.status.withdrawalLimit.subtract(player.account.thisMonthWithdrawal)}
            </p>
        </div>
        <script>
            var maxWithdrawal = ${player.account.status.withdrawalLimit.subtract(player.account.thisMonthWithdrawal)},
                    form = document.withdrawMoney,
                    input = form.elements.amount,
                    INVALID_AMOUNT;
            if (locale == "en_US") {
                INVALID_AMOUNT = "You can't withdraw more than " + maxWithdrawal;
            } else {
                INVALID_AMOUNT = "Вы не можете вывести более " + maxWithdrawal;
            }
            input.addEventListener("change", checkAmount, false);

            function validateWithdrawMoney() {
                checkAmount();
                if (!form.checkValidity()) {
                    input.focus();
                    return false;
                }
                return true;
            }

            function checkAmount() {
                var amount = input.value;
                if (amount > maxWithdrawal) {
                    input.setCustomValidity(INVALID_AMOUNT);
                } else {
                    input.setCustomValidity('');
                }
            }
        </script>
    </section>
</main>
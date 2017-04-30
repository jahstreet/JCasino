<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-transactions cols col-8">
        <h2><fmt:message key="manage.transactions.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="showTransactions" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_transactions">
            <select name="type" title="<fmt:message key="manage.transactions.option.title"/>" required>
                <option selected disabled value=""><fmt:message key="manage.transactions.option.title"/></option>
                <option name="replenish" value="replenish"><fmt:message key="transactions.type.replenish"/></option>
                <option name="withdraw" value="withdraw"><fmt:message key="transactions.type.withdraw"/></option>
                <option name="all" value="all"><fmt:message key="transactions.type.all"/></option>
            </select>
            <div class="input-block">
                <label for="operation-month"><fmt:message key="manage.transactions.input.month.label"/></label>
                <input id="operation-month" type="month" name="month" value="${month_input}"
                       title="<fmt:message key="manage.transactions.input.month.title"/>"
                       min="2017-01">
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="sortByAmount" type="checkbox" name="sort_by_amount"
                       value="sort_by_amount" title="<fmt:message key="manage.transactions.checkbox.sort.amount.title"/>">
                <label for="sortByAmount"><fmt:message key="manage.transactions.checkbox.sort.amount.title"/></label>
            </div>
            <input type="submit" value="<fmt:message key="manage.transactions.show.submit"/>"/>
        </form>
        <section class="table-section">
            <c:if test="${transactions != null}">
                <table class="custom-table">
                    <caption><fmt:message key="history.table.transactions.caption"/></caption>
                    <tr>
                        <th><fmt:message key="history.table.transactions.player.id"/></th>
                        <th><fmt:message key="history.table.transactions.date"/></th>
                        <th><fmt:message key="history.table.transactions.amount"/></th>
                        <th><fmt:message key="history.table.transactions.type"/></th>
                    </tr>
                    <c:forEach var="transaction" items="${transactions}">
                        <tr>
                            <td>
                                <div class="custom-link">
                                    <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${transaction.playerId}">
                                            ${transaction.playerId}
                                    </a>
                                </div>
                            </td>
                            <td>${j:formatDateTime(transaction.date, "dd.MM.yyyy HH:mm")}</td>
                            <td>${transaction.amount}</td>
                            <td><fmt:message key="transactions.type.${transaction.type.toString().toLowerCase()}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </section>

    </section>
</main>
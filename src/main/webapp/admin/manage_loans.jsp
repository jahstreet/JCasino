<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-loans cols col-8">
        <h2><fmt:message key="manage.loans.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="showLoans" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_loans">
            <div class="input-block">
                <label for="operation-month-acquire"><fmt:message key="manage.loans.input.month.aquire.label"/></label>
                <input id="operation-month-acquire" type="month" name="month_acquire" value="${month_acquire_input}"
                       title="<fmt:message key="manage.transactions.input.month.title"/>"
                       min="2017-01">
            </div>
            <div class="input-block">
                <label for="operation-month-expire"><fmt:message key="manage.loans.input.month.expire.label"/></label>
                <input id="operation-month-expire" type="month" name="month_expire" value="${month_expire_input}"
                       title="<fmt:message key="manage.transactions.input.month.title"/>"
                       min="2017-01">
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="sortByRest" type="checkbox" name="sort_by_rest"
                       value="sort_by_rest" title="<fmt:message key="manage.loans.checkbox.sort.rest.title"/>">
                <label for="sortByRest"><fmt:message key="manage.loans.checkbox.sort.rest.title"/></label>
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="filterNotPaid" type="checkbox" name="filter_not_paid"
                       value="filter_not_paid" title="<fmt:message key="manage.loans.checkbox.filter.paid.title"/>">
                <label for="filterNotPaid"><fmt:message key="manage.loans.checkbox.filter.paid.title"/></label>
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="filterOverdued" type="checkbox" name="filter_overdued"
                       value="filter_overdued" title="<fmt:message key="manage.loans.checkbox.filter.overdued.title"/>">
                <label for="filterOverdued"><fmt:message key="manage.loans.checkbox.filter.overdued.title"/></label>
            </div>
            <input type="submit" value="<fmt:message key="manage.loans.show.submit"/>"/>
        </form>
        <section class="table-section">
            <c:if test="${loans != null}">
                <table class="custom-table">
                    <caption><fmt:message key="history.table.loans.caption"/></caption>
                    <thead>
                    <tr>
                        <th><fmt:message key="history.table.loans.player.id"/></th>
                        <th><fmt:message key="history.table.loans.acquire"/></th>
                        <th><fmt:message key="history.table.loans.expire"/></th>
                        <th><fmt:message key="history.table.loans.percent"/></th>
                        <th><fmt:message key="history.table.loans.amount"/></th>
                        <th><fmt:message key="history.table.loans.rest"/></th>
                    </tr>
                    </thead>
                    <tbody id="itemContainer">
                    <c:forEach var="loan" items="${requestScope.loans}">
                        <tr>
                            <td>
                                <div class="custom-link">
                                    <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${loan.playerId}">
                                            ${loan.playerId}
                                    </a>
                                </div>
                            </td>
                            <td>${j:formatDate(loan.acquire, "dd.MM.yyyy")}</td>
                            <td>${j:formatDate(loan.expire, "dd.MM.yyyy")}</td>
                            <td>${loan.percent}</td>
                            <td>${loan.amount}</td>
                            <td>${loan.rest}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="holder"></div>
            </c:if>
        </section>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/manage_loan.js"></script>
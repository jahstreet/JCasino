<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="operation-history cols col-8">
        <h2><fmt:message key="history.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="showHistory" method="GET"
              action="${pageContext.request.contextPath}/controller"
              onsubmit="return true">
            <input type="hidden" name="command" value="show_history"/>
            <div class="input-block">
                <label for="operation-type"><fmt:message key="history.input.type.text"/></label>
                <select id="operation-type" name="type" title="" required>
                    <optgroup label="<fmt:message key="history.select.type.label"/>">
                        <option value="transactions"><fmt:message key="history.select.type.transactions"/></option>
                        <option value="loans"><fmt:message key="history.select.type.loans"/></option>
                        <option value="streaks"><fmt:message key="history.select.type.streaks"/></option>
                    </optgroup>
                </select>
            </div>
            <div class="input-block">
                <label for="operation-month"><fmt:message key="history.input.month.text"/></label>
                <input id="operation-month" type="month" name="month" value="${month_input}"
                       title="<fmt:message key="history.input.month.title"/>"
                       min="2017-01">
            </div>
            <input type="submit" value="<fmt:message key="history.submit"/>">
        </form>
        <hr/>
        <section class="table-section">
            <c:choose>
                <c:when test="${transactions != null}">
                    <table class="custom-table">
                        <caption><fmt:message key="history.table.transactions.caption"/></caption>
                        <thead>
                        <tr>
                            <th><fmt:message key="history.table.transactions.date"/></th>
                            <th><fmt:message key="history.table.transactions.amount"/></th>
                            <th><fmt:message key="history.table.transactions.type"/></th>
                        </tr>
                        </thead>
                        <tbody id="transactionItemContainer">
                        <c:forEach var="transaction" items="${requestScope.transactions}">
                            <tr>
                                <td>${j:formatDateTime(transaction.date, "dd.MM.yyyy HH:mm")}</td>
                                <td>${transaction.amount}</td>
                                <td><fmt:message
                                        key="transactions.type.${transaction.type.toString().toLowerCase()}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="transactionHolder"></div>
                </c:when>
                <c:when test="${loans != null}">
                    <table class="custom-table">
                        <caption><fmt:message key="history.table.loans.caption"/></caption>
                        <thead>
                        <tr>
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
                </c:when>
                <c:when test="${streaks != null}">
                    <div class="streakHolder"></div>
                    <table class="custom-table" id="streakItemContainer">
                        <caption><fmt:message key="history.table.streaks.caption"/></caption>
                        <thead>
                        <tr>
                            <th class="col-date" rowspan="2"><fmt:message key="history.table.streaks.date"/></th>
                            <th class="col-attr" rowspan="2"></th>
                            <th class="col-rolls" colspan="10"><fmt:message key="history.table.streaks.rolls"/></th>
                            <th class="col-total" rowspan="2"><fmt:message key="history.table.streaks.total"/></th>
                        </tr>
                        <tr>
                            <c:forEach var="i" begin="1" end="10">
                                <th class="col-roll">${i}</th>
                            </c:forEach>
                        </tr>
                        </thead>
                        <c:forEach var="streak" items="${streaks}">
                            <tbody>
                            <c:choose>
                                <c:when test="${streak.rolls!=null && !streak.rolls.isEmpty()}">
                                    <tr>
                                        <td rowspan="5">${j:formatDateTime(streak.date, "dd.MM.yyyy HH:mm")}</td>
                                        <td><fmt:message key="history.table.streaks.roll"/></td>
                                        <c:forEach var="roll" items="${streak.rolls}">
                                            <td><c:forEach var="reel" items="${roll.roll}"> ${reel} </c:forEach></td>
                                        </c:forEach>

                                        <td rowspan="5">${streak.total}</td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="history.table.streaks.offset"/></td>
                                        <c:forEach var="roll" items="${streak.rolls}">
                                            <td><c:forEach var="offset"
                                                           items="${roll.offset}"> ${offset} </c:forEach></td>
                                        </c:forEach>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="history.table.streaks.lines"/></td>
                                        <c:forEach var="roll" items="${streak.rolls}">
                                            <td>
                                                <c:forEach var="line" items="${roll.lines}" varStatus="status">
                                                    <c:if test="${line}"> ${status.index+1} </c:if>
                                                </c:forEach>
                                            </td>
                                        </c:forEach>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="history.table.streaks.bet"/></td>
                                        <c:forEach var="roll" items="${streak.rolls}">
                                            <td>${roll.bet}</td>
                                        </c:forEach>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="history.table.streaks.result"/></td>
                                        <c:forEach var="roll" items="${streak.rolls}">
                                            <td>${roll.result}</td>
                                        </c:forEach>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td rowspan="5">${j:formatDateTime(streak.date, "dd.MM.yyyy HH:mm")}</td>
                                        <td colspan="12" rowspan="5">
                                            <fmt:message key="history.table.streaks.not.finished"/>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </c:forEach>
                    </table>
                    <div class="streakHolder"></div>
                </c:when>
            </c:choose>
        </section>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/operation_history.js"></script>
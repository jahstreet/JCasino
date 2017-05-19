<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-streaks cols col-8">
        <h2><fmt:message key="manage.streaks.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="showStreaks" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_streaks">
            <div class="input-block">
                <label for="operation-month"><fmt:message key="manage.transactions.input.month.label"/></label>
                <input id="operation-month" type="month" name="month" value="${month_input}"
                       title="<fmt:message key="manage.transactions.input.month.title"/>"
                       min="2017-01">
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="sortByTotal" type="checkbox" name="sort_by_total"
                       value="sort_by_total" title="<fmt:message key="manage.streaks.checkbox.sort.total.title"/>">
                <label for="sortByTotal"><fmt:message key="manage.streaks.checkbox.sort.total.title"/></label>
            </div>
            <input type="submit" value="<fmt:message key="manage.streaks.show.submit"/>"/>
        </form>
        <section class="table-section">
            <c:if test="${streaks != null}">
                <div class="holder"></div>
                <table class="custom-table" id="itemContainer">
                    <caption><fmt:message key="history.table.streaks.caption"/></caption>
                    <thead>
                    <tr>
                        <th class="col-id" rowspan="2"><fmt:message key="history.table.streaks.player.id"/></th>
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
                                    <td rowspan="5">
                                        <div class="custom-link">
                                            <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${streak.playerId}">
                                                    ${streak.playerId}
                                            </a>
                                        </div>
                                    </td>
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
                                    <td rowspan="5">
                                        <div class="custom-link">
                                            <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${streak.playerId}">
                                                    ${streak.playerId}
                                            </a>
                                        </div>
                                    </td>
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
                <div class="holder"></div>
            </c:if>
        </section>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/manage_streak.js"></script>
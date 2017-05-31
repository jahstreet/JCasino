<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-loans cols col-8">
        <h2><fmt:message key="manage.players.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="showPlayers" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_players">
            <div class="input-block">
                <label for="player-id"><fmt:message key="manage.players.input.id.label"/></label>
                <input id="player-id" type="number" name="id" value=""
                       title="<fmt:message key="manage.players.input.id.title"/>"
                       placeholder="<fmt:message key="manage.players.input.id.holder"/>"
                       min="1">
            </div>
            <div class="input-block">
                <label for="month-withdrawal"><fmt:message key="manage.players.withdrawal.range"/></label>
                <input id="month-withdrawal" type="text" class="range-input" name="withdrawal" readonly/>
                <div id="slider-range-withdrawal"></div>
            </div>
            <div class="input-block">
                <label for="balance"><fmt:message key="manage.players.balance.range"/></label>
                <input id="balance" type="text" class="range-input" name="balance" readonly/>
                <div id="slider-range-balance"></div>
            </div>
            <div class="input-block">
                <select name="status" title="<fmt:message key="manage.player.account.status.option.title"/>">
                    <option selected disabled value="">
                        <fmt:message key="manage.player.account.status.header"/>
                    </option>
                    <option name="basic" value="basic">
                        <fmt:message key="player.account.status.basic"/>
                    </option>
                    <option name="vip" value="vip">
                        <fmt:message key="player.account.status.vip"/>
                    </option>
                    <option name="ban" value="ban">
                        <fmt:message key="player.account.status.ban"/>
                    </option>
                    <option name="unactive" value="unactive">
                        <fmt:message key="player.account.status.unactive"/>
                    </option>
                </select>
            </div>
            <div class="input-block">
                <select name="verification" title="<fmt:message key="manage.player.verification.status.option.title"/>">
                    <option selected disabled value="">
                        <fmt:message key="manage.player.verification.status.header"/>
                    </option>
                    <option name="verified" value="verified">
                        <fmt:message key="player.verification.status.verified"/>
                    </option>
                    <option name="not_verified" value="not_verified">
                        <fmt:message key="player.verification.status.not.verified"/>
                    </option>
                </select>
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="disableRanges" type="checkbox" name="disable_ranges"
                       value="disable_ranges"
                       title="<fmt:message key="manage.players.checkbox.disable.ranges.title"/>"
                       checked>
                <label for="disableRanges"><fmt:message key="manage.players.checkbox.disable.ranges.label"/></label>
            </div>
            <input type="submit" value="<fmt:message key="manage.players.show.submit"/>"/>
        </form>
        <section class="table-section">
            <c:if test="${requestScope.players != null && !requestScope.players.isEmpty()}">
                <hr/>
                <table class="custom-table">
                    <caption><fmt:message key="manage.players.table.caption"/></caption>
                    <thead>
                    <tr>
                        <th><fmt:message key="manage.players.table.id"/></th>
                        <th><fmt:message key="manage.players.table.email"/></th>
                        <th><fmt:message key="manage.players.table.account.status"/></th>
                        <th><fmt:message key="manage.players.table.verification.status"/></th>
                        <th><fmt:message key="manage.players.table.withdrarwal"/></th>
                        <th><fmt:message key="manage.players.table.balance"/></th>
                    </tr>
                    </thead>
                    <tbody id="itemContainer">
                    <c:forEach var="player" items="${requestScope.players}">
                        <tr>
                            <td>
                                <div class="custom-link">
                                    <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${player.id}">
                                            ${player.id}
                                    </a>
                                </div>
                            </td>
                            <td>${player.email}</td>
                            <td><fmt:message
                                    key="player.account.status.${player.account.status.status.toString().toLowerCase()}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${player.verification.status.toString().equals('VERIFIED')}">
                                        <fmt:message key="verification.ok"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="verification.no"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${player.account.thisMonthWithdrawal}" default="0"/></td>
                            <td><c:out value="${player.account.balance}" default="0"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="holder"></div>
            </c:if>
        </section>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/manage_players.js"></script>
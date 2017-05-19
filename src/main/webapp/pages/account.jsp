<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="account cols col-8">
        <dl class="player-account custom-dl">
            <h2><fmt:message key="account.account.header"/></h2>
            <dt><fmt:message key="account.balance"/></dt>
            <dd>${player.account.balance}</dd>

            <dt><fmt:message key="account.status"/></dt>
            <dd>${player.account.status.status}</dd>

            <dt><fmt:message key="account.betlimit"/></dt>
            <dd>${player.account.status.betLimit}</dd>

            <dt><fmt:message key="account.withdrawallimit"/></dt>
            <dd>${player.account.status.withdrawalLimit}</dd>

            <dt><fmt:message key="account.thismonthwithdrowal"/></dt>
            <dd>${player.account.thisMonthWithdrawal}</dd>

            <c:choose>
                <c:when test="${player.account.currentLoan == null}">
                    <dt><fmt:message key="account.loanpercent"/></dt>
                    <dd>${player.account.status.loanPercent}%</dd>

                    <dt><fmt:message key="account.maxloanamount"/></dt>
                    <dd>${player.account.status.maxLoan}</dd>
                </c:when>
                <c:otherwise>
                    <hr/>
                    <h2><fmt:message key="account.currentloan.header"/></h2>
                    <dt><fmt:message key="account.currentloan.amount"/></dt>
                    <dd>${player.account.currentLoan.amount}</dd>

                    <dt><fmt:message key="account.currentloan.acquire"/></dt>
                    <dd>${player.account.currentLoan.acquire}</dd>

                    <dt><fmt:message key="account.currentloan.expire"/></dt>
                    <dd>${player.account.currentLoan.expire}</dd>

                    <dt><fmt:message key="account.currentloan.percent"/></dt>
                    <dd>${player.account.currentLoan.percent}%</dd>

                    <dt><fmt:message key="account.currentloan.rest"/></dt>
                    <dd>${player.account.currentLoan.rest}</dd>
                </c:otherwise>
            </c:choose>
        </dl>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=goto_replenish_account">
                <fmt:message key="account.payment"/>
            </a>
        </div>
        <c:if test="${!player.account.status.status.toString().equals('BAN') &&
            !player.account.status.status.toString().equals('UNACTIVE')}">
            <c:choose>
                <c:when test="${player.account.currentLoan == null}">
                    <div class="custom-button">
                        <a href="${pageContext.request.contextPath}/controller?command=goto_take_loan">
                            <fmt:message key="account.takeloan"/>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="custom-button">
                        <a href="${pageContext.request.contextPath}/controller?command=goto_pay_loan">
                            <fmt:message key="account.payloan"/>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${!player.account.status.status.toString().equals('BAN') &&
                 !player.account.status.status.toString().equals('UNACTIVE') &&
                 player.account.status.withdrawalLimit.compareTo(player.account.thisMonthWithdrawal) > 0 &&
                 player.account.currentLoan == null &&
                 player.verification.status.toString().equals('VERIFIED')}">
            <div class="custom-button">
                <a href="${pageContext.request.contextPath}/controller?command=goto_withdraw_money">
                    <fmt:message key="account.withdraw"/>
                </a>
            </div>
        </c:if>
    </section>
</main>
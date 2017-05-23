<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-player cols col-8">
        <h2><fmt:message key="manage.player.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>

        <dl class="custom-dl clearfix">

            <%--profile--%>

            <dt><fmt:message key="profile.id"/></dt>
            <dd>${player.id}</dd>

            <dt><fmt:message key="profile.email"/></dt>
            <dd>${player.email}</dd>

            <dt><fmt:message key="profile.registerdate"/></dt>
            <dd>${player.registrationDate}</dd>

            <dt><fmt:message key="profile.fName"/></dt>
            <dd>${player.profile.fName}</dd>

            <dt><fmt:message key="profile.mName"/></dt>
            <dd>${player.profile.mName}</dd>

            <dt><fmt:message key="profile.lName"/></dt>
            <dd>${player.profile.lName}</dd>

            <dt><fmt:message key="profile.birthdate"/></dt>
            <dd>${player.profile.birthDate}</dd>

            <dt><fmt:message key="profile.passport"/></dt>
            <dd>${player.profile.passport}</dd>
            <hr/>

            <%--verification--%>

            <dt><fmt:message key="verification.profile"/></dt>
            <c:choose>
                <c:when test="${player.verification.profileVerified}">
                    <dd><fmt:message key="verification.ok"/></dd>
                </c:when>
                <c:otherwise>
                    <dd><fmt:message key="verification.no"/></dd>
                </c:otherwise>
            </c:choose>

            <dt><fmt:message key="verification.email"/></dt>
            <c:choose>
                <c:when test="${player.verification.emailVerified}">
                    <dd><fmt:message key="verification.ok"/></dd>
                </c:when>
                <c:otherwise>
                    <dd><fmt:message key="verification.no"/></dd>
                </c:otherwise>
            </c:choose>

            <dt><fmt:message key="verification.passportscan"/></dt>
            <dd>
                <c:choose>
                    <c:when test="${player.verification.scanVerified}">
                        <fmt:message key="verification.ok"/>
                    </c:when>
                    <c:when test="${!player.verification.scanVerified && player.verification.passport != null}">
                        <fmt:message key="verification.processing"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="verification.no"/>
                    </c:otherwise>
                </c:choose>
            </dd>
            <c:if test="${!player.verification.scanVerified && player.verification.passport != null}">
                <c:if test="${player.verification.emailVerified && player.verification.profileVerified}">
                    <div class="custom-link">
                        <a href="${pageContext.request.contextPath}/controller?command=verify_scan&id=${player.id}">
                            <fmt:message key="manage.player.verify.scan"/>
                        </a>
                    </div>
                </c:if>
                <div class="scan">
                    <img src="/image/${player.verification.passport}"
                         alt="${player.profile.fName}'s passport-scan">
                </div>
                <form class="cancel-scan-verification custom-form clearfix" name="cancelScanVerification"
                      method="GET" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="cancel_scan_verification">
                    <input type="hidden" name="id" value="${player.id}">
                    <textarea name="commentary" rows="4" maxlength="700" wrap="soft"
                              title="<fmt:message key="manage.player.verification.scan.cancel.textarea.title"/>"
                              placeholder="<fmt:message key="manage.player.verification.scan.cancel.textarea.holder"/>"
                              required>${commentary_input}</textarea>
                    <input type="submit"
                           value="<fmt:message key="manage.player.verification.scan.cancel.submit"/>"/>
                </form>
            </c:if>

            <dt><fmt:message key="verification.status"/></dt>
            <dd>
                <c:choose>
                    <c:when test="${player.verification.status.toString().equals('VERIFIED')}">
                        <fmt:message key="verification.ok"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="verification.no"/>
                    </c:otherwise>
                </c:choose>
            </dd>

            <c:if test="${player.verification.profileVerified
                && player.verification.emailVerified
                && player.verification.scanVerified}">
                <dt><fmt:message key="verification.date"/></dt>
                <dd>${j:formatDateTime(player.verification.verificationDate, "dd.MM.yyyy HH:mm")}</dd>
            </c:if>

            <c:if test="${player.verification.commentary != null}">
                <dt><fmt:message key="verification.commentary"/></dt>
                <dd>${player.verification.adminId}: "${player.verification.commentary}"</dd>
            </c:if>

            <hr/>

            <%--account--%>

            <dt><fmt:message key="account.balance"/></dt>
            <dd>${player.account.balance}</dd>

            <dt><fmt:message key="account.status"/></dt>
            <dd>
                <fmt:message key="player.account.status.${player.account.status.status.toString().toLowerCase()}"/>
            </dd>

            <c:if test="${player.account.status.adminId != null && player.account.status.commentary != null}">
                <dt><fmt:message key="account.commentary"/></dt>
                <dd>${player.account.status.adminId}: "${player.account.status.commentary}"</dd>
            </c:if>

            <form class="change-account-status custom-form clearfix" name="changeAccountStatus" method="GET"
                  action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="change_account_status">
                <input type="hidden" name="id" value="${player.id}">
                <select name="status" title="<fmt:message key="manage.player.account.status.option.title"/>"
                        required>
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
                <textarea name="commentary" rows="4" maxlength="700" wrap="soft"
                          title="<fmt:message key="manage.player.account.status.textarea.title"/>"
                          placeholder="<fmt:message key="manage.player.account.status.textarea.holder"/>">${commentary_input}</textarea>
                <input type="submit" value="<fmt:message key="manage.player.account.status.submit"/>"/>
            </form>

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

            <hr/>

            <%--stats--%>

            <dt><fmt:message key="stats.maxbet"/></dt>
            <dd>${player.stats.maxBet}</dd>

            <dt><fmt:message key="stats.totalbet"/></dt>
            <dd>${player.stats.totalBet}</dd>

            <dt><fmt:message key="stats.maxwinbet"/></dt>
            <dd>${player.stats.maxWinRoll}</dd>

            <dt><fmt:message key="stats.maxwinstreak"/></dt>
            <dd>${player.stats.maxWinStreak}</dd>

            <dt><fmt:message key="stats.totalwin"/></dt>
            <dd>${player.stats.totalWin}</dd>

            <dt><fmt:message key="stats.maxpayment"/></dt>
            <dd>${player.stats.maxPayment}</dd>

            <dt><fmt:message key="stats.totalpayment"/></dt>
            <dd>${player.stats.totalPayment}</dd>

            <dt><fmt:message key="stats.maxwithdrawal"/></dt>
            <dd>${player.stats.maxWithdrawal}</dd>

            <dt><fmt:message key="stats.totalwithdrawal"/></dt>
            <dd>${player.stats.totalWithdrawal}</dd>
        </dl>
        <hr/>

        <h3><fmt:message key="manage.player.more.info"/></h3>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=show_player_support&id=${player.id}">
                <fmt:message key="manage.player.show.support"/>
            </a>
        </div>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=show_player_transactions&id=${player.id}">
                <fmt:message key="manage.player.show.transactions"/>
            </a>
        </div>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=show_player_loans&id=${player.id}">
                <fmt:message key="manage.player.show.loans"/>
            </a>
        </div>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=show_player_streaks&id=${player.id}">
                <fmt:message key="manage.player.show.streaks"/>
            </a>
        </div>
    </section>
</main>
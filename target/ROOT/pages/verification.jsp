<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="verification cols col-8">
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <dl class="player-verification custom-dl">

            <dt><fmt:message key="verification.profile"/></dt>
            <dd>
                <c:choose>
                    <c:when test="${player.verification.profileVerified}">
                        <fmt:message key="verification.ok"/>
                    </c:when>
                    <c:otherwise>
                        <span><fmt:message key="verification.no"/></span>
                        <div class="verify-button">
                            <a href="${pageContext.request.contextPath}/controller?command=verify_profile">&#10008</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </dd>

            <dt><fmt:message key="verification.email"/></dt>
            <dd>
                <c:choose>
                    <c:when test="${player.verification.emailVerified}">
                        <fmt:message key="verification.ok"/>
                    </c:when>
                    <c:otherwise>
                        <span><fmt:message key="verification.no"/></span>
                        <div class="verify-button">
                            <a href="${pageContext.request.contextPath}/controller?command=goto_email_verification">&#10008</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </dd>

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
                        <span><fmt:message key="verification.no"/></span>
                        <div class="verify-button">
                            <a href="${pageContext.request.contextPath}/controller?command=goto_upload_passport">&#10008</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </dd>

            <c:if test="${player.verification.commentary != null}">
                <dt><fmt:message key="verification.commentary"/></dt>
                <dd>${player.verification.commentary}</dd>
            </c:if>

            <dt><fmt:message key="verification.status"/></dt>
            <dd>${player.verification.status.toString().replaceAll("_", " ")}</dd>

            <c:if test="${player.verification.profileVerified
                && player.verification.emailVerified
                && player.verification.scanVerified}">
                <dt><fmt:message key="verification.date"/></dt>
                <dd>${j:formatDateTime(player.verification.verificationDate, "dd.MM.yyyy HH:mm")}</dd>
            </c:if>
        </dl>

        <c:if test="${!player.verification.scanVerified && player.verification.passport != null}">
            <div class="scan">
                <img src="/image/${player.verification.passport}"
                     alt="${player.profile.fName}'s passport-scan">
            </div>
        </c:if>
    </section>
</main>
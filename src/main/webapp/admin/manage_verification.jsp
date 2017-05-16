<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-support cols col-8">
        <h2><fmt:message key="manage.verification.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <section class="table-section">
            <table class="custom-table">
                <caption><fmt:message key="manage.verification.table.caption"/></caption>
                <thead>
                <tr>
                    <th><fmt:message key="manage.verification.table.header.playerid"/></th>
                    <th><fmt:message key="manage.verification.table.header.profole.verification"/></th>
                    <th><fmt:message key="manage.verification.table.header.email.verification"/></th>
                    <th><fmt:message key="manage.verification.table.header.scan.verification"/></th>
                    <th><fmt:message key="manage.verification.table.header.adminId"/></th>
                    <th><fmt:message key="manage.verification.table.header.date"/></th>
                    <th><fmt:message key="manage.verification.table.header.commentary"/></th>
                    <th><fmt:message key="manage.verification.table.header.manage"/></th>
                </tr>
                </thead>
                <tbody id="itemContainer">
                <c:forEach var="verification" items="${verification_list}">
                    <tr>
                        <td>${verification.playerId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${verification.profileVerified}">
                                    <fmt:message key="verification.ok"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="verification.no"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${verification.emailVerified}">
                                    <fmt:message key="verification.ok"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="verification.no"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${verification.scanVerified}">
                                    <fmt:message key="verification.ok"/>
                                </c:when>
                                <c:when test="${!verification.scanVerified && verification.passport != null}">
                                    <fmt:message key="verification.processing"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="verification.no"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${verification.adminId}</td>
                        <td>${j:formatDateTime(verification.verificationDate, "dd.MM.yyyy HH:mm")}</td>
                        <td>${verification.commentary}</td>
                        <td>
                            <div class="custom-link">
                                <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${verification.playerId}">
                                    <fmt:message key="manage.verification.table.link.verify"/>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="holder"></div>
        </section>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/manage_verification.js"></script>
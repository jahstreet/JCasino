<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <%@include file="jspf/slider.jsp" %>
    <section class="left-bar cols col-4">
        <c:choose>
            <c:when test="${sessionScope.user.role != null && 'PLAYER'.equals(sessionScope.user.role.toString())}">
                <%@include file="jspf/leftmenu.jspf" %>
                <%@include file="jspf/user.jspf" %>
            </c:when>
            <c:otherwise>
                <%@include file="jspf/login.jspf" %>
            </c:otherwise>
        </c:choose>
    </section>
    <section class="content cols col-8">
        <div class="holder"></div>
        <ul id="itemContainer">
            <c:forEach var="news" items="${newsList}">
                <c:if test="${news.locale.locale.equals(locale) ||
                ((locale == null || locale.equals('default')) && news.locale.locale.equals('ru_RU'))}">
                    <li>
                        <details>
                            <summary>
                                <h3><c:out value="${news.header}"/></h3>
                                <img src="${pageContext.request.contextPath}/image/news/news-image${news.id}.jpg"
                                     alt="news-image${news.id}">
                                <span><fmt:message key="news.summary"/>...</span>
                            </summary>
                            <p class="news-text"><c:out value="${news.text}"/></p>
                                <%--<a href="#"><fmt:message key="news.more"/>...</a>--%>
                        </details>
                        <p>
                            <time>${news.date}</time>
                        </p>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
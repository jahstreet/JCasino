<main class="container">
    <section class="slider cols col-12">
        <img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" alt="slider-image">
    </section>
    <section class="left-bar cols col-4">
        <c:choose>
            <c:when test="${sessionScope.user.role != null && 'PLAYER'.equals(sessionScope.user.role.toString())}">
                <!-- Left menu block -->
                <%@include file="jspf/leftmenu.jspf" %>
                <!-- User block -->
                <%@include file="jspf/user.jspf" %>
            </c:when>
            <c:otherwise>
                <!-- Authorisation form -->
                <%@include file="jspf/login.jspf" %>
            </c:otherwise>
        </c:choose>
    </section>
    <section class="content cols col-8">
        <%--News--%>
        <%@include file="jspf/news.jspf" %>
    </section>
</main>

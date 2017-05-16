<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="admin-tasks cols col-8">
        <%--stub--%>
        <jsp:forward page="${pageContext.request.contextPath}/admin/manage_news.jsp"/>
    </section>
</main>

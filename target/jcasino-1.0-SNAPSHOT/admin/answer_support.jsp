<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-news cols col-8">
        <h2><fmt:message key="manage.news.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <dl class="custom-dl">
            <dt><fmt:message key="support.answer.playerid"/></dt>
            <dd><c:if test="${question.playerId != 0}">${question.playerId}</c:if></dd>

            <dt><fmt:message key="support.answer.email"/></dt>
            <dd>${question.email}</dd>

            <dt><fmt:message key="support.answer.topic"/></dt>
            <dd><fmt:message key="support.topic.${question.topic.toString().toLowerCase()}"/></dd>

            <dt><fmt:message key="support.answer.question.date"/></dt>
            <dd>${j:formatDateTime(question.questionDate, "dd-MM-yyyy HH:mm")}</dd>

            <dt><fmt:message key="support.answer.question"/></dt>
            <dd>${question.question}</dd>
        </dl>
        <hr/>
        <form class="custom-form clearfix" name="answer_support" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="answer_support">
            <input type="hidden" name="id" value="${question.id}">
            <textarea name="answer" rows="7" maxlength="700" wrap="soft"
                      title="<fmt:message key="support.answer.textarea.title"/>"
                      placeholder="<fmt:message key="support.answer.textarea.holder"/>"
                      required>${answer_input}</textarea>
            <input type="submit" value="<fmt:message key="support.answer.submit"/>"/>
        </form>
    </section>
</main>
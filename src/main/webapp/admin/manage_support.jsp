<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-support cols col-8">
        <h2><fmt:message key="manage.support.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <%--show questions--%>
        <h3><fmt:message key="manage.support.show.questions.form.header"/></h3>
        <form class="custom-form clearfix" name="showQuestions" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_questions">
            <select name="topic" title="<fmt:message key="support.option.title"/>">
                <option selected disabled value=""><fmt:message key="support.option.header"/></option>
                <option name="transaction" value="transaction"><fmt:message key="support.topic.transaction"/></option>
                <option name="loan" value="loan"><fmt:message key="support.topic.loan"/></option>
                <option name="rules" value="rules"><fmt:message key="support.topic.rules"/></option>
                <option name="verification" value="verification">
                    <fmt:message key="support.topic.verification"/>
                </option>
                <option name="ban" value="ban"><fmt:message key="support.topic.ban"/></option>
                <option name="other" value="other"><fmt:message key="support.topic.other"/></option>
            </select>
            <input type="submit" value="<fmt:message key="manage.support.show.submit"/>"/>
        </form>
        <hr/>
        <%--show answers--%>
        <h3><fmt:message key="manage.support.show.answers.form.header"/></h3>
        <form class="custom-form clearfix" name="showAnswers" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="show_answers">
            <select name="topic" title="<fmt:message key="support.option.title"/>">
                <option selected disabled value=""><fmt:message key="support.option.header"/></option>
                <option name="transaction" value="transaction"><fmt:message key="support.topic.transaction"/></option>
                <option name="loan" value="loan"><fmt:message key="support.topic.loan"/></option>
                <option name="rules" value="rules"><fmt:message key="support.topic.rules"/></option>
                <option name="verification" value="verification">
                    <fmt:message key="support.topic.verification"/>
                </option>
                <option name="ban" value="ban"><fmt:message key="support.topic.ban"/></option>
                <option name="other" value="other"><fmt:message key="support.topic.other"/></option>
            </select>
            <div class="input-block">
                <label for="operation-month"><fmt:message key="manage.support.input.month.label"/></label>
                <input id="operation-month" type="month" name="month" value="${month_input}"
                       title="<fmt:message key="manage.support.input.month.title"/>"
                       min="2017-01">
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="showMy" type="checkbox" name="show_my" value="show_my"
                       title="<fmt:message key="manage.support.checkbox.showmy.title"/>">
                <label for="showMy"><fmt:message key="manage.support.checkbox.showmy.label"/></label>
            </div>
            <div class="input-inline-block">
                <input class="custom-checkbox" id="sort-satisfaction" type="checkbox" name="satisfaction"
                       value="satisfaction"
                       title="<fmt:message key="manage.support.checkbox.satisfactionsort.title"/>">
                <label for="sort-satisfaction"><fmt:message
                        key="manage.support.checkbox.satisfactionsort.label"/></label>
            </div>
            <input type="submit" value="<fmt:message key="manage.support.show.submit"/>"/>
        </form>
        <%--questions/answers table--%>
        <c:if test="${questionList != null && !questionList.isEmpty()}">
            <hr/>
            <section class="table-section">
                <table class="custom-table">
                    <caption><fmt:message key="support.table.caption"/></caption>
                    <thead>
                    <tr>
                        <th><fmt:message key="support.table.header.playerid"/></th>
                        <th><fmt:message key="support.table.header.date"/></th>
                        <th><fmt:message key="support.table.header.topic"/></th>
                        <th><fmt:message key="support.table.header.question"/></th>
                        <th><fmt:message key="support.table.header.date"/></th>
                        <th class="answer-header"><fmt:message key="support.table.header.answer"/></th>
                        <th class="satisfaction-header"><fmt:message key="support.table.header.satisfaction"/></th>
                    </tr>
                    </thead>
                    <tbody id="itemContainer">
                    <c:forEach var="question" items="${questionList}">
                        <tr>
                            <td>
                                <c:if test="${question.playerId != 0}">
                                    <div class="custom-link">
                                        <a href="${pageContext.request.contextPath}/controller?command=goto_manage_player&id=${question.playerId}">
                                                ${question.playerId}
                                        </a>
                                    </div>
                                </c:if>
                            </td>
                            <td>${j:formatDateTime(question.questionDate, "dd-MM-yyyy HH:mm")}</td>
                            <td><fmt:message key="support.topic.${question.topic.toString().toLowerCase()}"/></td>
                            <td><c:out value="${question.question}"/></td>
                            <td>${j:formatDateTime(question.answerDate, "dd-MM-yyyy HH:mm")}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${question.answer == null}">
                                        <div class="custom-link">
                                            <a href="${pageContext.request.contextPath}/controller?command=goto_answer_support&id=${question.id}">
                                                <fmt:message key="manage.support.answer.link"/>
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise><c:out value="${question.answer}"/></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${question.satisfaction != null}">
                                    <fmt:message
                                            key="support.satisfaction.${question.satisfaction.toString().toLowerCase()}"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="holder"></div>
            </section>
        </c:if>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/manage_support.js"></script>
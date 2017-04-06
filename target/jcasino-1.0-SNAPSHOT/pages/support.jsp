<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="slider cols col-12">
        <img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" alt="slider-image">
    </section>
    <section class="cols col-2"></section>
    <section class="support cols col-8">
        <h2><fmt:message key="support.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="custom-form clearfix" name="supportMessage" method="GET"
              action="${pageContext.request.contextPath}/controller"
              onsubmit="">
            <input type="hidden" name="command" value="send_support"/>
            <input type="email" name="email" value="${email_input}"
                   pattern="^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$"
                   title="<fmt:message key="support.email.title"/>"
                   placeholder="<fmt:message key="support.email.holder"/>"
                   required>
            <select name="topic" title="<fmt:message key="support.option.title"/>" required>
                <option selected disabled value=""><fmt:message key="support.option.header"/></option>
                <option name="transaction" value="transaction"><fmt:message key="support.topic.transaction"/></option>
                <option name="loan" value="loan"><fmt:message key="support.topic.loan"/></option>
                <option name="rules" value="rules"><fmt:message key="support.topic.rules"/></option>
                <option name="verification" value="verification"><fmt:message
                        key="support.topic.verification"/></option>
                <option name="ban" value="ban"><fmt:message key="support.topic.ban"/></option>
                <option name="other" value="other"><fmt:message key="support.topic.other"/></option>
            </select>
            <textarea name="question" rows="5" maxlength="700" wrap="soft"
                      title="<fmt:message key="support.textarea.title"/>"
                      placeholder="<fmt:message key="support.textarea.holder"/>"
                      required>${question_input}</textarea>
            <input type="submit" value="<fmt:message key="support.submit"/>">
        </form>
        <c:if test="${questionList != null && !questionList.isEmpty()}">
            <hr/>
            <section class="table-section">
                <table class="custom-table">
                    <caption><fmt:message key="support.table.caption"/></caption>
                    <tr>
                        <th><fmt:message key="support.table.header.date"/></th>
                        <th><fmt:message key="support.table.header.topic"/></th>
                        <th><fmt:message key="support.table.header.question"/></th>
                        <th><fmt:message key="support.table.header.date"/></th>
                        <th><fmt:message key="support.table.header.answer"/></th>
                        <th class="satisfaction-header"><fmt:message key="support.table.header.satisfaction"/></th>
                    </tr>
                    <c:forEach var="question" items="${questionList}">
                        <tr>
                            <td>${j:formatDateTime(question.questionDate, "dd-MM-yyyy HH:mm")}</td>
                            <td><fmt:message key="support.topic.${question.topic.toString().toLowerCase()}"/></td>
                            <td>${question.question}</td>
                            <td>${j:formatDateTime(question.answerDate, "dd-MM-yyyy HH:mm")}</td>
                            <td>${question.answer}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${question.satisfaction != null}">
                                        <fmt:message
                                                key="support.satisfaction.${question.satisfaction.toString().toLowerCase()}"/>
                                        <a class="reset-ref"
                                           href="${pageContext.request.contextPath}/controller?command=reset_satisfaction&id=${question.id}">
                                            <fmt:message key="support.satisfaction.reset"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <form class="inline-option-form clearfix" name="setSatisfaction"
                                              method="GET"
                                              action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="command" value="set_satisfaction"/>
                                            <input type="hidden" name="id" value="${question.id}"/>
                                            <select id="rate-select" class="rate-select" name="satisfaction"
                                                    title="<fmt:message key="support.satisfaction.title"/>"
                                                    required>
                                                <option name="satisfaction" value="best">
                                                    <fmt:message key="support.satisfaction.best"/>
                                                </option>
                                                <option name="satisfaction" value="good">
                                                    <fmt:message key="support.satisfaction.good"/>
                                                </option>
                                                <option name="satisfaction" value="norm">
                                                    <fmt:message key="support.satisfaction.norm"/>
                                                </option>
                                                <option name="satisfaction" value="bad">
                                                    <fmt:message key="support.satisfaction.bad"/>
                                                </option>
                                                <option name="satisfaction" value="worst">
                                                    <fmt:message key="support.satisfaction.worst"/>
                                                </option>
                                            </select>
                                            <input type="submit" name="submit"
                                                   value="<fmt:message key="support.satisfaction.submit"/>">
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </section>
        </c:if>
    </section>
    <section class="cols col-2"></section>
</main>
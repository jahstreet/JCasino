<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/left_menu.jspf" %>
        <%@include file="jspf/user_block.jspf" %>
    </section>
    <section class="manage-news cols col-8">
        <h2><fmt:message key="manage.news.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <%--add--%>
        <form class="add-news custom-form clearfix" name="addNews" method="POST" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/controller" accept-charset="UTF-8">
            <input type="hidden" name="command" value="add_news">
            <select name="locale" title="<fmt:message key="manage.news.add.locale.title"/>" required>
                <option selected disabled value=""><fmt:message key="manage.news.add.locale.title"/></option>
                <option name="ru" value="ru"><fmt:message key="manage.news.add.locale.ru"/></option>
                <option name="en" value="en"><fmt:message key="manage.news.add.locale.en"/></option>
            </select>
            <input type="text" name="header" maxlength="45"
                   title="<fmt:message key="manage.news.add.header.title"/>"
                   placeholder="<fmt:message key="manage.news.add.header.holder"/>"
                   value="${header_input}"
                   required>
            <input type="file" name="news_image" title="<fmt:message key="manage.news.add.image.title"/>" required>
            <textarea name="text" rows="8" maxlength="700" wrap="soft"
                      title="<fmt:message key="manage.news.add.textarea.title"/>"
                      placeholder="<fmt:message key="manage.news.add.textarea.holder"/>"
                      required>${text_input}</textarea>
            <input type="submit" value="<fmt:message key="manage.news.add.submit"/>"/>
        </form>


        <%--edit--%>
        <c:if test="${newsList!=null && !newsList.isEmpty()}">
            <hr/>
            <div class="holder"></div>
            <hr/>
            <ul id="itemContainer">
                <c:forEach var="news" items="${newsList}">
                    <li class="edit-news-list">
                            <%--header--%>
                        <div class="edit-block">
                            <h3><c:out value="${news.header}"/></h3>
                            <button id="change-header" type="button" class="change" onclick="changeNewsItem(event)">
                                ...
                            </button>
                            <form name="change-header" action="${pageContext.request.contextPath}/controller"
                                  class="hidden edit-news" method="GET">
                                <input type="hidden" name="command" value="edit_news">
                                <input type="hidden" name="id" value="${news.id}">
                                <input type="text" name="header" value="${news.header}"
                                       placeholder="<fmt:message key="manage.news.change.header.holder"/>"
                                       title="<fmt:message key="manage.news.change.header.title"/>"
                                       maxlength="45" required>
                                <input type="submit" value="<fmt:message key="manage.news.change.submit"/>">
                            </form>
                        </div>
                            <%--image--%>
                        <div class="edit-block">
                            <img src="${pageContext.request.contextPath}/image/news/news-image${news.id}.jpg"
                                 alt="news-image${news.id}">
                            <button id="change-image" type="button" class="change" onclick="changeNewsItem(event)">
                                ...
                            </button>
                            <form name="change-image" action="${pageContext.request.contextPath}/controller"
                                  class="hidden edit-news" method="POST" enctype="multipart/form-data">
                                <input type="hidden" name="command" value="edit_news">
                                <input type="hidden" name="id" value="${news.id}">
                                <input type="file" name="news_image"
                                       title="<fmt:message key="manage.news.change.image.title"/>" required>
                                <input type="submit" value="<fmt:message key="manage.news.change.submit"/>">
                            </form>
                        </div>
                            <%--text--%>
                        <div class="edit-block">
                            <p class="news-text"><c:out value="${news.text}"/></p>
                            <button id="change-text" type="button" class="change" onclick="changeNewsItem(event)">
                                ...
                            </button>
                            <form name="change-text" action="${pageContext.request.contextPath}/controller"
                                  class="hidden edit-news" method="GET">
                                <input type="hidden" name="command" value="edit_news">
                                <input type="hidden" name="id" value="${news.id}">
                                <textarea name="text" rows="8" maxlength="700" wrap="soft"
                                          title="<fmt:message key="manage.news.change.textarea.title"/>"
                                          placeholder="<fmt:message key="manage.news.change.textarea.holder"/>"
                                          required>${news.text}</textarea>
                                <input type="submit" value="<fmt:message key="manage.news.change.submit"/>">
                            </form>
                        </div>
                        <div class="edit-block">
                            <time>${news.date}</time>
                            <div class="locale-data">
                                <fmt:message key="manage.news.language"/>: <fmt:message
                                    key="manage.news.add.locale.${news.locale.toString().toLowerCase()}"/>
                            </div>
                        </div>
                        <div class="custom-link">
                            <a href="${pageContext.request.contextPath}/controller?command=delete_news&id=${news.id}">
                                <fmt:message key="manage.change.news.delete"/></a>
                        </div>
                        <hr/>
                    </li>
                </c:forEach>
            </ul>
            <div class="holder"></div>
        </c:if>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/manage_news.js"></script>
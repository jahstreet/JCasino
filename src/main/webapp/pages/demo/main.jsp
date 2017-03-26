<!DOCTYPE html>
<!-- Next line container -->
<div class="content">

    <aside class="left">
        <%--<%@include file="pages/jspf/leftmenu.jspf" %>--%>
        <%@include file="/pages/jspf/login.jspf" %>
    </aside>
    <section class="news">
        <ul>
            <jsp:useBean id="allNews" class="by.sasnouskikh.jcasino.entity.bean.NewsList" scope="session"/>
            <%session.setAttribute("newsList", allNews.getNewsList());%>
            <c:forEach var="news" items="${allNews.newsList}">
                <li>
                    <a href="pages/news_full.jsp?id=${allNews.newsList.indexOf(news)}">
                        <p class="news-title">${news.header}</p>
                        <img src="ShowImage?id=${allNews.newsList.indexOf(news)}"
                             alt="news-image${news.id}">
                        <p class="news-text">${news.text.substring(0,50)}...</p>
                        <p class="news-date">${news.date}</p>
                    </a>
                </li>

            </c:forEach>
        </ul>
    </section>
</div>

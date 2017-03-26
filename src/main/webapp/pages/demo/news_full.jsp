<%@ page import="by.sasnouskikh.jcasino.entity.bean.News" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<!-- Next line container -->
<div class="content">

    <aside class="left">
        <%--<%@include file="pages/jspf/leftmenu.jspf" %>--%>
        <%@include file="/pages/jspf/login.jspf" %>
    </aside>
    <section class="news_full">
        <div>
            <%! int id;
                News news;%>
            <% id = Integer.parseInt(request.getParameter("id"));
                List<News> list = (List<News>) session.getAttribute("newsList");
                news = list.get(id);%>
            <p class="news-title"><%=news.getHeader()%>
            </p>
            <img src="/ShowImage?id=<%=id%>"
                 alt="news-image<%=id%>">
            <p class="news-text"><%=news.getText()%>
            </p>
            <p class="news-date"><%=news.getDate()%>
            </p>
        </div>
    </section>
</div>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jcasino.by/functions" prefix="j" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="prop.pagecontent"/>
<!DOCTYPE html>
<html lang="<fmt:message key="header.html.lang"/>">
<head>
    <title>JCasino</title>
    <meta charset="UTF-8">
    <meta name="description" content="<fmt:message key="header.head.description"/> JCasino">
    <meta name="keywords" content="<fmt:message key="header.head.keywords"/>">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.5, user-scalable=yes">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/game/fruits/css/fruits.css">
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.0.js"></script>
</head>
<body id="body">
<h1 class="title">JCasino Fruits</h1>
<canvas id="canvas" width="600" height="450"></canvas>
<div id="money">${money_input}</div>
<div id="music">&#9835;</div>
<section class="button-container clearfix">
    <div class="buttons">
        <input class="button" type="button" form="spin" onclick="spin()"
               value="<fmt:message key="fruits.button.spin"/>">
        <div class="showlines button" onclick="lines()"><fmt:message key="fruits.button.show.lines"/></div>
    </div>
    <form id="spin">
        <%--AjaxCommand--%>
        <input type="hidden" name="command" value="spin"/>
        <div class="quantity-block">
            <%--Bet--%>
            <div class="quantity clearfix">
                <label for="bet"><fmt:message key="fruits.bet.label"/></label>
                <input readonly id="bet" class="bet" type="number" name="bet" value="0.25" min="0.25" step="0.25"
                       max="<c:choose><c:when test="${bet_input != null}">${bet_input}</c:when><c:when test="${player.account.status.betLimit != null}">${player.account.status.betLimit}</c:when><c:otherwise>5</c:otherwise></c:choose>">
            </div>
            <%--Offsets--%>
            <c:forEach var="i" begin="1" end="3" step="1">
                <div class="quantity clearfix">
                    <label for="offset${i}"><fmt:message key="fruits.offset.label"/> ${i}</label>
                    <input readonly id="offset${i}" type="number" name="offset${i}" value="0" min="0" max="59" step="1">
                </div>
            </c:forEach>
        </div>
        <%--Lines--%>
        <div class="line-block">
            <c:forEach var="i" begin="1" end="5" step="1">
                <input id="line${i}" name="line${i}" type="checkbox" class="checkbox">
                <label for="line${i}"><fmt:message key="fruits.line.label"/> ${i}</label>
            </c:forEach>
        </div>
    </form>
</section>
<script src="${pageContext.request.contextPath}/resources/game/fruits/js/slot.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/game/fruits/js/quantity.js" type="text/javascript"></script>
</body>
</html>
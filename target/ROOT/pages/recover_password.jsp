<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="slider cols col-12">
        <img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" alt="slider-image">
    </section>
    <section class="cols col-2"></section>
    <section class="recover-password cols col-8">
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <h2><fmt:message key="recoverpassword.header"/></h2>
        <form class="custom-form" name="recoverPasswordForm" method="GET"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="recover_password"/>
            <input type="email" name="email" value="${user.email}" placeholder="<fmt:message key="recoverpassword.email.holder"/>"
                   title="<fmt:message key="recoverpassword.email.title"/>"
                   pattern="^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$"
                   maxlength="320" required autofocus>
            <input type="submit" value="<fmt:message key="recoverpassword.submit"/>">
        </form>
    </section>
    <section class="cols col-2"></section>
</main>
<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="cols col-2"></section>
    <section class="register cols col-8">
        <h2><fmt:message key="register.header"/></h2>
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <form class="register-form clearfix" name="registerForm" method="POST"
              action="${pageContext.request.contextPath}/controller"
              onsubmit="return validateRegister()">
            <input type="hidden" name="command" value="register"/>

            <div class="input-block">
                <label class="required" for="email-input"><fmt:message key="register.email"/></label>
                <input id="email-input" type="email" name="email" value="${email_input}"
                       pattern="^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$"
                       title="<fmt:message key="register.email.title"/>"
                       maxlength="320" required autofocus/>
            </div>
            <div class="input-block">
                <label class="required" for="password-input"><fmt:message key="register.password"/></label>
                <input id="password-input" type="password" name="password" value=""
                       pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                       title="<fmt:message key="register.password.title"/>"
                       required/>
            </div>
            <div class="input-block">
                <label class="required" for="password-input-again"><fmt:message key="register.passwordagain"/></label>
                <input id="password-input-again" type="password" name="password_again" value=""
                       pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                       title="<fmt:message key="register.password.title"/>"
                       required/>
            </div>
            <div class="input-block">
                <label class="required" for="birthdate-input"><fmt:message key="register.birthdate"/></label>
                <input id="birthdate-input" type="date" name="birthdate" value="${birthdate_input}"
                       min="1900-01-01" title="<fmt:message key="register.birthdate.title"/>" required/>
            </div>
            <div class="input-block">
                <label for="fname=input"><fmt:message key="register.fname"/></label>
                <input id="fname=input" type="text" name="fname" value="${fname_input}" pattern="[A-Za-z ]{1,70}"
                       title="<fmt:message key="register.fname.title"/>"/>
            </div>
            <div class="input-block">
                <label for="mname-input"><fmt:message key="register.mname"/></label>
                <input id="mname-input" type="text" name="mname" value="${mname_input}" pattern="[A-Za-z ]{1,70}"
                       title="<fmt:message key="register.mname.title"/>"/>
            </div>
            <div class="input-block">
                <label for="lname-input"><fmt:message key="register.lname"/></label>
                <input id="lname-input" type="text" name="lname" value="${lname_input}" pattern="[A-Za-z ]{1,70}"
                       title="<fmt:message key="register.lname.title"/>"/>
            </div>
            <div class="input-block">
                <label class="required" for="passport-input"><fmt:message key="register.passport"/></label>
                <input id="passport-input" type="text" name="passport" value="${passport_input}" pattern="\w{1,30}"
                       title="<fmt:message key="register.passport.title"/>"
                       required/>
            </div>
            <div class="input-block">
                <label for="question-input"><fmt:message key="register.question"/></label>
                <input id="question-input" type="text" name="question" value="${question_input}" maxlength="64"
                       title="<fmt:message key="register.question.title"/>"/>
            </div>
            <div class="input-block">
                <label for="answer-input"><fmt:message key="register.answer"/></label>
                <input id="answer-input" type="text" name="answer" value="${answer_input}" maxlength="32"
                       title="<fmt:message key="register.answer.title"/>"/>
            </div>
            <div class="input-block">
                <input type="submit" value="<fmt:message key="register.submit"/>"/>
            </div>
        </form>
        <div class="custom-button">
            <a href="${pageContext.request.contextPath}/controller?command=goto_main">
                <fmt:message key="register.back"/>
            </a>
        </div>
        <script src="${pageContext.request.contextPath}/resources/js/register.js"></script>
    </section>
    <section class="cols col-2"></section>
</main>
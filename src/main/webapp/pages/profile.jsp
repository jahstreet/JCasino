<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="player-profile cols col-8">
        <noscript><p class="error-message">${errorMessage}</p></noscript>
        <dl class="custom-dl">
            <dt><fmt:message key="profile.id"/></dt>
            <dd><span>${player.id}</span></dd>
            <dt><fmt:message key="profile.email"/></dt>
            <dd><span><c:out value="${player.email}"/></span>
                <button id="change-email" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-email" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="email" name="email" value="" placeholder="<fmt:message key="profile.email.change.holder"/>"
                           pattern="^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$"
                           title="<fmt:message key="register.email.title"/>"
                           maxlength="320" required>
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.registerdate"/></dt>
            <dd><span>${player.registrationDate}</span></dd>

            <dt><fmt:message key="profile.fName"/></dt>
            <dd><span><c:out value="${player.profile.fName}"/></span>
                <button id="change-fname" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-fname" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="text" name="fname" value="" placeholder="<fmt:message key="profile.fname.change.holder"/>"
                           pattern="[A-Za-z ]{1,70}" title="<fmt:message key="register.fname.title"/>">
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.mName"/></dt>
            <dd><span><c:out value="${player.profile.mName}"/></span>
                <button id="change-mname" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-mname" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="text" name="mname" value="" placeholder="<fmt:message key="profile.mname.change.holder"/>"
                           pattern="[A-Za-z ]{1,70}" title="<fmt:message key="register.mname.title"/>">
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.lName"/></dt>
            <dd><span><c:out value="${player.profile.lName}"/></span>
                <button id="change-lname" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-fname" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="text" name="lname" value="" placeholder="<fmt:message key="profile.lname.change.holder"/>"
                           pattern="[A-Za-z ]{1,70}" title="<fmt:message key="register.lname.title"/>">
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.birthdate"/></dt>
            <dd><span><c:out value="${player.profile.birthDate}"/></span>
                <button id="change-bdate" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form onsubmit="return validateBirthdate()" name="change-bdate"
                      action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="date" name="birthdate" value="" min="1900-01-01"
                           title="<fmt:message key="register.birthdate.title"/>" required>
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.passport"/></dt>
            <dd><span><c:out value="${player.profile.passport}"/></span>
                <button id="change-passport" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-passport" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="text" name="passport" value=""
                           placeholder="<fmt:message key="profile.passport.change.holder"/>"
                           pattern="\w{1,30}" title="<fmt:message key="register.passport.title"/>" required>
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>

            <dt><fmt:message key="profile.question"/></dt>
            <dd><span><c:out value="${player.profile.question}"/></span>
                <button id="change-question" type="button" class="change" onclick="changeProfileItem(event)">...</button>
                <form name="change-question" action="${pageContext.request.contextPath}/controller" class="hidden" method="GET">
                    <input type="hidden" name="command" value="edit_profile">
                    <input type="text" name="question" value=""
                           placeholder="<fmt:message key="profile.question.change.holder"/>"
                           maxlength="64" title="<fmt:message key="register.question.title"/>">
                    <input type="text" name="secret_answer" value="" placeholder="<fmt:message key="profile.answer.change.holder"/>"
                           maxlength="32" title="<fmt:message key="register.answer.title"/>">
                    <input type="submit" value="<fmt:message key="profile.change.submit"/>">
                </form>
            </dd>
        </dl>

        <span></span>
        <button id="change-password" type="button" class="change" onclick="changeProfileItem(event)">
            <fmt:message key="profile.change.password"/></button>
        <form name="change-password" action="${pageContext.request.contextPath}/controller" class="hidden" method="POST"
              onsubmit="return validatePass()">
            <input type="hidden" name="command" value="edit_profile">
            <input type="password" name="old_password" value=""
                   placeholder="<fmt:message key="profile.password-old.change.holder"/>"
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="register.password.title"/>" required/>
            <input type="password" name="password" value=""
                   placeholder="<fmt:message key="profile.password.change.holder"/>"
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="register.password.title"/>" required/>
            <input type="password" name="password_again" value=""
                   placeholder="<fmt:message key="profile.password-again.change.holder"/>"
                   pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\w_-]{8,}$"
                   title="<fmt:message key="register.password.title"/>" required/>
            <input type="submit" value="<fmt:message key="profile.change.submit"/>">
        </form>

        <div class="custom-link">
            <a href="${pageContext.request.contextPath}/controller?command=goto_recover_password"><fmt:message key="profile.forgotpass"/></a>
        </div>
    </section>
</main>
<script src="${pageContext.request.contextPath}/resources/js/edit_profile.js"></script>
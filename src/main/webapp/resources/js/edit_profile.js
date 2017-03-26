var PASSWORD_MISMATCH,
    NOT_ENOUGH_AGE;

if (locale == "en_US") {
    PASSWORD_MISMATCH = "Passwords doesn't match";
    NOT_ENOUGH_AGE = "You are less then 18 years old";
} else {
    PASSWORD_MISMATCH = "Пароли не совпадают";
    NOT_ENOUGH_AGE = "Вам нет 18 лет";
}

var formBdate = document.getElementsByName("change-bdate")[0],
    bdate = formBdate.elements.birthdate,
    formPass = document.getElementsByName("change-password")[0],
    pwdNew1 = formPass.elements.password,
    pwdNew2 = formPass.elements.password_again;

bdate.addEventListener("change", checkBdate, false);
pwdNew1.addEventListener("change", checkPassword, false);
pwdNew2.addEventListener("change", checkPassword, false);

function checkBdate() {
    var bdateValue = new Date(bdate.value),
        year = bdateValue.getFullYear(),
        month = bdateValue.getMonth(),
        day = bdateValue.getDate(),
        curDate = new Date(),
        curYear = curDate.getFullYear(),
        curMonth = curDate.getMonth(),
        curDay = curDate.getDate(),
        delYear = curYear - year,
        delMonth = curMonth - month,
        delDay = curDay - day;
    if (!(delYear > 18
        || delYear == 18 && delMonth > 0
        || delYear == 18 && delMonth == 0 && delDay >= 0)) {
        bdate.setCustomValidity(NOT_ENOUGH_AGE);
    } else {
        bdate.setCustomValidity('');
    }
}

function checkPassword() {
    if (pwdNew1.value != pwdNew2.value) {
        pwdNew1.setCustomValidity(PASSWORD_MISMATCH);
    } else {
        pwdNew1.setCustomValidity('');
    }
}

function validateBdate() {
    checkBdate();
    if (!formBdate.checkValidity()) {
        bdate.focus();
        return false;
    }
}

function validatePass() {
    checkPassword();
    if (!formPass.checkValidity()) {
        pwdNew1.focus();
        return false;
    }
}

function changeProfileItem(event) {
    var button = event.currentTarget,
        form = button.nextElementSibling,
        span = button.previousElementSibling;
    button.style.display = "none";
    span.style.display = "none";
    if (form.name != "change-password") {
        form.style.display = "inline-block";
    } else {
        form.style.display = "block";
    }
    form.elements[1].focus();
}
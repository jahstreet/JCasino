<main class="container">
    <section class="left-bar cols col-4">
        <%@include file="jspf/leftmenu.jspf" %>
        <%@include file="jspf/user.jspf" %>
    </section>
    <section class="stats cols col-8">
        <dl class="player-stats custom-dl">
            <dt><fmt:message key="stats.maxbet"/></dt>
            <dd>${player.stats.maxBet}</dd>

            <dt><fmt:message key="stats.totalbet"/></dt>
            <dd>${player.stats.totalBet}</dd>

            <dt><fmt:message key="stats.maxwinbet"/></dt>
            <dd>${player.stats.maxWinRoll}</dd>

            <dt><fmt:message key="stats.maxwinstreak"/></dt>
            <dd>${player.stats.maxWinStreak}</dd>

            <dt><fmt:message key="stats.totalwin"/></dt>
            <dd>${player.stats.totalWin}</dd>

            <dt><fmt:message key="stats.maxpayment"/></dt>
            <dd>${player.stats.maxPayment}</dd>

            <dt><fmt:message key="stats.totalpayment"/></dt>
            <dd>${player.stats.totalPayment}</dd>

            <dt><fmt:message key="stats.maxwithdrawal"/></dt>
            <dd>${player.stats.maxWithdrawal}</dd>

            <dt><fmt:message key="stats.totalwithdrawal"/></dt>
            <dd>${player.stats.totalWithdrawal}</dd>
        </dl>
    </section>
</main>
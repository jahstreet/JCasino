<%@ page contentType="text/html;charset=UTF-8" %>
<main class="container">
    <section class="left-bar cols col-4">
        <c:choose>
            <c:when test="${sessionScope.user.role != null && 'PLAYER'.equals(sessionScope.user.role.toString())}">
                <%@include file="jspf/leftmenu.jspf" %>
                <%@include file="jspf/user.jspf" %>
            </c:when>
            <c:otherwise>
                <%@include file="jspf/login.jspf" %>
            </c:otherwise>
        </c:choose>
    </section>
    <section class="rules cols col-8">
        <c:choose>
            <c:when test="${locale == 'en_US'}">
                <h2>Слот "JCasino Fruits"</h2>
                <p>JCasino Fruits is 3-reel slot-machine with five paylines and MD5 FairPlay Fairness Check.</p>
                <p>The spin can be played on one or several lines simultaneously. You can select number of lines to play
                    by clicking on the corresponding buttons on the "Control Panel" on the game page.</p>
                <p>For each playing line the same bet is made. The total bet is calculated as "Bet on Line" * "Number
                    of playing lines". The player can choose the amount of the bet on the line by clicking on the "+"
                    and "-" buttons in the "Bet" section of the "Control Panel" on the game page. The total bet on the
                    game (spin) is shown on the indicator "Total". The minimum bet is 0.25, the maximum bet depends on
                    the status of the player.</p>
                <p>Start the reels by clicking on the "Spin" button.<br/>
                    After the reels stop on the playing lines will be determined the winning combinations.</p>
            </c:when>
            <c:otherwise>
                <h2>Slot "JCasino Fruits"</h2>
                <p>JCasino Fruits (JCasino Фрукты) - это трехбарабанная слот-машина c пятью линиями выигрыша и Контролем
                    честности MD5 FairPlay.</p>
                <p>Игра может вестись на одной или нескольких линиях одновременно. Выбрать количество линий для игры
                    можно, нажав на соответствующие кнопки на "Панели управления" на странице игры.</p>
                <p>На каждую играющую линию делается одинаковая ставка. Суммарная ставка вычисляется как "Ставка на
                    линию" * "Кол-во играющих линий". Выбрать размер ставки на линию игрок может, нажав на кнопки "+" и
                    "-" в секции "Ставка"на "Панели управления" на странице игры. Общая сумма ставки на игру (спин)
                    демонстрируется на индикаторе "Суммарная ставка". Минимальная ставка 0,25, максимальная ставка
                    определяется в зависимости от статуса игрока.</p>
                <p>Запуск барабанов осуществляется нажатием на кнопку "Спин".<br/>
                    После того, как барабаны остановятся, на играющих линиях будут определены выигрышные комбинации.</p>
            </c:otherwise>
        </c:choose>
        <div class="rules-table-container">
            <table class="win-table rules-table">
                <caption><fmt:message key="rules.table.win.caption"/></caption>
                <tr>
                    <th><fmt:message key="rules.table.win.header.combination"/></th>
                    <th><fmt:message key="rules.table.win.header.multiplier"/></th>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.cherry"/> X3</td>
                    <td>X6</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.grapes"/> X3</td>
                    <td>X12</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.lemon"/> X3</td>
                    <td>X32</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.apple"/> X3</td>
                    <td>X45</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.orange"/> X3</td>
                    <td>X60</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.banana"/> X3</td>
                    <td>X90</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.watermelon"/> X3</td>
                    <td>X180</td>
                </tr>
            </table>
        </div>
        <c:choose>
            <c:when test="${locale == 'en_US'}">
                <p>The amount of winnings is determined by the formula: "Bet on line 1" * "Coefficient of winning on
                    line 1" + ... + "Bet on Line 5" * "Winning Coefficient on Line 5".</p>
                <p>In addition to playing for money, there is a possibility of demo-games for demo-money. To enable
                    Demo-mode you need to click on the corresponding button on the "Control panel". Demo-mode is
                    intended for acquaintance with the game rules and elements of "Control panel" of the slot machine.
                    Return to Real monet game-mode is made by pressing the same button again.</p>

                <h3>MD5 FairPlay Fairness Check in slot JCasino Fruits</h3>
                <p>When playing in the JCasino Fruits slot, we give players the opportunity to control randomness of the
                    results. A work model of a mechanical slot machine with completely random stopping of the reels is
                    realized in game. Each of the three reels of the JCasino Fruits slot has a strictly defined set of
                    elements arranged in a strict order.</p>
            </c:when>
            <c:otherwise>
                <p>Сумма выигрыша определяется по формуле: "Ставка на линию 1" * "Коэфициент выигрыша по линии 1" + ...
                    + "Ставка на линию 5" * "Коэфициент выигрыша по линии 5".</p>
                <p>Помимо игры на деньги предусмотрена возможность демонстрационной игры на демо-деньги. Для включения
                    режима демонстрационной игры необходимо нажать на кнопку "Демо". Демонстрационный режим игры
                    предназначен для знакомства со сценарием игры и элементами управления игровым автоматом. Возврат к
                    игре на реальные деньги производится повторным нажатием на эту же кнопку.</p>

                <h3>Контроль честности MD5 FairPlay в слоте JCasino Fruits</h3>
                <p>При игре в слот JCasino Fruits мы предоставляем игрокам возможность осуществления контроля
                    случайности получаемых результатов. В игре реализована модель работы механической слот-машины с
                    полностью случайной остановкой барабанов. Каждый из трех барабанов слота JCasino Fruits имеет строго
                    определенный набор символов, размещенный в строгом порядке.</p>
            </c:otherwise>
        </c:choose>
        <div class="rules-table-container">
            <table class="reel-table rules-table">
                <caption><fmt:message key="rules.table.reel.caption"/></caption>
                <tr>
                    <th><fmt:message key="rules.table.reel.header.position"/></th>
                    <th><fmt:message key="rules.table.reel.header.value"/></th>
                    <th><fmt:message key="rules.table.reel.header.position"/></th>
                    <th><fmt:message key="rules.table.reel.header.value"/></th>
                    <th><fmt:message key="rules.table.reel.header.position"/></th>
                    <th><fmt:message key="rules.table.reel.header.value"/></th>
                </tr>
                <jsp:useBean id="reelValues" class="by.sasnouskikh.jcasino.game.ReelValues"/>
                <c:forEach var="index" begin="1" end="20" step="1">
                    <tr>
                        <td>${index}</td>
                        <td>
                            <fmt:message key="reel.value.${reelValues.reelValues[index-1].toString().toLowerCase()}"/>
                        </td>
                        <td>${20+index}</td>
                        <td>
                            <fmt:message
                                    key="reel.value.${reelValues.reelValues[20+index-1].toString().toLowerCase()}"/>
                        </td>
                        <td>${40+index}</td>
                        <td>
                            <fmt:message
                                    key="reel.value.${reelValues.reelValues[40+index-1].toString().toLowerCase()}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <c:choose>
            <c:when test="${locale == 'en_US'}">
                <p>60 symbols total.</p>
            </c:when>
            <c:otherwise>
                <p>Всего 60 символов.</p>
            </c:otherwise>
        </c:choose>
        <div class="rules-table-container">
            <table class="reel-total-table rules-table">
                <caption><fmt:message key="rules.table.reel.total.caption"/></caption>
                <tr>
                    <th><fmt:message key="rules.table.reel.total.header.value"/></th>
                    <th><fmt:message key="rules.table.reel.total.header.number"/></th>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.cherry"/></td>
                    <td>30</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.grapes"/></td>
                    <td>15</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.lemon"/></td>
                    <td>5</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.apple"/></td>
                    <td>4</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.orange"/></td>
                    <td>3</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.banana"/></td>
                    <td>2</td>
                </tr>
                <tr>
                    <td><fmt:message key="reel.value.watermelon"/> X3</td>
                    <td>1</td>
                </tr>
            </table>
        </div>
        <c:choose>
            <c:when test="${locale == 'en_US'}">
                <p>Before the start of the first spin (when you visit the game page first time, when you switch the game
                    mode (to real money / demo), by pressing the "Spin" button after the previous streak is completed),
                    game data with the results of the future 10 games (streak) is automatically generated. The result of
                    each streak is represented by a string in format (X)X-(Y)Y-(Z)Z (eg 1-42-17), where:</p>
                <dl>
                    <dt>(X)X</dt>
                    <dd class="with-comment">the number of the character on which the 1st reel of the slot will randomly
                        stop
                    </dd>
                    <dt>(Y)Y</dt>
                    <dd class="with-comment">the number of the character on which the 2nd reel of the slot will randomly
                        stop
                    </dd>
                    <dt>(Z)Z</dt>
                    <dd class="with-comment">the number of the character on which the 3rd reel of the slot will randomly
                        stop
                    </dd>
                </dl>
                <p class="comment">number of the first (upper) symbol on each reel (on the game line 2 (top line)).</p>
                <p>Prior to the beginning of the game, the player can set the offset for each reel separately in the
                    form of an integer from 0 to 59. Offset is set by pressing the "+" and "-" buttons in the "Offset"
                    section on the "Control panel" on the game page for each of the reels.</p>
                <p>You can change the offset before each spin (thus the result of the game does not depend on the casino
                    in any way). The offset in the statistics of games is represented by a string in format
                    (A)A-(B)B-(C)C (eg 0-14-5), where:</p>
                <dl>
                    <dt>(A)A</dt>
                    <dd>offset for reel 1</dd>
                    <dt>(B)B</dt>
                    <dd>offset for reel 2</dd>
                    <dt>(C)C</dt>
                    <dd>offset for reel 3</dd>
                </dl>

                <h3>Formation of the game result</h3>
                <p>During the game, each of the three reels of the slot will stop on a symbol with a sequence number
                    defined as the sum of the random number specified in the casino gaming data and the offset indicated
                    by the player before the spin:</p>
                <ul>
                    <li>Reel #1 = XX+AA</li>
                    <li>Reel #2 = YY+BB</li>
                    <li>Reel #3 = ZZ+CC</li>
                </ul>
                <p>If the addition result is greater than 60, then 60 is subtracted from the result.</p>
                <p>Such a mechanism for the formation of the game result makes it completely random and independent from
                    the casino.</p>

                <h3>Example</h3>
                <p>The result of the spin in the gaming data is written down by a string 22-39-16.<br/>
                    The player set offset before the spin 20-30-40.<br/>
                    That means that the result of the spin would be:</p>
                <ul>
                    <li>reel # 1 should stop at the symbol 22+20=42, which according to the table corresponds to the
                        symbol "Watermelon"
                    </li>
                    <li>reel #2 should stop at the symbol 39+30=69 69-60=9, which according to the table corresponds to
                        the symbol "Cherry"
                    </li>
                    <li>reel #3 should stop at the symbol 16+40=56, which according to the table corresponds to the
                        symbol "Grapes"
                    </li>
                </ul>
                <p>In the game results, information about the symbols falling on the second (top) line of the slot is
                    recorded. The symbols on lines 1 and 3 correspond to the symbols that are on the reel (see the
                    symbol allocation table) next and one from the symbols indicated in the results.</p>
                <p>So in this example:</p>
                <ul>
                    <li>line 1 (middle) will show symbols 43, 10, 57</li>
                    <li>line 3 (bottom) will show symbols 44, 11, 58</li>
                </ul>

                <h3>Game data format</h3>
                <p>The results of the games in the order in which they were issued by the random number generator are
                    included in a secret text string in format 7-12-56_23-47-19 _...._ 1-59-60. The results of the
                    games go strictly in order from the first game to the last, and it is in this order that they will
                    fall out during the game.</p>
                <p>Before the game begins, the secret data line is signed with an electronic digital signature using the
                    MD5 algorithm (RS4 Data Security, Inc. MD5 Message-Digest Algorithm), and the received signature is
                    available to the player immediately, before the streak starts, and the content of this line remains
                    hidden from him until the end of the streak. This demonstrates to the player that the streak and its
                    signature were created on-line.</p>
                <p>The player can save the received signature on his computer or control its integrity during the game
                    from the screen of the monitor in a special window "Streak".</p>
                <p>At the end of the streak, the contents of the secret string become available to the player. Thus, the
                    player can check at the end of a streak: the correctness of the results, both in terms of values
                    ​​and in the order in which they fall, also the player can check the identity of the signature shown
                    to him before the game and the signature that he will receive when the secret string is re-signed,
                    which became available to him. Coincidence of signatures unequivocally indicates that game data has
                    not changed during the game, the game was conducted fairly, and its result is completely random.</p>
                <p>After the streak is completed, a new streak will be created automatically, about what the player can
                    be notified by noticing that the inscription contents of the "Streak" window has been changed (the
                    contents of the window will be the signature of the new streak signature).</p>
                <p>It is assumed that the player, before completing the game, will perform all necessary checks.
                    Verification can only be performed after the streak is completed. If the streak is not completed,
                    but the player does not want to continue the game for real money, he can click the "Finish" button
                    without making any money bets. In this case, the decoded secret line of the streak will appear in
                    the "Streak" window.</p>
                <p>When you switch from real money mode to "Demo" mode and vice versa, the series automatically
                    completes and a new one is generated.</p>
                <p>At the end of the game, the player can view all the statistics of the games in personal account.</p>
                <p>The streak is available for completion to the player throughout his activity on the site. 30 minutes
                    after the last activity of the player or after Logout, the series ends automatically and becomes
                    unavailable for continuation.</p>
            </c:when>
            <c:otherwise>
                <p>Перед началом первой игры (при первом посещении страницы игры, при переключении режима игры (на
                    реальные дениги/демо), при нажатии кнопки "Спин" после завершения предыдущей серии) в автоматическом
                    режиме генерируются игровые данные с результатами будущих 10-ти игр (серия). Результат каждой из игр
                    представляется строкой формата (X)X-(Y)Y-(Z)Z (напр. 1-42-17), где:</p>
                <dl>
                    <dt>(X)X</dt>
                    <dd class="with-comment">номер символа, на котором случайно остановится 1-й барабан слота</dd>
                    <dt>(Y)Y</dt>
                    <dd class="with-comment">номер символа, на котором случайно остановится 2-й барабан слота</dd>
                    <dt>(Z)Z</dt>
                    <dd class="with-comment">номер символа, на котором случайно остановится 3-й барабан слота</dd>
                </dl>
                <p class="comment">номер первого (верхнего) символа на каждом барабане (по игровой линии 2
                    (верхней)).</p>
                <p>До начала игры по созданной серии игрок может установить для каждого барабана в отдельности отступ в
                    виде целого числа от 0 до 59. Отступ устанавливается нажатием на кнопки "+" и "-" в секции "Отступ"
                    на "Панели управления" на странице игры для каждого из барабанов.</p>
                <p>Изменять отступ можно перед каждым спином (таким образом результат игры никаким образом не зависит от
                    казино). Смещение в статистике игр представляется строкой формата (A)A-(B)B-(C)C (напр. 0-14-5),
                    где:</p>
                <dl>
                    <dt>(A)A</dt>
                    <dd>смещение для барабана 1</dd>
                    <dt>(B)B</dt>
                    <dd>смещение для барабана 2</dd>
                    <dt>(C)C</dt>
                    <dd>смещение для барабана 3</dd>
                </dl>

                <h3>Формирование результата игры</h3>
                <p>В ходе игры каждый из трех барабанов слота будет останавливаться на символе с порядковым номером,
                    определяемым как сумма случайного номера, заданного в игровых данных казино и смещения, указанного
                    игроком перед спином:</p>
                <ul>
                    <li>Барабан #1 = XX+AA</li>
                    <li>Барабан #2 = YY+BB</li>
                    <li>Барабан #3 = ZZ+CC</li>
                </ul>
                <p>Если результат от сложения больше 60, то от результата вычитается 60.</p>
                <p>Такой механизм формирования результата игры делает его абсолютно случайным и независимым от
                    казино.</p>

                <h3>Пример</h3>
                <p>Результат игры в игровых данных записан строкой 22-39-16.<br/>
                    Перед началом игры игрок задал смещение 20-30-40.<br/>
                    Это означает, что в результате игры:</p>
                <ul>
                    <li>барабан #1 должен остановиться на символе 22+20=42, что по таблице соответствует символу
                        "Арбуз"
                    </li>
                    <li>барабан #2 должен остановиться на символе 39+30=69 69-60=9, что по таблице соответствует символу
                        "Вишня"
                    </li>
                    <li>барабан #3 должен остановиться на символе 16+40=56, что по таблице соответствует символу
                        "Виноград"
                    </li>
                </ul>
                <p>В результатах игры записывается информация о символах, выпадающих на второй (верхней) линии слота.
                    Символы на линиях 1 и 3 соответствуют символам, которые находятся на барабане (см. таблицу
                    размещения символов) следующими и через один от указанного в результатах символа.</p>
                <p>Т.о. в приведенном примере:</p>
                <ul>
                    <li>на линии 1 (средней) выпадут символы 43, 10, 57</li>
                    <li>на линии 3 (нижней) выпадут символы 44, 11, 58</li>
                </ul>

                <h3>Формат игровых данных</h3>
                <p>Результаты игр в том порядке, в котором они были выданы генератором случайных чисел, включаются в
                    секретную текстовую строку вида 7-12-56_23-47-19_...._1-59-60. Результаты игр идут строго по порядку
                    от первой игры к последней, и именно в таком порядке они будут выпадать в ходе игры.</p>
                <p>Перед началом игры секретная строка данных подписывается электронной цифровой подписью (ЭЦП) по
                    алгоритму MD5 (RSA Data Security, Inc. MD5 Message-Digest Algorithm), и полученная сигнатура
                    становится доступной игроку сразу же, до начала игры по серии, а содержание этой строки остается
                    скрытым от него до конца игры по серии. Это демонстрирует игроку, что серия и подпись под ней были
                    созданы в режиме он-лайн.</p>
                <p>Игрок может сохранить полученную сигнатуру на своем компьютере или контролировать ее целостность в
                    процессе игры с экрана монитора в специальном окне "Серия".</p>
                <p>По окончании серии игр игроку становится доступно содержание секретной строки. Таким образом, игрок
                    может проверить по окончании серии игр: правильность выпадения результатов, как по значениям ,так и
                    по порядку их выпадения, также игрок может проверить идентичность показанной ему перед началом игры
                    сигнатуры и той сигнатуры, которая будет получена им при повторном подписании секретной строки,
                    ставшей ему доступной. Совпадение сигнатур однозначно свидетельствует о том, что в процессе игры
                    игровые данные не менялись, игра велась честно, и ее результат полностью случаен.</p>
                <p>После завершения серии новая серия будет создана автоматически, о чем игрок может быть уведомлен
                    изменением надписи в окне "Серия" (содержанием окна станет сигнатура новой серии).</p>
                <p>Предполагается, что игрок, перед тем как завершить игру, будет выполнять все необходимые проверки.
                    Проверка может быть выполнена только после завершения серии. Если серия не завершена, но игрок не
                    хочет продолжать игру на деньги, он может нажать кнопку "Финиш", не делая денежных ставок.
                    При этом в окне "Серия" появится расшифрованная секретная строка серии.</p>
                <p>При переходе из режима игры на реальные деньги в "Демо" режим и обратно серия автоматически
                    завершается и генерируется новая.</p>
                <p>По окончании игры игрок может посмотреть всю статистику игр в личном кабинете.</p>
                <p>Серия доступна для завершения игроку на протяжении всей его активности на сайте. Спустя 30 минут
                    после последней активности игрока либо после выхода из системы (Logout) серия завершается
                    автоматически и становится недоступной для продолжения.</p>
            </c:otherwise>
        </c:choose>
    </section>
</main>
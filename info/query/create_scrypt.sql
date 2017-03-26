-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema jcasino
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema jcasino
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jcasino`
  DEFAULT CHARACTER SET utf8;
USE `jcasino`;

-- -----------------------------------------------------
-- Table `jcasino`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`user` (
  `id`                INT UNSIGNED             NOT NULL AUTO_INCREMENT
  COMMENT 'ID пользователя.\n\nUser’s id.',
  `password_md5`      VARCHAR(32)              NOT NULL
  COMMENT 'Пароль, зашифрованный MD5.\n\nPassword encrypted by MD5.\n\nAvailable signs: [a-z] [A-Z] [0-9] + - = ! ? ( )\nMust have at least:\n- 1 uppercase letter;\n- 1 lowercase letter;\n- 1 digit;\n- min 8 symbols.\n\nWhy? - We want it that way!',
  `email`             VARCHAR(320)             NOT NULL
  COMMENT 'E-mail пользователя, используемый как login для входа в систему.\n\nUser’s email which is used as login too to sing-in system.\n\nPS: max amount of signs in e-mail in different «Simple MAIL Transfer Protocols»:\n64 + @ + 64 = 129\nor\n64 + @ + 255 = 320 - use this because it’s bigger.',
  `role`              ENUM ('player', 'admin') NOT NULL DEFAULT 'player'
  COMMENT 'Игрок или администратор?\n\nPlayer or admin?',
  `registration_date` DATETIME                 NOT NULL
  COMMENT 'Для того, чтобы следить как долго пользователь зарегистрирован в системе (может применятся в будущем для разработки системы персональных бонусов).\n\nTo monitor how long user is registered in days. Fills automatically after registration.',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_email_UNIQUE` (`email` ASC),
  INDEX `role_idx` (`role` ASC)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  COMMENT = 'Пользователи, зарегистрированные в системе.\n\nUsers registered in system.';

-- -----------------------------------------------------
-- Table `jcasino`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`player` (
  `id`            INT UNSIGNED   NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `fname`         VARCHAR(70)    NULL
  COMMENT 'Имя игрока (не обязательно, т. к. позже мы получим скан его паспорта и запросим дополнительно при верификации).\n\nPlayer’s first name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `mname`         VARCHAR(70)    NULL
  COMMENT 'Отчество игрока (не обязательно, т. к. позже мы получим скан его паспорта).\n\nPlayer’s middle name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `lname`         VARCHAR(70)    NULL
  COMMENT 'Фамилия игрока (не обязательно, т. к. позже мы получим скан его паспорта).\n\nPlayer’s last name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `birthdate`     DATE           NOT NULL
  COMMENT 'Дата рождения игрока (обязательно, чтобы удостовериться что игроку есть 18).\n\nPlayer’s birthday date (is necessary to be sure that player is 18 years old).',
  `passport`      VARCHAR(30)    NULL
  COMMENT 'Номер паспорта игрока (не обязательно, т. к. позже мы получим скан его паспорта и запросим дополнительно при верификации).\n\nPlayer’s passport number (is not necessary to have it, because we will receive player’s passport scan in future and will request it during verification).',
  `passport_scan` MEDIUMBLOB     NULL
  COMMENT 'Нужен для верификации.\nМаксимальный размер - 16 мб (нужен скан хорошего разрешения).\n\nNeeds for verification.\nMax size - 16 mb (we need scan in good resolution).',
  `question`      VARCHAR(255)   NULL
  COMMENT 'Секретный вопрос для восстановления забытого пароля.\n\nPlayer’s secret-question to recover forgotten password.',
  `answer_md5`    VARCHAR(32)    NULL
  COMMENT 'Ответ на секретный вопрос для восстановления забытого пароля (ввода нового). Шифруется MD5.\n\nPlayer’s answer on secret-question to recover forgotten password (set new). Encrypts by MD5.',
  `visit_date`    DATETIME       NULL
  COMMENT 'Время и дата последнего визита.\n\nLatest visit date and time.',
  `credit`        DECIMAL(12, 2) NULL DEFAULT 0
  COMMENT 'Количество денег.\n\nAmount of credits available to player.',
  PRIMARY KEY (`id`),
  INDEX `visit_date_idx` (`visit_date` ASC),
  INDEX `credit_idx` (`credit` ASC),
  CONSTRAINT `fk_player_user`
  FOREIGN KEY (`id`)
  REFERENCES `jcasino`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Информация об игроке.\n\nPlayer’s info.';

-- -----------------------------------------------------
-- Table `jcasino`.`loan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`loan` (
  `id`          INT(11) UNSIGNED       NOT NULL AUTO_INCREMENT
  COMMENT 'Порядковый номер займа.\n\nIndex number of loan.',
  `player_id`   INT UNSIGNED           NOT NULL
  COMMENT 'ID игрока. Игрок не может взять займ, если у него не погашен предыдущий (контролируется на уровне клиент-серверной части).\n\nPlayer’s id.',
  `amount`      DECIMAL(9, 2) UNSIGNED NOT NULL
  COMMENT 'Количество занимаемых средств.\n\nAmount of credits loaned.',
  `acquire`     DATE                   NOT NULL
  COMMENT 'Дата получения займа.\n\nDate of acquiring the loan.',
  `expire`      DATE                   NOT NULL
  COMMENT 'Срок действия займа. Если займ не погашен к данному сроку, к игроку применяются штрафные санкции.\n\nExpiry date of the loan. If the loan is not paid before that date, the player will have to pay a fee.',
  `percent`     DECIMAL(4, 2) UNSIGNED NOT NULL
  COMMENT 'Месячный процент по займу.\n\nLoan’s month’s percentage.',
  `amount_paid` DECIMAL(9, 2)          NULL
  COMMENT 'Сколько уже оплачено (можно погашать долг частями).',
  `amount_rest` DECIMAL(9, 2)          NULL
  COMMENT 'Сколько осталось заплатить с учетом ежемесячного начисления процентов и штрафа за неуплату (при его наличии). Проценты и штрафы начисляются в зависимости от значения данного поля. Как только данное поле равно 0 - займ погашен. Проверяется клиент-серверной частью каждый раз при оплате.',
  PRIMARY KEY (`id`),
  INDEX `fk_loan_player_idx` (`player_id` ASC),
  INDEX `status_idx` (`status` ASC),
  INDEX `expire_idx` (`expire` ASC),
  CONSTRAINT `fk_loan_player`
  FOREIGN KEY (`player_id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Список займов.\n\nList of loans.';

-- -----------------------------------------------------
-- Table `jcasino`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`transaction` (
  `id`        INT(11) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'Порядковый номер транзакции.\n\nIndex number of the transaction.',
  `player_id` INT UNSIGNED     NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `date`      DATETIME         NOT NULL
  COMMENT 'Дата и время транзакции.\n\nDate and time of the transaction.',
  `amount`    DECIMAL(9, 2)    NOT NULL
  COMMENT 'Количество средств.\nОграничение на транзакцию - [+/-] 9’999’999.99.\nНе может быть меньше определенной величины (проверяется на стороне клиент-серверной части).\n\nAmount of money.\nTransaction limit - [+/-] 9’999’999.99.\nCan’t be less than specified value (that value is not contained in database).',
  PRIMARY KEY (`id`),
  INDEX `fk_transaction_player1_idx` (`player_id` ASC),
  CONSTRAINT `fk_transaction_player`
  FOREIGN KEY (`player_id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Список транзакиций игроков\n\nList of players’ transactions.';

-- -----------------------------------------------------
-- Table `jcasino`.`question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`question` (
  `id`           INT(11) UNSIGNED                                                      NOT NULL AUTO_INCREMENT
  COMMENT 'Порядковый номер вопроса.\n\nIndex number of question.',
  `player_id`    INT UNSIGNED                                                          NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `topic`        ENUM ('transaction', 'loan', 'rules', 'verification', 'ban', 'other') NOT NULL
  COMMENT 'Тема вопроса.\n\nTopic of the question.',
  `question`     VARCHAR(700)                                                          NOT NULL
  COMMENT 'Текст вопроса.\n\nText of the question.',
  `admin_id`     INT UNSIGNED                                                          NULL
  COMMENT 'ID администратора.\n\nAdmin’s id.',
  `answer`       VARCHAR(700)                                                          NULL
  COMMENT 'Текст ответа администратора.\n\nText of admin’s answer.',
  `satisfaction` ENUM ('best', 'good', 'norm', 'bad', 'worst')                         NULL
  COMMENT 'Уровень удовлетворенности игрока ответом администратора.\n\n*Может быть NULL - игрок не захотел оценивать ответ.\n\nThe degree of user’s satisfaction with the admin’s answer.\n\n*Can be NULL - player didn’t want to evaluate admin’s answer.',
  PRIMARY KEY (`id`),
  INDEX `fk_question_player_idx` (`player_id` ASC),
  INDEX `fk_question_user_idx` (`admin_id` ASC),
  INDEX `topic_idx` (`topic` ASC),
  INDEX `satisfaction_idx` (`satisfaction` ASC),
  CONSTRAINT `fk_question_player`
  FOREIGN KEY (`player_id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_question_user`
  FOREIGN KEY (`admin_id`)
  REFERENCES `jcasino`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Список вопросов игроков и ответов администраторов.\n\nList of users’ question and admins’ answers.';

-- -----------------------------------------------------
-- Table `jcasino`.`stats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`stats` (
  `id`               INT UNSIGNED            NOT NULL
  COMMENT 'Id игрока.\n\nPlayer’s id.',
  `max_bet`          DECIMAL(5, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'Максимальная ставка на один спин.\n\nMax bet for one spin.',
  `total_bet`        DECIMAL(12, 2) UNSIGNED NULL DEFAULT 0
  COMMENT 'Сумма всех ставок игрока.\n\nTotal bet sum made by player.',
  `max_win`          DECIMAL(9, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'Максимальный выигрыш за один спин.\n\nMax win for one spin.',
  `max_win_streak`   DECIMAL(9, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'максимальный выигрыш за одну серию (20 спинов, см. правила).\n\nMax win for one streak.',
  `total_win`        DECIMAL(12, 2) UNSIGNED NULL DEFAULT 0
  COMMENT 'Суммарный выигрыш за все серии.\n\nTotal win for all streaks.',
  `max_payment`      DECIMAL(9, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'Максимальный платеж.\n\nMax payment.',
  `total_payment`    DECIMAL(12, 2) UNSIGNED NULL DEFAULT 0
  COMMENT 'Сумма всех платежей.\n\nTotal sum of payments.',
  `max_withdrawal`   DECIMAL(9, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'Максимальный вывод.\n\nMax withdrawal.',
  `month_withdrawal` DECIMAL(9, 2) UNSIGNED  NULL DEFAULT 0
  COMMENT 'Вывод в текущем месяце.\nДля мониторинга состояния счета игрока на предмет не превышения максимально допустимой суммы вывода в месяц в соответствии с его статусом.\n\nCurrent month withdrawal.\nTo monitor withdrawal limit.',
  `total_withdrawal` DECIMAL(12, 2) UNSIGNED NULL DEFAULT 0
  COMMENT 'Сумма всех выводов.\n\nTotal sum of withdrawals.',
  PRIMARY KEY (`id`),
  INDEX `max_win_streak_idx` (`max_win_streak` ASC)
)
  ENGINE = InnoDB
  COMMENT = 'Статистика игрока.\nМы создали таблицу отдельно, т. к. планируем обрабатывать ее данные отдельно (и чаще).\n\nPlayer’s statistics table.\nWe have defined this table apart from basic table «player» because we plan to process it’s data apart of basic table «player».';

-- -----------------------------------------------------
-- Table `jcasino`.`verification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`verification` (
  `id`         INT UNSIGNED NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `email_code` CHAR(8)      NULL
  COMMENT 'Случайно генерированный код, отправляемый на e-mail игрока.\n\nЕсли пользователь вводит верный код в специальную форму, система изменяет его бит-маску его ‘verification_status’ определенным образом.\n\nГенерируется при каждом новом запросе игрока на верификацию. Действителен в течение 24 часов.\n\n??? Может быть его тоже шифровать???\n\nRandomly generated code which sends to player’s e-mail to verify it.\n\nIf user enters right verification code in the specified form system changes his verification_status bit-mask in a certain way.\n\nGenerates each time when user clicks the verification button and is valid for 24 hours.',
  `try_date`   DATETIME     NULL
  COMMENT 'Дата и время последней попытки верификации.\n\nЕсли более 24 часов прошло с момента отправки верификационного кода на e-mail игрока, игрок должен заново запросить код.\n\nDate and time of latest e-mail verification try. \n\nIf more than 24 hours passed since verification code was sent to player’s e-mail player has to ask for a new code.',
  `status`     BIT(3)       NOT NULL
  COMMENT 'Bit-mask with verification information about player: [abc]\nc - all necessary fields in table «player» are filled;\nb - e-mail is verified;\na - passport scan is verified.\n\nPlayer is verified when [abc] = [111].\n\nThe process of verification:\nFirst - player verifies his e-mail and fills all necessary fields in table «player».\nNext - admin gets the information about player (when player’s verification_status bit-mask becomes 011) and checks his passport data (data in table «player» including passport-scan).\nIf it’s ok (a = 1) - player can withdraw money.\n\n*Player can do payments and play without verification.',
  `admin_id`   INT UNSIGNED NULL
  COMMENT 'Администратор, который верифицировал игрока.\n\nAdmin who verified player.',
  `date`       DATETIME     NULL
  COMMENT 'Дата верификации игрока.\n\nDate player was verified by specified admin (admin_id).',
  PRIMARY KEY (`id`),
  INDEX `fk_verification_admin_idx` (`admin_id` ASC),
  INDEX `status_idx` (`status` ASC),
  CONSTRAINT `fk_verification_player`
  FOREIGN KEY (`id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_verification_user_admin`
  FOREIGN KEY (`admin_id`)
  REFERENCES `jcasino`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Таблица состояния верификации игроков. Мы создали таблицу отдельно, т. к. планируем обрабатывать ее данные отдельно.\n\n\nPlayers’ verification state table.\nWe have defined this table apart from basic table «user» because we plan to process it’s data apart of basic table «user».';

-- -----------------------------------------------------
-- Table `jcasino`.`streak`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`streak` (
  `id`        INT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT 'Порядковый номер серии.\n\nIndex number of streak.',
  `player_id` INT UNSIGNED  NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `date`      DATETIME      NOT NULL
  COMMENT 'Время и дата генерации серии.\n\nDate and time when streak was generated.',
  `roll`      VARCHAR(255)  NOT NULL
  COMMENT 'Случайно генерированная строка определенного формата, содержащая информацию о серии из 20 спинов. Возвращается игроку по окончании серии для реализации контроля честности.\n\nRandomly-generated string line in a special format which contains info about next 20 spins.',
  `roll_md5`  VARCHAR(255)  NOT NULL
  COMMENT 'Строка серии с солью зашифрованная MD5. Предоставляется игроку перед началом очередной серии для реализации контроля честности.\n\nRoll-string + «salt» encrypted with MD5 algorithm.',
  `offset`    VARCHAR(255)  NULL
  COMMENT 'Строка определенного формата, содержащая информацию смещениях барабана слот-машины, сделанных игроком на протяжении серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами).\n\nString line in a special format which contains info about offsets made by player during the streak.',
  `bet`       VARCHAR(255)  NULL
  COMMENT 'Строка определенного формата, которая содержит информацию о ставках, сделанных игроком на протяжении серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами).\n\nString line in a special format which contains info about bets made by player during the streak.',
  `result`    DECIMAL(7, 2) NULL
  COMMENT 'Результат серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами). Хранится в отдельной ячейке для того, чтобы не пересчитывать его, извлекая данные из форматированных строк.\n\nStreak result.',
  PRIMARY KEY (`id`),
  INDEX `fk_streak_player_idx` (`player_id` ASC),
  INDEX `date_idx` (`date` ASC),
  INDEX `result_idx` (`result` ASC),
  CONSTRAINT `fk_streak_player`
  FOREIGN KEY (`player_id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Список серий, сгенерированных слот-машиной.\n\nList of streaks generated by slot-machine.';

-- -----------------------------------------------------
-- Table `jcasino`.`player_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`player_status` (
  `status`           ENUM ('basic', 'vip', 'ban', 'unactive') NOT NULL
  COMMENT 'Статусы:\n- ‘simple’ - присваивается при регистрации;\n- ‘vip’ - имеет более высокие лимиты и низкие проценты на займ;\n- ‘ban’ - не может играть на деньги и выводить деньги;\n- ‘inactive’ - если дата последнего визита более года назад, необходимо связаться с администратором для обновления данных.\n\nStatuses:\n- ‘simple’ - just simple status;\n- ‘vip’ - has higher bet-limit and lower loan percent, has no withdrawal limit;\n- ‘ban’ - can’t play on money and withdraw money;\n- ‘inactive’ - if last visit time was a year ago (needs to contact admins to update info).',
  `bet_limit`        DECIMAL(5, 2) UNSIGNED                   NOT NULL
  COMMENT 'Максимально возможная ставка на один спин.\n\nMax bet available for player for one spin.',
  `withdrowal_limit` DECIMAL(9, 2) UNSIGNED                   NOT NULL
  COMMENT 'Ограничение на вывод средств в месяц.\n\nMonth’s withdrawal limit for player.',
  `loan_percent`     DECIMAL(4, 2) UNSIGNED                   NOT NULL
  COMMENT 'Процент по займу.\n\nLoan’s percent.',
  PRIMARY KEY (`status`)
)
  ENGINE = InnoDB
  COMMENT = 'Список статусов игрока.\n\nPlayer’s status list.';

-- -----------------------------------------------------
-- Table `jcasino`.`player_status_m2m_player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jcasino`.`player_status_m2m_player` (
  `id`         INT UNSIGNED                             NOT NULL
  COMMENT 'ID игрока.\n\nPlayer’s id.',
  `status`     ENUM ('basic', 'vip', 'ban', 'unactive') NOT NULL DEFAULT 'basic'
  COMMENT 'Статус игрока.\n\nPlayer’s status.',
  `admin_id`   INT UNSIGNED                             NULL
  COMMENT 'ID админа.\n\nAdmin’s id.',
  `commentary` VARCHAR(700)                             NULL
  COMMENT 'Комментарии админа (прошлый статус, причина изменения статуса и т. п).\n\nAdmin comments.',
  PRIMARY KEY (`id`),
  INDEX `fk_player_status_m2m_player_player_status_idx` (`status` ASC),
  INDEX `fk_player_status_m2m_player_admin_idx` (`admin_id` ASC),
  CONSTRAINT `fk_player_status_m2m_player_player_status`
  FOREIGN KEY (`status`)
  REFERENCES `jcasino`.`player_status` (`status`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_player_status_m2m_player_player`
  FOREIGN KEY (`id`)
  REFERENCES `jcasino`.`player` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_player_status_m2m_player_admin`
  FOREIGN KEY (`admin_id`)
  REFERENCES `jcasino`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  COMMENT = 'Соответствие id игрока, его статуса и админа, изменившего статус, с комментариями админа.';


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

-- 1) запросы с WHERE
-- выборка информации о всех сериях игрока для отображения
SELECT
  `date`,
  `roll`,
  `offset`,
  `bet`,
  `result`
FROM `streak`
WHERE `player_id` = 2; # id конкретного игрока

-- выборка вопросов игроков без ответа для обработки их админами
SELECT
  `id`,
  `player_id`,
  `topic`,
  `question`,
  `q_date`
FROM `question`
WHERE `answer` <=> NULL; # или IS NULL



-- 2) запросы с встроенными функциями
-- определение количества дней, в течение которых пользователь зарегистрирован в системе
SELECT TIMESTAMPDIFF(DAY,
                     (SELECT `registration_date`
                      FROM `user`
                      WHERE `id` = 2), # id конкретного пользователя
                     now())
  AS 'days';

-- вставка записи при регистрации нового пользователя
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES (MD5('vak26HDm'), LOWER('zEn-tOr@aliens.com'), 'admin', NOW());

-- 3) запросы с JOIN
-- выборка администратором пользователей готовых к авторизации паспортных данных
SELECT
  `user`.`id`,
  CONCAT_WS(' ', `fname`, `mname`, `lname`)  AS `full_name`,
  `birthdate`,
  `passport`,
  `passport_scan`                            AS `scan`,
  LPAD(BIN(`verification`.`status`), 3, '0') AS `status`
FROM `user`
  NATURAL JOIN `player`
  NATURAL JOIN `verification`
WHERE `verification`.`status` = b'011'
      AND `passport_scan` IS NOT NULL;

-- список пользователей зарегистрированных в системе с их полными именами
-- (у админов отображаемое на сайте (в ответах на вопросы игроков) имя - `JCasino`, поэтому присваивать им имена
-- в БД не имеет смысла)
-- п. с. более разумного прикладного применения LEFT/RIGHT JOIN'у для своей системы я не придумал
SELECT
  `id`,
  `email`,
  `role`,
  CONCAT_WS(' ', `fname`, `mname`, `lname`) AS `name`
FROM `user`
  LEFT JOIN `player` USING (`id`);

-- 4) запросы с GROUP BY HAVING и агрегатными функциями
-- вывод количества игроков по каждому статусу
SELECT
  UPPER(`player_status`.`status`) AS `stat`,
  COUNT(*)                        AS `total`
FROM `player`
  NATURAL JOIN `player_status_m2m_player`
  #USING (`id`)
  NATURAL JOIN `player_status` #USING (`status`)
GROUP BY `status`;

-- вывод суммы транзакций по месяцам определенного года
SELECT
  MONTHNAME(`date`) AS `month`,
  SUM(`amount`)     AS `total`
FROM `transaction`
WHERE YEAR(`date`) = '2017'
GROUP BY `month`;

-- вывод пользователей играющих в минус (сумма транзакций положительная; выводят меньше, чем закидывают)
SELECT
  `player`.`id`,
  `fname`,
  SUM(`transaction`.`amount`) AS `total`
FROM `player`
  JOIN `transaction` ON `player`.`id` = `transaction`.`player_id`
GROUP BY `player_id`
HAVING `total` > 0;

-- 5) запрос с UNION
-- получение отчета работы казино
SELECT
  'players',
  '',
  ''
UNION
SELECT
  'id',
  'visit_date',
  'credit'
UNION
SELECT
  `id`,
  `visit_date`,
  `credit`
FROM `player`
UNION
SELECT
  'loans',
  '',
  ''
UNION
SELECT
  'player_id',
  'expire',
  'amount_rest'
UNION
SELECT
  `player_id`,
  `expire`,
  `amount_rest`
FROM `loan`
WHERE `amount_rest` > 0
UNION
SELECT
  'transactions',
  '',
  ''
UNION
SELECT
  'player_id',
  'date',
  'amount'
UNION
SELECT
  `player_id`,
  `date`,
  `amount`
FROM `transaction`;

-- 6) запросы с подзапросами
-- не взаимосвязанный, вывод топ 10 игроков (условие попадания в топ 10 -
-- общая сумма выигрыша игрока больше средней суммы общих выигрышей всех игроков, которые посещали систему
-- не более 30 дней назад)
SELECT
  `id`,
  `fname`     AS `name`,
  `total_win` AS `total`
FROM `stats`
  NATURAL JOIN `player`
WHERE (SELECT AVG(`total_win`)
       FROM `stats`
         NATURAL JOIN `player`
       WHERE (TO_DAYS(NOW()) - TO_DAYS(`visit_date`)) <= 30) < `total_win`
ORDER BY `total` DESC
LIMIT 10;

-- взаимосвязанный, вывод суммы транзакций игроков, которые сделали 3 и более транзакций
SELECT
  `player_id`   AS `id`,
  sum(`amount`) AS `total`
FROM `transaction` AS `outer`
WHERE
  (SELECT COUNT(*)
   FROM `transaction` AS `inner`
   WHERE `outer`.`player_id` = `inner`.`player_id`) >= 3
GROUP BY `player_id`;
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
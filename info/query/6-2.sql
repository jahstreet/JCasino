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
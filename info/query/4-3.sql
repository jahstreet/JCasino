-- вывод игроков, играющих в минус (сумма транзакций положительная; выводят меньше, чем закидывают)
SELECT
  `player`.`id`,
  `fname`,
  SUM(`transaction`.`amount`) AS `total`
FROM `player`
  JOIN `transaction` ON `player`.`id` = `transaction`.`player_id`
GROUP BY `player_id`
HAVING `total` > 0;
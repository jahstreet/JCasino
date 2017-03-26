-- вывод суммы транзакций по месяцам определенного года
SELECT
  MONTHNAME(`date`) AS `month`,
  SUM(`amount`)     AS `total`
FROM `transaction`
WHERE YEAR(`date`) = '2017' #определенный год
GROUP BY `month`
ORDER BY MONTH(`month`);
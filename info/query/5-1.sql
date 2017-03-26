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
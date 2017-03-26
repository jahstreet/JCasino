-- выборка информации о всех сериях игрока для отображения
SELECT
  `date`,
  `roll`,
  `offset`,
  `bet`,
  `result`
FROM `streak`
WHERE `player_id` = 2; # id конкретного игрока
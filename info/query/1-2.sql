-- выборка вопросов игроков, не имеющих ответа, для обработки их админами
SELECT
  `id`,
  `player_id`,
  `topic`,
  `question`,
  `q_date`
FROM `question`
WHERE `answer` <=> NULL; # или IS NULL
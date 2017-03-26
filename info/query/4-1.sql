-- вывод количества игроков по каждому статусу
SELECT
  UPPER(`status`),
  COUNT(*) AS `total`
FROM `player`
  NATURAL JOIN `player_status_m2m_player`
  #USING (`id`)
  NATURAL JOIN `player_status` #USING (`status`)
GROUP BY `status`;
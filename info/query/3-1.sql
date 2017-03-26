-- выборка администратором пользователей, готовых к авторизации паспортных данных
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
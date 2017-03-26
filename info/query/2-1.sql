-- определение количества дней, в течение которых пользователь зарегистрирован в системе
SELECT TIMESTAMPDIFF(DAY,
                     (SELECT `registration_date`
                      FROM `user`
                      WHERE `id` = 2), # id конкретного пользователя
                     now())
  AS 'days';
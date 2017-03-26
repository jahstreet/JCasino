-- вставка записи при регистрации нового пользователя
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES (MD5('vak26HDm'), LOWER('zEn-tOr@aliens.com'), 'admin', NOW());
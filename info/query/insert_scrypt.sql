-- player status
INSERT INTO `jcasino`.`player_status` (`status`, `bet_limit`, `withdrowal_limit`, `loan_percent`)
VALUES ('basic', '5', '300', '20');
INSERT INTO `jcasino`.`player_status` (`status`, `bet_limit`, `withdrowal_limit`, `loan_percent`)
VALUES ('vip', '50', '10000', '5');
INSERT INTO `jcasino`.`player_status` (`status`, `bet_limit`, `withdrowal_limit`, `loan_percent`)
VALUES ('ban', '0', '0', '20');
INSERT INTO `jcasino`.`player_status` (`status`, `bet_limit`, `withdrowal_limit`, `loan_percent`)
VALUES ('unactive', '0', '0', '20');

-- user
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('5d3c47fddf8cbac13c1687c612f331d7', 'jahstreetlove@gmail.com', 'admin', '2017-01-13 12:28:00');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('58528c37b46102e31f68dd4de8ed2686', 'alien_by@tut.by', 'player', '2017-01-14 01:02:03');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('1278bb137a3052eff24650b6362aa802', 'andrew.burn1ng@gmail.com', 'player', '2017-01-15 14:33:16');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('7c916c4d2e84b4b8355ba93cbebe8d57', 'sasha88@yandex.by', 'player', '2017-01-23 17:20:44');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('a4ede711348b654fba68ba51ab25548d', 'igarrugar@tut.by', 'player', '2017-01-29 22:22:22');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('9dca9a8e052e1828faad779b8ac2218a', 'kybercriminal@mymail.xyz', 'player', '2017-02-10 21:30:00');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('731e0bd8d70f036b112e389661b7f34b', 'tramp@tramp.tr', 'player', '2017-02-17 16:10:07');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('f34072580b9ae7eb90ce859e1654fe82', 'soldat@army.net', 'player', '2017-02-23 11:11:11');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('9c63485db2cd7ca752178a2c841a847e', 'casino_hack@lol.by', 'admin', '2017-02-24 12.15.03');
INSERT INTO `jcasino`.`user` (`password_md5`, `email`, `role`, `registration_date`)
VALUES ('86e98be9d818b799d7fb7ddd2e4e6b44', 'raper@mc.net', 'player', '2017-02-25 17.33.22');

-- player (ответы на секретные вопросы пока не шифровал)
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES
  ('2', 'Александр', 'Альбертович', 'Сосновских', '1991-09-24', 'KH1731245', 'NULL', 'Фамилия Люды?', 'MD5 Миханчик',
        '2017-01-14 01:02:03', '10000.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES ('3', 'Андрей', 'Сергеевич', 'Душков', '1992-08-11', 'KH1833442', 'NULL', 'Имя Сержанта?', 'MD5 Карина',
             '2017-01-15 14:33:16', '1200.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES ('4', 'Александра', 'Анатольевна', 'Петрова', '1988-04-15', 'PB7573921', 'NULL', 'NULL', 'NULL',
             '2017-01-23 17:20:44', '728.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES
  ('5', 'John', 'NULL', 'Johnson', '1994-11-11', 'UM85646213', 'NULL', 'NULL', 'NULL', '2017-01-29 22:22:22', '901.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES ('6', 'Peter', 'NULL', 'Peterson', '1980-01-02', 'LL2677644L1', 'NULL', 'NULL', 'NULL', '2017-02-10 21:30:00',
             '1163.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES ('7', 'Victor', 'NULL', 'Delacrough', '1983-05-27', 'STP293PS1', 'NULL', 'NULL', 'NULL', '2017-02-17 16:10:07',
             '23.00');
INSERT INTO 'jcasino'.'player' (id, fname, mname, lname, birthdate, passport, passport_scan, question, answer_md5, visit_date, credit)
VALUES
  ('8', 'Самюель', 'Эл', 'Джексон', '1993-12-30', 'KH8573651', 'NULL', 'Кто твоя папа?', 'MD5 я', '2016-11-23 11:11:11',
        '4.00');
INSERT INTO `jcasino`.`player` (`id`, `fname`, `lname`, `birthdate`, `passport`, `question`, `answer_md5`, `visit_date`)
VALUES ('10', 'Samantha', 'Adams', '1986-10-12', 'MHG33HGE1', 'My birthdate', 'MD5 1986-10-12', '2017-02-27 22:17:44');

-- stats
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('2', '3', '173', '29', '144', '5582', '1000', '20000', '300', '132', '7662');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('3', '0', '0', '0', '0', '0', '1200', '1200', '0', '0', '0');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('4', '2', '715', '3', '23', '500', '100', '7231', '17', '17', '17');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('5', '4', '1433', '82', '288', '6563', '290', '720', '220', '200', '710');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('6', '2', '11', '2', '5', '7', '10', '10', '0', '0', '0');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('7', '4', '44', '5', '10', '15', '20', '25', '0', '0', '0');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('8', '10', '8547573', '757', '3006', '1020', '3000', '7577575', '93471', '385', '100000');
INSERT INTO `jcasino`.`stats` (`id`, `max_bet`, `total_bet`, `max_win`, `max_win_streak`, `total_win`, `max_payment`, `total_payment`, `max_withdrawal`, `month_withdrawal`, `total_withdrawal`)
VALUES ('10', '3', '74', '12', '18', '204', '200', '200', '0', '0', '0');

-- transaction
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('2', '2017-01-20 11-11-11', '10000');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('4', '2017-01-21 07-04-32', '300');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('3', '2017-01-22 20-10-00', '70');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('3', '2017-01-23 21-11-03', '20');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('3', '2017-01-24 22-40-30', '-35');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('8', '2017-02-10 17-15-51', '500');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('5', '2017-02-11 18-30-30', '300');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('6', '2017-02-12 12-15-55', '200');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('7', '2017-02-13 13-42-22', '100');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('4', '2017-02-14 12-01-42', '-15');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('8', '2017-02-15 23-11-03', '-10');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('5', '2017-02-16 20-15-10', '-100');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('10', '2017-02-27 22:17:40', '200');
INSERT INTO `jcasino`.`transaction` (`player_id`, `date`, `amount`) VALUES ('5', '2017-03-02 11:39:41', '-400');

-- loan
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('5', '150', '2017-02-23', '2017-03-23', '20', '0', '150');
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('7', '200', '2017-02-24', '2017-03-24', '20', '30', '170');
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('2', '500', '2017-02-25', '2017-03-25', '20', '500', '0');
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('10', '300', '2017-02-26', '2017-03-26', '20', '120', '180');
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('6', '500', '2017-02-27', '2017-03-27', '20', '320', '180');
INSERT INTO `jcasino`.`loan` (`player_id`, `amount`, `acquire`, `expire`, `percent`, `amount_paid`, `amount_rest`)
VALUES ('2', '350', '2017-03-24', '2017-04-24', '20', '100', '250');

-- question
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`)
VALUES ('7', 'other', 'Why are you so bad people?', '2017-02-23 12:17:22');
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`, `admin_id`, `answer`, `a_date`, `satisfaction`)
VALUES ('2', 'loan', 'Под какой процент я могу взять 500 долларов и на какой срок?', '2017-02-25 18:14:40', '1',
        '20% на 1 месяц!', '2017-02-25 18:14:50', 'best');
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`, `admin_id`, `answer`, `a_date`, `satisfaction`)
VALUES ('4', 'other', 'Lorem ipsum?', '2017-02-27 22:22:15', '9', 'Dolor sit amet.', '2017-03-02 12:15:24', 'good');
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`, `admin_id`, `answer`, `a_date`, `satisfaction`)
VALUES ('10', 'rules', 'Где я могу почитать правила игры?', '2017-03-02 17:21:40', '1', 'На странице «Правила игры»!',
        '2017-03-03 10:54:07', 'norm');
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`, `admin_id`, `answer`, `a_date`)
VALUES ('2', 'ban', 'Can you unban me? Plz…', '2017-03-17 09:10:18', '1', 'Yes, of course…', '2017-03-17 09:15:22');
INSERT INTO `jcasino`.`question` (`player_id`, `topic`, `question`, `q_date`)
VALUES ('7', 'verification', 'How can I call you, dear?)', '2017-03-17 18:33:40');

-- player status m2m player
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`) VALUES ('2', 'basic', '1');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`) VALUES ('3', 'basic', '1');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`, `commentary`)
VALUES ('4', 'vip', '9', 'Good player!!');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`, `commentary`)
VALUES ('5', 'ban', '1', 'Мошенник!');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`, `commentary`)
VALUES ('6', 'unactive', '1', 'Не заходил уже год.');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`) VALUES ('7', 'basic', '9');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`) VALUES ('8', 'basic', '9');
INSERT INTO `jcasino`.`player_status_m2m_player` (`id`, `status`, `admin_id`) VALUES ('10', 'basic', '1');

-- streak (генерация строк определенного формата и MD5 шифров пока не реализована)
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('2', '2017-02-23 17:13:22', 'line1', 'MD5 line1', '1line', 'anybet1', '40');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('3', '2017-02-24 12:17:43', 'line2', 'MD5 line2', '2line', 'anybet2', '-12');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('4', '2017-02-24 20:15:01', 'line3', 'MD5 line3', '3line', 'anybet3', '2.2');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('5', '2017-02-24 22:32:40', 'line4', 'MD5 line4', '4line', 'anybet4', '100');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `result`)
VALUES ('6', '2017-02-25 10:59:53', 'line5', 'MD5 line5', NULL, NULL);
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('7', '2017-02-25 12:22:00', 'line6', 'MD5 line6', '6line', 'anybet6', '-80.13');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('8', '2017-02-25 19:38:17', 'line7', 'MD5 line7', '7line', 'anybet7', '8.2');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`)
VALUES ('10', '2017-02-26 15:41:36', 'line8', 'MD5 line8', NULL);
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('2', '2017-02-26 20:51:10', 'line9', 'MD5 line9', '9line', 'anybet9', '17.7');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('2', '2017-02-26 23:10:22', 'line10', 'MD5 line10', '10line', 'anybet10', '-30.1');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('2', '2017-02-27 15:15:15', 'line11', 'MD5 line11', '11line', 'anybet11', '-1.2');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`, `bet`, `result`)
VALUES ('5', '2017-02-27 19:14:30', 'line12', 'MD5 line12', '12line', 'anybet12', '-42.33');
INSERT INTO `jcasino`.`streak` (`player_id`, `date`, `roll`, `roll_md5`, `offset`)
VALUES ('10', '2017-03-02 11:33:22', 'line13', 'MD5 line13', NULL);

-- verification
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`)
VALUES ('2', '203844fA', '2017-02-23 12:24:15', b'011');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`) VALUES ('3', NULL, NULL, b'000');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`) VALUES ('4', NULL, NULL, b'001');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`)
VALUES ('5', 'jJF48512', '2017-02-25 17:32:01', b'010');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`, `admin_id`, `date`)
VALUES ('6', 'hghbc99J', '2017-02-26 21:14:48', b'111', '9', '2017-02-28 10:46:31');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`, `admin_id`, `date`)
VALUES ('7', '745jhFd8', '2017-02-24 16:55:21', b'111', '1', '2017-02-25 12:11:46');
INSERT INTO `jcasino`.`verification` (`id`, `email_code`, `try_date`, `status`)
VALUES ('8', 'hHn65yh2', '2017-02-25 15:25:53', b'011');
INSERT INTO `jcasino`.`verification` (`id`, `status`) VALUES ('10', b'001');




































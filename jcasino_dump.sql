-- MySQL dump 10.13  Distrib 5.7.18, for macos10.12 (x86_64)
--
-- Host: localhost    Database: jcasino
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Порядковый номер займа.\n\nIndex number of loan.',
  `player_id` int(11) unsigned NOT NULL COMMENT 'ID игрока. Игрок не может взять займ, если у него не погашен предыдущий (контролируется на уровне клиент-серверной части).\n\nPlayer’s id.',
  `amount` decimal(9,2) unsigned NOT NULL COMMENT 'Количество занимаемых средств.\n\nAmount of credits loaned.',
  `acquire` date NOT NULL COMMENT 'Дата получения займа.\n\nDate of acquiring the loan.',
  `expire` date NOT NULL,
  `percent` decimal(4,2) unsigned NOT NULL COMMENT 'Месячный процент по займу.\n\nLoan’s month’s percentage.',
  `amount_paid` decimal(9,2) DEFAULT '0.00' COMMENT 'Сколько уже оплачено (можно погашать долг частями).',
  PRIMARY KEY (`id`),
  KEY `fk_loan_player_idx` (`player_id`),
  CONSTRAINT `fk_loan_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='Список займов.\n\nList of loans.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
INSERT INTO `loan` VALUES (1,5,150.00,'2017-02-23','2017-03-23',20.00,0.00),(2,7,200.00,'2017-02-24','2017-03-24',20.00,30.00),(3,2,500.00,'2017-02-25','2017-03-25',20.00,500.00),(4,10,300.00,'2017-02-26','2017-03-26',20.00,120.00),(5,6,500.00,'2017-02-27','2017-03-27',20.00,390.00),(6,2,350.00,'2017-03-24','2017-04-24',20.00,350.00),(7,2,13.20,'2017-03-26','2017-04-26',20.00,13.20),(8,2,8.40,'2017-03-26','2017-04-26',20.00,8.40),(9,2,24.00,'2017-04-06','2017-05-06',20.00,24.00),(10,15,6.00,'2017-04-13','2017-05-13',20.00,0.00),(11,4,52.50,'2017-05-17','2017-06-17',5.00,0.00);
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `header` varchar(45) NOT NULL,
  `text` varchar(700) NOT NULL,
  `admin_id` int(10) unsigned NOT NULL,
  `locale` enum('ru','en') NOT NULL DEFAULT 'ru' COMMENT 'News content locale for news i18n.',
  PRIMARY KEY (`id`),
  KEY `fk_user_news_idx` (`admin_id`),
  CONSTRAINT `fk_user_news` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,'2017-01-01','Мы открылись!','Добро пожаловать на JCasino, друг. Регистрируйся и выигрывай столько, сколько тебе нужно. Реализуй мечты с JCasino!',1,'ru'),(2,'2017-01-16','Бонусная система','JCasino обещает радовать своих игроков бонусами и подарками. Мы разрабатываем систему поощрения постоянных игроков и программу проведения лотерей с розыгрышем денежных и ценных призов. Оставайся с JCasino и выигрывай!',1,'ru'),(3,'2017-01-23','Работа техподдержки','Друзья, задавайте интересующие Вас вопросы и оставляйте Ваши замечания и предложения в новом разделе техподдержки на нашем сайте. Оценивайте работу администраторов и на основании Ваших оценок мы будем постоянно совершенствовать наши системы.',1,'ru'),(4,'2017-02-27','Благотворительная акция','JCasino предлагает Вам принять участие в благотворительной акции в поддержку благосостояния разработчиков казино. В течение марта 2017 года 1% от каждого сыгранного спина будет перечисляться на счет наших любимых разработчиков. Хвала халяве;)',1,'ru'),(5,'2017-03-03','Сделаем JCasino вновь великим','Игроки и Гости JCasino, распространяйте ссылку на наш сайт в соц. сетях и мы поднимем выигрышные коэфициенты. VIP-статус каждому репостонувшему!',1,'ru'),(6,'2017-03-11','Да будет звук!','Разработчики и саунд дизайнеры JCasino реализовали звуковую поддержку наших слотов. Теперь \"пробивать\" еще интереснее и азартнее. Оцени \"звон монет\" в обновленном интерфейсе игры! Ваш JCasino.',1,'ru'),(7,'2017-03-30','Рост коэффициентов','Новость ГОДА! Мы увеличили коэффициенты выигрышей в полтора раза! Накопи на квартиру либо погаси иппотеку вместе с JCasino!',1,'ru'),(8,'2017-04-07','Команда JCasino','Хочешь стать частью нашей команды разработчиков - присылай свое резюме на jcasino.slots@gmail.com и мы с тобой обязательно свяжемся. Продай ручку каждому HR\'у с JCasino!',1,'ru'),(9,'2017-04-22','Скоро в твоем городе','JCasino расширяется! Мы открывает наше первое оффлайн-казино в г. Минск (Беларусь). Такими темпами мы очень скоро окажемся и в твоем городе. Следи за новостями JCasino.',1,'ru'),(10,'2017-05-01','Больше игр - больше побед','JCasino публикует первую статистику игроков. По результатам анализа в долгосрочном периоде (от 6 месяцев) наблюдается стабильный выигрыш наших игроков - преумножение вложенных средств до 3-х раз. Не сдавайся на полпути, играй и ты выиграешь!',1,'ru'),(11,'2017-05-15','English News','Yeah! We added multilanguage support for our News. Since now we are going to seize the world. JCasino World Wide Team.',1,'en');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `id` int(11) unsigned NOT NULL COMMENT 'ID игрока.\n\nPlayer’s id.',
  `fname` varchar(70) DEFAULT NULL COMMENT 'Имя игрока (не обязательно, т. к. позже мы получим скан его паспорта и запросим дополнительно при верификации).\n\nPlayer’s first name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `mname` varchar(70) DEFAULT NULL COMMENT 'Отчество игрока (не обязательно, т. к. позже мы получим скан его паспорта).\n\nPlayer’s middle name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `lname` varchar(70) DEFAULT NULL COMMENT 'Фамилия игрока (не обязательно, т. к. позже мы получим скан его паспорта).\n\nPlayer’s last name (is not necessary to have it, because we will receive player’s passport scan in future).',
  `birthdate` date NOT NULL COMMENT 'Дата рождения игрока (обязательно, чтобы удостовериться что игроку есть 18).\n\nPlayer’s birthday date (is necessary to be sure that player is 18 years old).',
  `passport` varchar(30) DEFAULT NULL COMMENT 'Номер паспорта игрока (не обязательно, т. к. позже мы получим скан его паспорта и запросим дополнительно при верификации).\n\nPlayer’s passport number (is not necessary to have it, because we will receive player’s passport scan in future and will request it during verification).',
  `question` varchar(64) DEFAULT NULL COMMENT 'Секретный вопрос для восстановления забытого пароля.\n\nPlayer’s secret-question to recover forgotten password.',
  `answer_md5` varchar(32) DEFAULT NULL COMMENT 'Ответ на секретный вопрос для восстановления забытого пароля (ввода нового). Шифруется MD5.\n\nPlayer’s answer on secret-question to recover forgotten password (set new). Encrypts by MD5.',
  `balance` decimal(12,2) DEFAULT '0.00' COMMENT 'Количество денег.\n\nAmount of credits available to player.',
  `status` enum('basic','vip','ban','unactive') NOT NULL DEFAULT 'basic' COMMENT 'Статус игрока.\n\nPlayer’s status.',
  `admin_id` int(11) unsigned DEFAULT NULL COMMENT 'ID админа.\n\nAdmin’s id.',
  `commentary` varchar(700) DEFAULT NULL COMMENT 'Комментарии админа (прошлый статус, причина изменения статуса и т. п).\n\nAdmin comments.',
  PRIMARY KEY (`id`),
  KEY `fk_player_status_idx` (`status`),
  KEY `fk_player_admin_idx` (`admin_id`),
  CONSTRAINT `fk_player_admin` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_player_player_status` FOREIGN KEY (`status`) REFERENCES `player_status` (`status`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_player_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Информация об игроке.\n\nPlayer’s info.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (2,'ALIAKSANDER','ALBERTOVICH','SASNOUSKIKH','1991-09-24','KH1731245','Знак зодиака','567136873e70d017ddc1b66cbaeabd2c',9845.50,'basic',NULL,NULL),(3,'ANDREI','SERGEEVICH','DUSHKKOU','1992-08-11','KH1833442','Имя Сержанта?','u8bdbc505c23c2ce57b5c661de3ajfn',1200.00,'ban',1,'Так надо'),(4,'ALEKSA','ANATOLEVNA','PETROVA','1988-04-15','PB7573921',NULL,NULL,775.75,'vip',9,'Good player!!'),(5,'JOHN',NULL,'JOHNSON','1994-11-11','UM85646213',NULL,NULL,901.00,'ban',1,'Мошенник!'),(6,'PETER',NULL,'PETERSON','1980-01-02','LL2677644L1',NULL,NULL,1163.00,'unactive',1,'Не заходил уже год.'),(7,'VICTOR',NULL,'DELACROUGH','1983-05-27','STP293PS1',NULL,NULL,23.00,'basic',NULL,NULL),(8,'SAMUEL','EL','JACKSON','1993-12-30','KH8573651','Кто твоя папа?','d41d8cd3553f00b204e9800998ecf8ww',4.00,'basic',NULL,NULL),(10,'SAMANTHA',NULL,'ADAMS','1986-10-12','MHG33HGE1','My birthdate','d41d8c9f00b204e9800998ecf84m2s',0.00,'basic',NULL,NULL),(12,'ALIAKSANDER','ALBERTOVICH','SASNOUSKIKH','1991-09-24','KH1731245','Maya?','7fa3b767c460b54a2be4d49030b349c7',0.00,'basic',NULL,NULL),(13,'','','','1991-09-24','KH1731245','','d41d8cd98f00b204e9800998ecf8427e',0.00,'basic',1,NULL),(14,'MILA','VIKTOROVNA','MIKHANCHIK','1995-04-30','KH1731245','Где искать работу?','a704bc505c9c2ce57b5c66101de3a625',5329.50,'vip',1,'Играйте в JCasino и выигрывайте!'),(15,'','','','1991-09-12','MP3138042','','d41d8cd98f00b204e9800998ecf8427e',1234573.00,'basic',NULL,NULL);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_status`
--

DROP TABLE IF EXISTS `player_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_status` (
  `status` enum('basic','vip','ban','unactive') NOT NULL COMMENT 'Статусы:\n- ‘simple’ - присваивается при регистрации;\n- ‘vip’ - имеет более высокие лимиты и низкие проценты на займ;\n- ‘ban’ - не может играть на деньги и выводить деньги;\n- ‘inactive’ - если дата последнего визита более года назад, необходимо связаться с администратором для обновления данных.\n\nStatuses:\n- ‘simple’ - just simple status;\n- ‘vip’ - has higher bet-limit and lower loan percent, has no withdrawal limit;\n- ‘ban’ - can’t play on money and withdraw money;\n- ‘inactive’ - if last visit time was a year ago (needs to contact admins to update info).',
  `bet_limit` decimal(5,2) unsigned NOT NULL COMMENT 'Максимально возможная ставка на один спин.\n\nMax bet available for player for one spin.',
  `withdrawal_limit` decimal(9,2) unsigned NOT NULL COMMENT 'Ограничение на вывод средств в месяц.\n\nMonth’s withdrawal limit for player.',
  `loan_percent` decimal(4,2) unsigned NOT NULL COMMENT 'Процент по займу.\n\nLoan’s percent.',
  `max_loan_amount` decimal(9,2) unsigned NOT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Список статусов игрока.\n\nPlayer’s status list.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_status`
--

LOCK TABLES `player_status` WRITE;
/*!40000 ALTER TABLE `player_status` DISABLE KEYS */;
INSERT INTO `player_status` VALUES ('basic',5.00,300.00,20.00,25.00),('vip',50.00,10000.00,5.00,100.00),('ban',0.00,0.00,20.00,0.00),('unactive',0.00,0.00,20.00,0.00);
/*!40000 ALTER TABLE `player_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Порядковый номер вопроса.\n\nIndex number of question.',
  `player_id` int(11) unsigned DEFAULT NULL,
  `email` varchar(320) NOT NULL COMMENT 'ID игрока.\n\nPlayer’s id.',
  `topic` enum('transaction','loan','rules','verification','ban','other') NOT NULL COMMENT 'Тема вопроса.\n\nTopic of the question.',
  `question` varchar(700) NOT NULL COMMENT 'Текст вопроса.\n\nText of the question.',
  `q_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `admin_id` int(11) unsigned DEFAULT NULL COMMENT 'ID администратора.\n\nAdmin’s id.',
  `answer` varchar(700) DEFAULT NULL COMMENT 'Текст ответа администратора.\n\nText of admin’s answer.',
  `a_date` datetime DEFAULT NULL,
  `satisfaction` enum('best','good','norm','bad','worst') DEFAULT NULL COMMENT 'Уровень удовлетворенности игрока ответом администратора.\n\n*Может быть NULL - игрок не захотел оценивать ответ.\n\nThe degree of user’s satisfaction with the admin’s answer.\n\n*Can be NULL - player didn’t want to evaluate admin’s answer.',
  PRIMARY KEY (`id`),
  KEY `fk_question_player_idx` (`email`),
  KEY `fk_question_user_idx` (`admin_id`),
  KEY `topic_idx` (`topic`),
  KEY `satisfaction_idx` (`satisfaction`),
  KEY `fk_question_player_idx1` (`player_id`),
  CONSTRAINT `fk_question_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_question_user` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='Список вопросов игроков и ответов администраторов.\n\nList of users’ question and admins’ answers.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,7,'tramp@tramp.tr','other','Why are you so bad people?','2017-02-23 00:00:00',NULL,NULL,NULL,NULL),(2,2,'alien_by@tut.by','loan','Под какой процент я могу взять 500 долларов и на какой срок?','2017-02-25 18:14:40',1,'20% на 1 месяц!','2017-02-25 18:14:50',NULL),(3,4,'sasha88@yandex.by','other','Lorem ipsum?','2017-02-27 22:22:15',9,'Dolor sit amet.','2017-03-02 12:15:24','best'),(4,10,'raper@mc.net','rules','Где я могу почитать правила игры?','2017-03-02 17:21:40',1,'На странице «Правила игры»!','2017-03-03 10:54:07','norm'),(5,2,'alien_by@tut.by','ban','Can you unban me? Plz…','2017-03-17 09:10:18',1,'Yes, of course…','2017-03-17 09:15:22','best'),(6,7,'tramp@tramp.tr','verification','How can I call you, dear?)','2017-03-17 18:33:40',NULL,NULL,NULL,NULL),(7,NULL,'albert_einstein@tut.by','other','Как дела чуваки?','2017-03-21 18:10:00',NULL,NULL,NULL,NULL),(8,2,'alien_by@tut.by','rules','Как бы так?','2017-03-21 18:10:49',1,'Да вот так вот как-то.','2017-03-30 00:20:46','worst'),(9,2,'alien_by@tut.by','loan','What is loan? Baby don\'t hurt me...','2017-04-06 20:54:18',NULL,NULL,NULL,'norm'),(10,NULL,'alien_by@tut.by','transaction','How to pay?','2017-04-06 20:58:09',NULL,NULL,NULL,NULL),(11,2,'alien_by@tut.by','ban','Another one?','2017-04-06 21:00:30',NULL,NULL,NULL,NULL),(12,NULL,'anhelina.maretskaya@gmail.com','other','fghjklsdfghjkdfghjk sdfghjuygfcdfgh dfghjkiuyt uytrds.инвгнана','2017-04-13 14:39:56',NULL,NULL,NULL,NULL),(13,NULL,'jahstreetlove@gmail.com','ban','erhjewghkwhtkh45y6uyyуорпуопушпу','2017-04-13 14:45:33',NULL,NULL,NULL,NULL),(14,4,'sasha88@yandex.by','other','Loose yourself in a music...','2017-05-17 20:07:39',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `streak`
--

DROP TABLE IF EXISTS `streak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `streak` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Порядковый номер серии.\n\nIndex number of streak.',
  `player_id` int(11) unsigned NOT NULL COMMENT 'ID игрока.\n\nPlayer’s id.',
  `date` datetime NOT NULL COMMENT 'Время и дата генерации серии.\n\nDate and time when streak was generated.',
  `roll` varchar(255) NOT NULL COMMENT 'Случайно генерированная строка определенного формата, содержащая информацию о серии из 20 спинов. Возвращается игроку по окончании серии для реализации контроля честности.\n\nRandomly-generated string line in a special format which contains info about next 20 spins.',
  `offset` varchar(255) DEFAULT NULL COMMENT 'Строка определенного формата, содержащая информацию смещениях барабана слот-машины, сделанных игроком на протяжении серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами).\n\nString line in a special format which contains info about offsets made by player during the streak.',
  `lines` varchar(255) DEFAULT NULL,
  `bet` varchar(255) DEFAULT NULL COMMENT 'Строка определенного формата, которая содержит информацию о ставках, сделанных игроком на протяжении серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами).\n\nString line in a special format which contains info about bets made by player during the streak.',
  `result` varchar(255) DEFAULT NULL COMMENT 'Результат серии. Хранится для подсчета результатов и статистики (в кабинете пользователя игрок может просматривать информацию о проведенных им сериях, возможно с фильтрами). Хранится в отдельной ячейке для того, чтобы не пересчитывать его, извлекая данные из форматированных строк.\n\nStreak result.',
  PRIMARY KEY (`id`),
  KEY `fk_streak_player_idx` (`player_id`),
  KEY `date_idx` (`date`),
  KEY `result_idx` (`result`),
  CONSTRAINT `fk_streak_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='Список серий, сгенерированных слот-машиной.\n\nList of streaks generated by slot-machine.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `streak`
--

LOCK TABLES `streak` WRITE;
/*!40000 ALTER TABLE `streak` DISABLE KEYS */;
INSERT INTO `streak` VALUES (1,2,'2017-02-23 17:13:22','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','11111_11110_11100_11000_10000_00000_00001_00011_00111_01111','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9','4.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9'),(2,2,'2017-02-26 20:51:10','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','11111_11110_11100_11000_10000_00000_00001_00011_00111_01111','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9'),(3,2,'2017-02-26 23:10:22','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','11111_11110_11100_11000_10000_00000_00001_00011_00111_01111','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9','3.15_3.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9'),(4,2,'2017-02-27 15:15:15','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_42-15-12_3-4-5_12-52-23_12-3-42_12-4-1','11111_11110_11100_11000_10000_00000_00001_00011_00111_01111','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9','3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9'),(5,2,'2017-04-11 19:37:57','11-46-46_60-58-55_38-11-25_12-5-36_54-28-58_34-15-38_49-25-47_20-8-55_6-16-57_10-36-53','0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0','00000_00000_00000_00000_00000_00000_00000_00000_00000_00000','0.25_0_0_0_0_0_0_0_0_0','0_0_0_0_0_0_0_0_0_0'),(6,2,'2017-04-11 19:44:09','52-35-38_16-30-45_2-38-55_9-53-40_42-55-7_14-46-59_52-47-58_46-15-60_52-29-5_8-11-35','0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_2-59-2','11100_11100_11100_11100_11100_11100_00000_00000_00000_11111','0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25','0_0_0_0_0_0_0_0_0_0'),(7,14,'2017-04-11 21:17:09','27-11-28_54-56-57_4-36-41_39-11-60_28-10-54_45-33-13_20-51-20_41-8-36_2-43-60_15-12-5','5-2-59_5-2-45_5-2-36_5-3-30_4-7-30_8-6-30_11-8-34_11-8-34_10-8-34_10-8-34','00111_00111_01001_01001_11111_11111_11111_11111_11111_11111','0.5_0.5_0.5_4.75_4.5_4.5_5.0_5.0_4.5_4.5','0_0_3.0_0_54.0_54.0_0_30.0_0_54.0'),(8,14,'2017-04-11 21:21:51','49-47-54_20-35-8_37-20-31_33-21-21_4-20-6_49-51-30_18-38-14_56-22-42_7-27-27_39-5-60','8-10-32_8-10-32_8-10-32_8-10-32_8-10-32_8-10-32_8-10-32_10-12-30_10-12-30_12-14-28','11111_11111_11111_11111_11111_11111_11111_11111_11111_11111','4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5','0_54.0_54.0_54.0_27.0_0_27.0_27.0_54.0_0'),(9,14,'2017-04-11 21:23:26','42-18-34_21-49-29_37-3-45_37-13-54_33-29-14_13-59-7_35-17-1_55-51-15_17-41-55_17-38-1','12-14-28_12-14-28_12-14-28_12-14-28_12-14-28_12-14-28_12-14-28_10-14-30_10-14-30_10-14-30','11111_11111_11111_11111_11111_11111_11111_11111_11111_11111','4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5','27.0_54.0_54.0_0_0_54.0_54.0_108.0_54.0_54.0'),(10,14,'2017-04-11 21:25:00','31-10-20_48-40-13_52-32-36_15-21-44_45-18-58_10-30-13_25-34-26_58-59-10_36-39-42_14-50-6','10-14-30_10-14-30_10-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30','11111_11111_11111_11111_11111_11111_11111_11111_11111_11111','4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5','0_0_81.0_0_0_0_0_0_0_27.0'),(11,14,'2017-04-11 21:26:03','12-2-9_35-12-37_56-36-14_48-22-16_39-12-13_33-1-3_20-26-3_53-35-40_53-26-37_39-25-31','8-18-28_8-18-28_8-18-28_8-18-28_8-18-28_8-18-28_8-18-28_8-18-28_8-18-28_8-18-28','11111_11111_11111_11111_11111_11111_11111_11111_11111_11111','4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5','0_54.0_27.0_27.0_54.0_54.0_0_0_54.0_54.0'),(12,2,'2017-05-17 14:27:02','19-52-49_11-32-53_7-19-14_31-29-9_35-12-37_8-18-19_20-53-39_11-60-11_40-38-59_48-45-38','0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0_0-0-0-0-0','11111_00000_00000_00000_00000_00000_00000_00000_00000_00000','0.25_0_0_0_0_0_0_0_0_0','3.00_0_0_0_0_0_0_0_0_0'),(13,4,'2017-05-17 19:56:23','34-14-29_5-24-40_58-43-6_7-3-48_3-54-32_46-57-24_40-53-12_45-45-31_33-31-37_18-18-18','0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0-0-0','11111_11111_11111_11111_11111_11111_11111_11111_11111_00000','0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25_0','0_0_0_0_0_0_0_6.00_3.00_0'),(14,2,'2017-05-19 12:34:33','8-27-35_8-32-11_12-53-52_46-41-35_22-26-47_46-5-38_36-57-53_36-51-9_33-16-54_49-58-55','0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0_0-0-0-0-0_0-0-0-0-0','11111_11111_11111_11111_11111_11111_11111_11111_00000_00000','0.25_0.25_0.25_0.25_0.25_0.25_0.25_0.25_0_0','0_0_0_0_0_3.00_0_0_0_0');
/*!40000 ALTER TABLE `streak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Порядковый номер транзакции.\n\nIndex number of the transaction.',
  `player_id` int(11) unsigned NOT NULL COMMENT 'ID игрока.\n\nPlayer’s id.',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Дата и время транзакции.\n\nDate and time of the transaction.',
  `amount` decimal(9,2) NOT NULL COMMENT 'Количество средств.\nОграничение на транзакцию - [+/-] 9’999’999.99.\nНе может быть меньше определенной величины (проверяется на стороне клиент-серверной части).\n\nAmount of money.\nTransaction limit - [+/-] 9’999’999.99.\nCan’t be less than specified value (that value is not contained in database).',
  PRIMARY KEY (`id`),
  KEY `fk_transaction_player1_idx` (`player_id`),
  CONSTRAINT `fk_transaction_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='Список транзакиций игроков\n\nList of players’ transactions.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,2,'2017-01-20 11:11:11',10000.00),(2,4,'2017-01-21 07:04:32',300.00),(3,3,'2017-01-22 20:10:00',70.00),(4,3,'2017-01-23 21:11:03',20.00),(5,3,'2017-01-24 22:40:30',-35.00),(6,8,'2017-02-10 17:15:51',500.00),(7,5,'2017-02-11 18:30:30',300.00),(8,6,'2017-02-12 12:15:55',200.00),(9,7,'2017-02-13 13:42:22',100.00),(10,4,'2017-02-14 12:01:42',-15.00),(11,8,'2017-02-15 23:11:03',-10.00),(12,5,'2017-02-16 20:15:10',-100.00),(13,10,'2017-02-27 22:17:40',200.00),(14,5,'2017-03-02 11:39:41',-400.00),(15,2,'2017-03-18 13:22:14',75.00),(16,2,'2017-03-21 14:42:29',-250.00),(17,2,'2017-03-21 14:43:05',50.00),(18,2,'2017-03-21 14:46:02',-50.00),(19,2,'2017-03-22 10:59:44',9.00),(20,2,'2017-03-22 11:00:21',7.50),(21,2,'2017-03-26 13:56:13',200.00),(22,2,'2017-03-26 13:56:26',200.00),(23,2,'2017-04-06 19:31:13',20.00),(24,14,'2017-04-11 21:17:47',5000.00),(25,15,'2017-04-13 14:56:05',1234567.00);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID пользователя.\n\nUser’s id.',
  `password_md5` varchar(32) NOT NULL COMMENT 'Пароль, зашифрованный MD5.\n\nPassword encrypted by MD5.\n\nAvailable signs: [a-z] [A-Z] [0-9] + - = ! ? ( )\nMust have at least:\n- 1 uppercase letter;\n- 1 lowercase letter;\n- 1 digit;\n- min 8 symbols.\n\nWhy? - We want it that way!',
  `email` varchar(320) NOT NULL COMMENT 'E-mail пользователя, используемый как login для входа в систему.\n\nUser’s email which is used as login too to sing-in system.\n\nPS: max amount of signs in e-mail in different «Simple MAIL Transfer Protocols»:\n64 + @ + 64 = 129\nor\n64 + @ + 255 = 320 - use this because it’s bigger.',
  `role` enum('player','admin') NOT NULL DEFAULT 'player' COMMENT 'Игрок или администратор?\n\nPlayer or admin?',
  `registration_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Для того, чтобы следить как долго пользователь зарегистрирован в системе (может применятся в будущем для разработки системы персональных бонусов).\n\nTo monitor how long user is registered in days. Fills automatically after registration.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_email_UNIQUE` (`email`),
  KEY `role_idx` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='Пользователи, зарегистрированные в системе.\n\nUsers registered in system.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'5d3c47fddf8cbac13c1687c612f331d7','jahstreetlove@gmail.com','admin','2017-01-13 12:28:00'),(2,'fa72bfd38dcac08d86e01262a6feef46','alien_by@tut.by','player','2017-01-14 01:02:03'),(3,'1278bb137a3052eff24650b6362aa802','andrew.burn1ng@gmail.com','player','2017-01-15 14:33:16'),(4,'7c916c4d2e84b4b8355ba93cbebe8d57','sasha88@yandex.by','player','2017-01-23 17:20:44'),(5,'a4ede711348b654fba68ba51ab25548d','igarrugar@tut.by','player','2017-01-29 22:22:22'),(6,'9dca9a8e052e1828faad779b8ac2218a','kybercriminal@mymail.xyz','player','2017-02-10 21:30:00'),(7,'731e0bd8d70f036b112e389661b7f34b','tramp@tramp.tr','player','2017-02-17 16:10:07'),(8,'f34072580b9ae7eb90ce859e1654fe82','soldat@army.net','player','2017-02-23 11:11:11'),(9,'9c63485db2cd7ca752178a2c841a847e','casino_hack@lol.by','admin','2017-02-24 12:15:03'),(10,'86e98be9d818b799d7fb7ddd2e4e6b44','raper@mc.net','player','2017-02-25 17:33:22'),(11,'a2d07690e91c09891052ab5ec3dc7a33','zen-tor@aliens.com','admin','2017-01-16 17:54:36'),(12,'0fce7d26cf0ce2f6013cf525a4981882','ivanov.ivan@gmail.com','player','2017-03-16 11:48:29'),(13,'5d3c47fddf8cbac13c1687c612f331d7','super8@tut.by','player','2017-04-04 10:18:56'),(14,'5d3c47fddf8cbac13c1687c612f331d7','mikhanchik.95@mail.ru','player','2017-04-11 21:16:19'),(15,'c070202aef2d05077de4a4d16bf26875','anhelina.maretskaya@gmail.com','player','2017-04-13 14:52:06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification`
--

DROP TABLE IF EXISTS `verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification` (
  `id` int(11) unsigned NOT NULL COMMENT 'ID игрока.\n\nPlayer’s id.',
  `status` bit(3) NOT NULL DEFAULT b'0' COMMENT 'Bit-mask with verification information about player: [abc]\nc - all necessary fields in table «player» are filled;\nb - e-mail is verified;\na - passport scan is verified.\n\nPlayer is verified when [abc] = [111].\n\nThe process of verification:\nFirst - player verifies his e-mail and fills all necessary fields in table «player».\nNext - admin gets the information about player (when player’s verification_status bit-mask becomes 011) and checks his passport data (data in table «player» including passport-scan).\nIf it’s ok (a = 1) - player can withdraw money.\n\n*Player can do payments and play without verification.',
  `admin_id` int(11) unsigned DEFAULT NULL COMMENT 'Администратор, который верифицировал игрока.\n\nAdmin who verified player.',
  `commentary` varchar(700) DEFAULT NULL,
  `date` datetime DEFAULT NULL COMMENT 'Дата верификации игрока.\n\nDate player was verified by specified admin (admin_id).',
  `passport` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `passport_UNIQUE` (`passport`),
  KEY `fk_verification_admin_idx` (`admin_id`),
  KEY `status_idx` (`status`),
  CONSTRAINT `fk_verification_player` FOREIGN KEY (`id`) REFERENCES `player` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_verification_user_admin` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица состояния верификации игроков. Мы создали таблицу отдельно, т. к. планируем обрабатывать ее данные отдельно.\n\n\nPlayers’ verification state table.\nWe have defined this table apart from basic table «user» because we plan to process it’s data apart of basic table «user».';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification`
--

LOCK TABLES `verification` WRITE;
/*!40000 ALTER TABLE `verification` DISABLE KEYS */;
INSERT INTO `verification` VALUES (2,'',NULL,NULL,NULL,NULL),(3,'',NULL,NULL,NULL,'scan/3/1.jpg'),(4,'',NULL,NULL,NULL,'scan/4/1.jpg'),(5,'',NULL,NULL,NULL,NULL),(6,'',NULL,NULL,NULL,NULL),(7,'',NULL,NULL,NULL,NULL),(8,'',NULL,NULL,NULL,NULL),(10,'',NULL,NULL,NULL,NULL),(12,'\0',NULL,NULL,NULL,NULL),(13,'\0',NULL,NULL,NULL,NULL),(14,'',NULL,NULL,NULL,NULL),(15,'\0',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'jcasino'
--
/*!50003 DROP PROCEDURE IF EXISTS `drop_schema` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `drop_schema`()
BEGIN
	DROP SCHEMA IF EXISTS jcasino;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `off_fk_check` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `off_fk_check`()
BEGIN
	SET FOREIGN_KEY_CHECKS=0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_all_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_all_autoincrement`()
BEGIN
	CALL reset_loan_autoincrement();
    CALL reset_news_autoincrement();
    CALL reset_question_autoincrement();
    CALL reset_streak_autoincrement();
    CALL reset_transaction_autoincrement();
    CALL reset_user_autoincrement();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_loan_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_loan_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM loan;
    SET @query = CONCAT("ALTER TABLE loan AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_news_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_news_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM news;
    SET @query = CONCAT("ALTER TABLE news AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_question_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_question_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM question;
    SET @query = CONCAT("ALTER TABLE question AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_streak_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_streak_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM streak;
    SET @query = CONCAT("ALTER TABLE streak AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_transaction_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_transaction_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM transaction;
    SET @query = CONCAT("ALTER TABLE transaction AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `reset_user_autoincrement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_user_autoincrement`()
BEGIN
	SELECT @maxId := IFNULL(MAX(ID), 0) + 1 FROM user;
    SET @query = CONCAT("ALTER TABLE user AUTO_INCREMENT=", @maxId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `set_fk_check` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `set_fk_check`()
BEGIN
	SET FOREIGN_KEY_CHECKS=1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `truncate_all` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `truncate_all`()
BEGIN
	SET FOREIGN_KEY_CHECKS=0;
	TRUNCATE TABLE jcasino.loan;
	TRUNCATE TABLE jcasino.news;
	TRUNCATE TABLE jcasino.player;
	TRUNCATE TABLE jcasino.player_status;
	TRUNCATE TABLE jcasino.question;
	TRUNCATE TABLE jcasino.streak;
	TRUNCATE TABLE jcasino.transaction;
	TRUNCATE TABLE jcasino.user;
	TRUNCATE TABLE jcasino.verification;
	SET FOREIGN_KEY_CHECKS=1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-30 14:10:27

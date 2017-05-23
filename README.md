# JCasino Slots
EPAM Java WEB Development Training final-task project "Slot Casino".

Used technologies:

  - Front-end: HTML5, CSS3, JavaScript (jQuery, AJAX, some libraries), Canvas animation (for game), responsive design without front-end frameworks;
  - Back-end: Java 8, Servlet, WebFilter, WebListener, JSP, JSTL, Java Mail;
  - Database: JDBC, MySQL (with some stored procedures), self-written connection-pool;
  - Tests: JUnit 4, DBUnit, Mockito, PowerMockito, IntelliJ IDEA code coverage tool;
  - Other: Tomcat, Maven, Log4j2, local and remote deployment support (used Jelastic platform), some fail-safe and scalability features.
    
Application architecture:

  - Client <-> Server ( [WebFilter, WebListener] [Controller <-> Command <-> Service <-> { DAO] ) <-> MySQL RDMS }
  
Used design patterns:

  - Singleton;
  - Builder/Factory;
  - Lazy initialization;
  - Wrapper;
  - Chain of responsibility;
  - POST-REDIRECT-GET;
  - Model-View-Controller;
  - GRASP patterns;
  - ACID transaction system;
  - partial REST features;
  - other...
  
Application features:

  - Player:
    - Registration, login/logout;
    - 3-step player identity verification (verification of profile data (auto), e-mail address (by entering code, sent to
      e-mail), uploaded passport scan image (by Admin));
    - Technical support(Player asks questions to Admin and rates answers, Player takes answer to his control panel and 
      e-mail);
    - Edit Player profile, change and recover password;
    - View player stats data;
    - View player account data, replenishing and withdrawing money (stub), taking loan;
    - View player operation history with some filters;
    - All the Slot-Machine games functional.
    
  - Admin:
    - Login/logout;
    - Add/edit/delete news;
    - Verify Player with comments;
    - View technical support with some filters, answering Player questions;
    - View transactions data with some filters;
    - View streaks data with some filters;
    - View loans data with some filters;
    - View any Player data;
    - Change player status with comments.
    
  - JCasino Fruits Game (3-reel slot-machine with five paylines and MD5 Fairness Check FairPlay):
    - View and choose playing lines;
    - Choose bet amount for 1 line, total bet counts as [1 line bet] * [number of playing lines];
    - Choose offset for each reel on every spin;
    - Finish streak (10 rolls) prematurely to see decoded streak-roll-string;
    - Turn on/off music, sound design of game;
    - "Demo" and "For real money" play modes.
    
  - Other:
    - Multi-language support (Ru/En);
    - News feed;
    - JavaScript pagination support;
    - Partial support for mobile devices with small screens.
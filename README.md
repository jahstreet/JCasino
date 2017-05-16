# JCasino Slots
EPAM Java WEB Development Training final-task project "Slot Casino".

Used technologies:
  * - Front-end: HTML5, CSS3, JavaScript (jQuery, AJAX, some libraries), Canvas animation (for game), responsive design
*     without front-end frameworks;
*   - Back-end: Java 8, Servlet, WebFilter, WebListener, JSP, JSTL, Java Mail;
*   - Database: JDBC, MySQL (with some stored procedures), self-written connection-pool;
*   - Tests: JUnit 4, DBUnit, Mockito, PowerMockito;
*   - Other: Tomcat, Maven, local and remote deployment support (used Jelastic platform), some fail-safe and scalability
*     features.
    
Application architecture:
  - Client <-> Server ( [WebFilter, WebListener] [Controller <-> Command <-> Service <-> {DAO]) <-> MySQL RDMS}
  
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
  - other...
  
Application functional:

  - Player:
    - Registration, login/logout;
    - 3-step player identity verification (verification of profile data (auto), e-mail address (by entering code, sent to
      e-mail), uploaded passport scan image (by Admin));
    - Technical support(Player asks questions to Admin and rates answers, Player takes answer to his control panel and 
      e-mail);
    - Player profile editing, changing and recovering password;
    - Viewing player stats data;
    - Viewing player account data, replenishing and withdrawing money (stub), taking loan;
    - Viewing player operation history with some filters;
    - All the Slot-Machine games functional.
    
  - Admin:
    - Login/logout;
    - Adding/editting/deleting news;
    - Player verification with comments;
    - Viewing technical support with some filters, answering Player questions;
    - Viewing transactions data with some filters;
    - Viewing streaks data with some filters;
    - Viewing loans data with some filters;
    - Viewing any Player data;
    - Changing player status with comments.
    
  - JCasino Fruits Game (3-reel slot-machine with five paylines and MD5 Fairness Check FairPlay):
    - Viewing and choosing playing lines;
    - Choosing bet amount for 1 line, total bet counts as [1 line bet] * [number of playing lines];
    - Choosing offset for each reel on every spin;
    - Finish streak (10 rolls) prematurely to see decoded streak-roll-string;
    - Turning on/off music, sound design of game;
    - "Demo" and "For real money" play modes.
    
  - Other:
    - Multilanguage support (Ru/En);
    - News feed;
    - JavaScript pagination support;
    - Partial support for mobile devices with small screens.
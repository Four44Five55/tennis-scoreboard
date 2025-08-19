# Tennis Scoreboard

Веб-приложение для ведения счёта теннисных матчей.

## Функциональность
- Создание нового матча
- Просмотр законченных матчей, поиск матчей по именам игроков
- Подсчёт очков в текущем матче
  
## Технологии
- Java 21
- Servlets API
- JSP
- Tomcat 11
- Gradle

## Запуск
1. Клонировать репозиторий
2. `./gradlew build`
3. Деплой WAR в Tomcat или запуск через IDE
4. Открыть http://localhost:8080/tennis-scoreboard

## Структура проекта
src/main/java/
  controller/ # Сервлеты
  service/ # Бизнес-логика (подсчёт очков)
  dao/ # Работа с БД
  model/ # Match, Player, Score
## API endpoints

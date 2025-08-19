# Tennis Scoreboard

Веб-приложение для ведения счёта теннисных матчей.

## Функциональность
- Создание нового матча с указанием игроков
- Подсчёт очков по теннисным правилам (15-30-40-game)
- Отображение текущего счёта
- История завершённых матчей

## Технологии
- Java 11+
- Servlets API
- JSP
- Tomcat 9+
- Gradle/Maven
- H2 Database (in-memory)

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

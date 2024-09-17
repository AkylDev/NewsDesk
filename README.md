# Auction Sales Board Service(Newsdesk)

Приложение для онлайн-аукционов, где пользователи могут размещать объявления, делать ставки и загружать изображения товаров с помощью MinIO.

## Технологии:
- **Java 17**
- **Spring Boot**
- **Gradle**
- **PostgreSQL**
- **MinIO** для загрузки изображений
- **Swagger/OpenAPI** для документации

## Запуск приложения:

1. Склонируйте репозиторий:
   ```bash
   git clone <repository-url>
   ```
2. Соберите проект:
```bash
./gradlew build
```

3. Запустите с помощью Docker Compose:
```bash
docker-compose up
```

Приложение будет доступно по адресу: `http://localhost:8088`

## Swagger UI:
Документация API доступна по адресу:
`http://localhost:8088/swagger-ui.html`




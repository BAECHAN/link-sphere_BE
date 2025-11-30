# LinkSphere Backend - Setup Guide

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- Gradle (included via wrapper)

## Database Setup

1. Create a PostgreSQL database:

```sql
CREATE DATABASE linksphere;
```

2. Copy the example configuration:

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

3. Update `application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/linksphere
    username: your_username
    password: your_password
```

## Running the Application

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Posts

- `POST /api/posts` - Create a new post
- `GET /api/posts` - Get posts list (supports search, tag filter, sorting, pagination)
- `GET /api/posts/{id}` - Get post details
- `PATCH /api/posts/{id}` - Update a post
- `DELETE /api/posts/{id}` - Delete a post
- `POST /api/posts/{id}/bookmark` - Toggle bookmark

### Comments

- `POST /api/comments` - Create a comment
- `PATCH /api/comments/{id}` - Update a comment
- `DELETE /api/comments/{id}` - Delete a comment

### Reactions

- `POST /api/reactions` - Toggle like on a post

## Testing with curl

### Create a post:

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://example.com",
    "title": "Test Post",
    "description": "Test Description",
    "content": "Test Content",
    "tags": ["kotlin", "spring"]
  }'
```

### Get posts:

```bash
curl http://localhost:8080/api/posts
```

### Search posts:

```bash
curl "http://localhost:8080/api/posts?search=kotlin&sort=viewCount&page=0&size=10"
```

## Authentication

Currently, authentication is disabled for development. All endpoints use a temporary user ID (`temp-user-id`).

When implementing authentication:

1. Uncomment `@AuthenticationPrincipal` parameters in controllers
2. Remove temporary `userId` assignments
3. Update `SecurityConfig.kt` to enable authentication
4. Implement JWT or OAuth2 authentication as needed

All TODO comments in the code mark where authentication should be integrated.

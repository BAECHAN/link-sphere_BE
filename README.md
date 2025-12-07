# LinkSphere 백엔드

> 링크 공유 및 관리를 위한 소셜 플랫폼 백엔드 API

## 📋 프로젝트 개요

LinkSphere는 사용자가 웹 링크를 공유하고, 태그로 분류하며, 다른 사용자와 소통할 수 있는 소셜 플랫폼입니다. 이 저장소는 Spring Boot와 Kotlin으로 작성된 RESTful API 백엔드입니다.

## 🛠 기술 스택

- **언어**: Kotlin 1.9.25
- **프레임워크**: Spring Boot 3.5.8
- **데이터베이스**: PostgreSQL
- **ORM**: Spring Data JPA
- **보안**: Spring Security, OAuth2
- **API 문서**: Swagger/OpenAPI 3.0
- **테스트**: Kotest, MockK, H2 (인메모리 DB)
- **빌드 도구**: Gradle 8.14.3

## 📁 프로젝트 구조

```
src/main/kotlin/com/example/linksphere_be/
├── config/              # 설정 클래스 (Security, Swagger)
├── controller/          # REST API 컨트롤러 및 인터페이스
│   ├── *Api.kt         # Swagger 문서가 포함된 API 인터페이스
│   └── *Controller.kt  # 비즈니스 로직을 호출하는 구현체
├── dto/                # 요청/응답 데이터 전송 객체
├── entity/             # JPA 엔티티 (도메인 모델)
├── exception/          # 전역 예외 처리
├── repository/         # Spring Data JPA 리포지토리
└── service/            # 비즈니스 로직 레이어

src/test/kotlin/
├── api/                # API 통합 테스트
└── util/               # 테스트 유틸리티
```

### 주요 설계 패턴

- **레이어드 아키텍처**: Controller → Service → Repository
- **인터페이스 분리**: Swagger 문서를 API 인터페이스로 분리하여 컨트롤러 코드 간소화
- **DTO 패턴**: 엔티티와 API 계층 분리
- **통일된 에러 응답**: `ErrorResponse` DTO를 통한 일관된 에러 처리

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- PostgreSQL 데이터베이스
- Gradle (wrapper 포함)

### 데이터베이스 설정

1. PostgreSQL 데이터베이스 생성:

```sql
CREATE DATABASE linksphere;
```

2. 설정 파일 복사:

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

3. `application.yml`에 데이터베이스 정보 입력:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/linksphere
    username: your_username
    password: your_password
```

### 애플리케이션 실행

```bash
./gradlew bootRun
```

API는 `http://localhost:8080`에서 접근 가능합니다.

### Swagger UI 접근

```
http://localhost:8080/swagger-ui/index.html
```

## 📚 API 엔드포인트

### 게시물 (Posts)

| Method | Endpoint                   | 설명                                             |
| ------ | -------------------------- | ------------------------------------------------ |
| POST   | `/api/posts`               | 게시물 생성                                      |
| GET    | `/api/posts`               | 게시물 목록 조회 (검색, 태그 필터, 정렬, 페이징) |
| GET    | `/api/posts/{id}`          | 게시물 상세 조회                                 |
| PATCH  | `/api/posts/{id}`          | 게시물 수정                                      |
| DELETE | `/api/posts/{id}`          | 게시물 삭제                                      |
| POST   | `/api/posts/{id}/bookmark` | 북마크 토글                                      |

### 댓글 (Comments)

| Method | Endpoint                        | 설명               |
| ------ | ------------------------------- | ------------------ |
| POST   | `/api/comments`                 | 댓글 생성          |
| GET    | `/api/comments?postId={postId}` | 게시물의 댓글 조회 |
| PATCH  | `/api/comments/{id}`            | 댓글 수정          |
| DELETE | `/api/comments/{id}`            | 댓글 삭제          |

### 반응 (Reactions)

| Method | Endpoint         | 설명        |
| ------ | ---------------- | ----------- |
| POST   | `/api/reactions` | 좋아요 토글 |

## 🧪 테스트

### 전체 테스트 실행

```bash
./gradlew test
```

### 테스트 구조

- **통합 테스트**: `PostApiTest`, `CommentApiTest`, `ReactionApiTest`
- **테스트 환경**: H2 인메모리 데이터베이스
- **테스트 프레임워크**: Kotest + Spring MockMvc

## 🔐 인증

현재 개발 단계에서는 인증이 비활성화되어 있으며, 모든 엔드포인트는 임시 사용자 ID(`temp-user-id`)를 사용합니다.

### 인증 구현 시 TODO

1. 컨트롤러의 `@AuthenticationPrincipal` 파라미터 주석 해제
2. 임시 `userId` 할당 제거
3. `SecurityConfig.kt`에서 인증 활성화
4. JWT 또는 OAuth2 인증 구현

코드 내 모든 TODO 주석이 인증 통합 지점을 표시합니다.

## 📖 추가 문서

- [아키텍처 가이드](docs/architecture.md) - 상세 아키텍처 설명
- [API 가이드](docs/api-guide.md) - API 상세 문서 및 예제
- [개발 가이드](docs/development-guide.md) - 개발 환경 설정 및 컨벤션
- [데이터베이스 스키마](docs/DBML.dbml) - ERD 및 테이블 구조
- [Postman Collection](docs/LinkSphere_API.postman_collection.json) - API 테스트용

## 🤝 기여

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 라이선스

This project is licensed under the MIT License.

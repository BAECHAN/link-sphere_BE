# LinkSphere 개발 가이드

## 개발 환경 설정

### 필수 요구사항

- **Java**: JDK 17 이상
- **Kotlin**: 1.9.25 (프로젝트에 포함)
- **PostgreSQL**: 최신 버전
- **IDE**: IntelliJ IDEA 권장 (Kotlin 지원)

### 프로젝트 클론 및 설정

```bash
# 1. 저장소 클론
git clone https://github.com/your-org/link-sphere_BE.git
cd link-sphere_BE

# 2. 데이터베이스 생성
psql -U postgres
CREATE DATABASE linksphere;
\q

# 3. 설정 파일 복사
cp src/main/resources/application.yml.example src/main/resources/application.yml

# 4. application.yml 수정 (데이터베이스 정보 입력)
vim src/main/resources/application.yml
```

### IntelliJ IDEA 설정

1. **프로젝트 열기**: `File > Open` → `build.gradle.kts` 선택
2. **JDK 설정**: `File > Project Structure > Project SDK` → Java 17 선택
3. **Kotlin 플러그인**: 자동으로 설치됨 (최신 버전 확인)
4. **Gradle 동기화**: 우측 Gradle 탭에서 "Reload All Gradle Projects" 클릭

---

## 빌드 및 실행

### Gradle 명령어

```bash
# 빌드 (테스트 포함)
./gradlew build

# 빌드 (테스트 제외)
./gradlew build -x test

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test

# 클린 빌드
./gradlew clean build
```

### IDE에서 실행

1. `LinkSphereBeApplication.kt` 파일 열기
2. `main` 함수 옆 실행 버튼 클릭
3. 또는 `Run > Run 'LinkSphereBeApplication'`

---

## 테스트

### 테스트 구조

```
src/test/kotlin/
├── api/                    # API 통합 테스트
│   ├── PostApiTest.kt
│   ├── CommentApiTest.kt
│   └── ReactionApiTest.kt
└── util/                   # 테스트 유틸리티
    └── TestDataLoader.kt
```

### 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스
./gradlew test --tests PostApiTest

# 특정 테스트 메서드
./gradlew test --tests "PostApiTest.should create post successfully"
```

### 테스트 작성 가이드

**Kotest 스타일**:

```kotlin
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostApiTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val testDataLoader: TestDataLoader
) : FunSpec({

    beforeEach {
        testDataLoader.loadTestData()
    }

    test("should create post successfully") {
        val request = CreatePostRequest(
            url = "https://example.com",
            title = "Test Post"
        )

        mockMvc.post("/api/posts") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.url") { value("https://example.com") }
        }
    }
})
```

**주요 어노테이션**:

- `@SpringBootTest`: 전체 Spring Context 로드
- `@AutoConfigureMockMvc`: MockMvc 자동 설정
- `@ActiveProfiles("test")`: 테스트 프로파일 활성화
- `@DirtiesContext`: 테스트 후 Context 초기화

---

## 코드 컨벤션

### Kotlin 스타일 가이드

프로젝트는 [Kotlin 공식 스타일 가이드](https://kotlinlang.org/docs/coding-conventions.html)를 따릅니다.

**주요 규칙**:

1. **들여쓰기**: 4 spaces
2. **클래스명**: PascalCase (`PostService`)
3. **함수/변수명**: camelCase (`createPost`)
4. **상수**: UPPER_SNAKE_CASE (`MAX_PAGE_SIZE`)
5. **파일명**: 클래스명과 동일 (`PostService.kt`)

### 파일 구조

```kotlin
package com.example.linksphere_be.service

import ...

/**
 * 게시물 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
class PostService(
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository
) {

    @Transactional
    fun createPost(request: CreatePostRequest, userId: String): PostResponse {
        // 구현
    }

    // 다른 메서드들...
}
```

### 네이밍 컨벤션

| 타입           | 규칙                               | 예시                |
| -------------- | ---------------------------------- | ------------------- |
| Controller     | `*Controller`                      | `PostController`    |
| Service        | `*Service`                         | `PostService`       |
| Repository     | `*Repository`                      | `PostRepository`    |
| Entity         | 도메인 이름                        | `Post`, `Comment`   |
| DTO (Request)  | `Create*Request`, `Update*Request` | `CreatePostRequest` |
| DTO (Response) | `*Response`                        | `PostResponse`      |
| API Interface  | `*Api`                             | `PostApi`           |

---

## Git 워크플로우

### 브랜치 전략

```
main          # 프로덕션 브랜치
└── develop   # 개발 브랜치
    ├── feature/post-api      # 기능 브랜치
    ├── feature/comment-api
    └── fix/post-validation   # 버그 수정 브랜치
```

### 커밋 메시지 규칙

**형식**:

```
<type>: <subject>

<body> (선택)
```

**타입**:

- `feat`: 새로운 기능
- `fix`: 버그 수정
- `refactor`: 리팩토링
- `docs`: 문서 수정
- `test`: 테스트 추가/수정
- `chore`: 빌드 설정 등

**예시**:

```bash
git commit -m "feat: Add post search functionality"
git commit -m "fix: Resolve null pointer exception in PostService"
git commit -m "refactor: Extract Swagger annotations to API interfaces"
```

### Pull Request 가이드

1. **브랜치 생성**: `feature/your-feature-name`
2. **작업 완료 후 Push**
3. **PR 생성**: `develop` 브랜치로
4. **PR 제목**: 커밋 메시지 규칙 준수
5. **설명**: 변경 사항 및 테스트 결과 기술
6. **리뷰 요청**: 최소 1명 이상

---

## 데이터베이스 마이그레이션

### JPA 스키마 자동 생성 (개발 환경)

`application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update # 개발: update, 프로덕션: validate
```

**옵션**:

- `create`: 기존 테이블 삭제 후 재생성
- `create-drop`: 종료 시 테이블 삭제
- `update`: 변경사항만 반영 (권장)
- `validate`: 스키마 검증만 수행
- `none`: 아무것도 하지 않음

### 수동 DDL 실행

프로덕션 환경에서는 `docs/DDL.sql` 파일을 사용하여 수동으로 스키마를 생성합니다:

```bash
psql -U postgres -d linksphere -f docs/DDL.sql
```

---

## 환경별 설정

### 프로파일 구조

```
src/main/resources/
├── application.yml           # 공통 설정
├── application-dev.yml       # 개발 환경
├── application-prod.yml      # 프로덕션 환경
└── application-test.yml      # 테스트 환경
```

### 프로파일 활성화

```bash
# 개발 환경
./gradlew bootRun --args='--spring.profiles.active=dev'

# 프로덕션 환경
java -jar build/libs/link-sphere_BE-0.0.1-SNAPSHOT.war --spring.profiles.active=prod
```

---

## 디버깅

### IntelliJ IDEA 디버거

1. 중단점 설정: 코드 라인 번호 옆 클릭
2. Debug 모드 실행: `Run > Debug 'LinkSphereBeApplication'`
3. 변수 확인: Variables 탭
4. 표현식 평가: Evaluate Expression (Alt+F8)

### 로그 레벨 설정

`application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.example.linksphere_be: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

---

## 성능 최적화

### N+1 문제 해결

**문제**:

```kotlin
// N+1 쿼리 발생
val posts = postRepository.findAll()
posts.forEach { post ->
    println(post.author.nickname)  // 각 post마다 쿼리 실행
}
```

**해결**:

```kotlin
// Fetch Join 사용
@Query("SELECT p FROM Post p JOIN FETCH p.author")
fun findAllWithAuthor(): List<Post>
```

### 페이지네이션

```kotlin
val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
val posts = postRepository.findAll(pageable)
```

---

## 보안 체크리스트

### 개발 시 주의사항

- [ ] 민감 정보를 코드에 하드코딩하지 않기
- [ ] `application.yml`을 `.gitignore`에 추가
- [ ] SQL Injection 방지 (JPA 사용 시 자동 방지)
- [ ] XSS 방지 (입력 검증)
- [ ] CORS 설정 확인

### 프로덕션 배포 전

- [ ] 인증/권한 구현
- [ ] HTTPS 적용
- [ ] Rate Limiting 설정
- [ ] 에러 메시지에서 민감 정보 제거
- [ ] 로그에서 개인정보 마스킹

---

## 트러블슈팅

### 자주 발생하는 문제

**1. 포트 충돌 (8080)**

```bash
# 포트 사용 중인 프로세스 확인
lsof -i :8080

# 프로세스 종료
kill -9 <PID>
```

**2. 데이터베이스 연결 실패**

- PostgreSQL 서비스 실행 확인: `brew services list`
- 데이터베이스 존재 확인: `psql -U postgres -l`
- `application.yml`의 연결 정보 확인

**3. Gradle 빌드 실패**

```bash
# Gradle 캐시 삭제
./gradlew clean

# Gradle Wrapper 재설치
./gradlew wrapper --gradle-version 8.14.3
```

---

## 유용한 도구

### 추천 IntelliJ 플러그인

- **Kotlin**: 기본 제공
- **Database Navigator**: 데이터베이스 관리
- **Rainbow Brackets**: 괄호 색상 구분
- **GitToolBox**: Git 기능 강화

### 외부 도구

- **Postman**: API 테스트
- **DBeaver**: 데이터베이스 GUI
- **HTTPie**: 커맨드라인 HTTP 클라이언트

---

## 참고 자료

- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Data JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)
- [Kotest 문서](https://kotest.io/)

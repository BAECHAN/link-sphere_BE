# LinkSphere 백엔드 아키텍처

## 개요

LinkSphere 백엔드는 **레이어드 아키텍처(Layered Architecture)** 패턴을 기반으로 설계되었으며, 각 레이어는 명확한 책임을 가지고 있습니다.

## 아키텍처 다이어그램

```
┌─────────────────────────────────────────┐
│         Client (Frontend/API)           │
└──────────────────┬──────────────────────┘
                   │ HTTP Request
┌──────────────────▼──────────────────────┐
│      Controller Layer (API 인터페이스)   │
│  - CommentApi, PostApi, ReactionApi     │
│  - Swagger 문서 포함                     │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│    Controller Implementation Layer      │
│  - CommentController, PostController    │
│  - 요청 검증 및 Service 호출             │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Service Layer (비즈니스 로직)    │
│  - PostService, CommentService          │
│  - 트랜잭션 관리                         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      Repository Layer (데이터 접근)      │
│  - Spring Data JPA 리포지토리            │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Database (PostgreSQL)           │
└─────────────────────────────────────────┘
```

## 레이어별 상세 설명

### 1. Controller Layer (API 인터페이스)

**역할**: API 명세 및 Swagger 문서 정의

**파일**: `*Api.kt` (예: `PostApi.kt`, `CommentApi.kt`)

**특징**:

- `@Operation`, `@ApiResponses` 등 Swagger 어노테이션 포함
- HTTP 메서드 매핑 (`@GetMapping`, `@PostMapping` 등)
- 요청/응답 타입 정의

**예시**:

```kotlin
@Tag(name = "Posts", description = "Post Management API")
interface PostApi {
    @Operation(summary = "Create a new post")
    @ApiResponses(...)
    @PostMapping
    fun createPost(@RequestBody request: CreatePostRequest): ResponseEntity<PostResponse>
}
```

### 2. Controller Implementation Layer

**역할**: API 인터페이스 구현 및 Service 호출

**파일**: `*Controller.kt` (예: `PostController.kt`)

**특징**:

- API 인터페이스를 구현 (`implements PostApi`)
- 요청 검증
- Service 레이어 호출
- 응답 생성 (HTTP 상태 코드 설정)

**예시**:

```kotlin
@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) : PostApi {
    override fun createPost(request: CreatePostRequest): ResponseEntity<PostResponse> {
        val post = postService.createPost(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(post)
    }
}
```

### 3. Service Layer

**역할**: 비즈니스 로직 처리 및 트랜잭션 관리

**파일**: `*Service.kt` (예: `PostService.kt`, `CommentService.kt`)

**특징**:

- `@Service` 어노테이션
- `@Transactional` 트랜잭션 관리
- 복잡한 비즈니스 규칙 구현
- 여러 Repository 조합 사용

**주요 책임**:

- 데이터 검증 및 변환
- 엔티티 ↔ DTO 변환
- 비즈니스 규칙 적용
- 예외 처리

**예시**:

```kotlin
@Service
class PostService(
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository
) {
    @Transactional
    fun createPost(request: CreatePostRequest, userId: String): PostResponse {
        // 비즈니스 로직
        val post = Post(...)
        val savedPost = postRepository.save(post)
        return toPostResponse(savedPost)
    }
}
```

### 4. Repository Layer

**역할**: 데이터베이스 접근 및 CRUD 작업

**파일**: `*Repository.kt` (예: `PostRepository.kt`)

**특징**:

- Spring Data JPA 인터페이스 확장
- 커스텀 쿼리 메서드
- `@Query` 어노테이션으로 복잡한 쿼리 정의

**예시**:

```kotlin
interface PostRepository : JpaRepository<Post, String> {
    fun findByUserId(userId: String): List<Post>

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    fun searchByTitle(keyword: String): List<Post>
}
```

### 5. Entity Layer

**역할**: 데이터베이스 테이블 매핑

**파일**: `entity/*.kt`

**주요 엔티티**:

- `User`: 사용자 정보
- `Post`: 게시물 (URL, 제목, 설명, AI 요약 등)
- `Comment`: 댓글
- `Tag`: 태그
- `PostTag`: 게시물-태그 N:M 관계
- `PostLike`: 게시물-좋아요 N:M 관계
- `Bookmark`: 북마크

### 6. DTO Layer

**역할**: API 요청/응답 데이터 구조 정의

**파일**: `dto/*.kt`

**주요 DTO**:

- **Request DTO**: `CreatePostRequest`, `UpdatePostRequest`
- **Response DTO**: `PostResponse`, `CommentResponse`
- **Error DTO**: `ErrorResponse` (통일된 에러 응답)

## 주요 설계 패턴

### 1. 인터페이스 분리 패턴 (Interface Segregation)

**목적**: Swagger 문서와 비즈니스 로직 분리

**구현**:

- API 인터페이스(`*Api.kt`)에 Swagger 어노테이션 집중
- Controller 구현체는 비즈니스 로직 호출에만 집중

**장점**:

- 코드 가독성 향상
- 유지보수 용이
- API 명세와 구현 분리

### 2. DTO 패턴

**목적**: 엔티티와 API 계층 분리

**구현**:

- 요청: `CreatePostRequest`, `UpdatePostRequest`
- 응답: `PostResponse`, `PostListResponse`

**장점**:

- 엔티티 내부 구조 노출 방지
- API 버전 관리 용이
- 순환 참조 방지

### 3. 통일된 에러 처리

**구현**: `GlobalExceptionHandler` + `ErrorResponse`

```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = 404,
                error = "Not Found",
                message = ex.message ?: "Resource not found",
                path = request.requestURI
            )
        )
    }
}
```

**표준 HTTP 상태 코드**:

- `201 Created`: 리소스 생성 성공
- `200 OK`: 조회/수정 성공
- `204 No Content`: 삭제 성공
- `400 Bad Request`: 잘못된 요청
- `404 Not Found`: 리소스 없음
- `500 Internal Server Error`: 서버 오류

## 데이터베이스 스키마

### 주요 테이블

| 테이블     | 설명              | 주요 필드                                    |
| ---------- | ----------------- | -------------------------------------------- |
| `User`     | 사용자            | id, email, nickname, profile_image_url       |
| `Post`     | 게시물            | id, userId, url, title, aiSummary, viewCount |
| `Comment`  | 댓글              | id, userId, postId, content                  |
| `Tag`      | 태그              | id, name                                     |
| `PostTag`  | 게시물-태그 (N:M) | postId, tagId                                |
| `PostLike` | 좋아요 (N:M)      | userId, postId                               |
| `Bookmark` | 북마크 (N:M)      | userId, postId                               |

자세한 ERD는 [`docs/DBML.dbml`](DBML.dbml) 참조.

## 보안 설정

### 현재 상태 (개발 모드)

- 모든 엔드포인트 인증 비활성화
- 임시 사용자 ID(`temp-user-id`) 사용
- CORS 모든 Origin 허용

### 프로덕션 배포 시 TODO

1. **인증 활성화**:

   - JWT 토큰 기반 인증 구현
   - OAuth2 소셜 로그인 (Google, GitHub 등)

2. **권한 관리**:

   - 게시물/댓글 작성자만 수정/삭제 가능
   - 관리자 권한 분리

3. **CORS 설정**:
   - 특정 도메인만 허용

## 테스트 전략

### 통합 테스트

- **도구**: Kotest + Spring MockMvc
- **데이터베이스**: H2 인메모리
- **범위**: Controller → Service → Repository 전체 플로우

### 테스트 격리

- `@DirtiesContext`: 각 테스트 클래스마다 새로운 ApplicationContext
- `TestDataLoader`: 테스트 데이터 초기화

## 확장 가능성

### 추가 가능한 기능

1. **검색 최적화**: Elasticsearch 통합
2. **캐싱**: Redis를 통한 조회 성능 향상
3. **파일 업로드**: AWS S3 연동
4. **실시간 알림**: WebSocket 또는 SSE
5. **AI 요약 자동화**: OpenAI API 연동

### 마이크로서비스 전환 고려사항

현재는 모놀리식 구조이지만, 필요 시 다음과 같이 분리 가능:

- **User Service**: 사용자 관리 및 인증
- **Post Service**: 게시물 및 댓글
- **Notification Service**: 알림 처리

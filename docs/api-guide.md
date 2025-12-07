# LinkSphere API 가이드

## 개요

이 문서는 LinkSphere 백엔드 API의 상세 사용 가이드입니다. 모든 엔드포인트는 JSON 형식으로 데이터를 주고받습니다.

## Base URL

```
http://localhost:8080
```

## 공통 응답 형식

### 성공 응답

각 API는 고유한 응답 구조를 가지지만, HTTP 상태 코드는 다음 규칙을 따릅니다:

- `200 OK`: 조회 또는 수정 성공
- `201 Created`: 리소스 생성 성공
- `204 No Content`: 삭제 성공 (응답 본문 없음)

### 에러 응답

모든 에러는 통일된 `ErrorResponse` 형식을 따릅니다:

```json
{
  "timestamp": "2024-12-07T13:20:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found",
  "path": "/api/posts/invalid-id"
}
```

**공통 에러 상태 코드**:

- `400 Bad Request`: 잘못된 요청 (유효성 검증 실패)
- `404 Not Found`: 리소스를 찾을 수 없음
- `500 Internal Server Error`: 서버 내부 오류

---

## 게시물 (Posts) API

### 1. 게시물 생성

**Endpoint**: `POST /api/posts`

**요청 본문**:

```json
{
  "url": "https://example.com/article",
  "title": "흥미로운 기사",
  "description": "이 기사는 매우 흥미롭습니다",
  "content": "제 생각에는...",
  "tags": ["kotlin", "spring", "backend"]
}
```

**필수 필드**:

- `url`: 공유할 링크 URL

**선택 필드**:

- `title`: 게시물 제목
- `description`: 간단한 설명
- `content`: 사용자 코멘트
- `tags`: 태그 배열

**응답** (`201 Created`):

```json
{
  "id": "post_abc123",
  "url": "https://example.com/article",
  "title": "흥미로운 기사",
  "description": "이 기사는 매우 흥미롭습니다",
  "ogImage": null,
  "aiSummary": null,
  "content": "제 생각에는...",
  "viewCount": 0,
  "createdAt": "2024-12-07T13:20:00",
  "author": {
    "id": "temp-user-id",
    "nickname": "사용자",
    "profileImageUrl": null
  },
  "tags": ["kotlin", "spring", "backend"],
  "likeCount": 0,
  "bookmarkCount": 0,
  "commentCount": 0,
  "isLiked": false,
  "isBookmarked": false
}
```

**curl 예제**:

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://example.com/article",
    "title": "흥미로운 기사",
    "tags": ["kotlin", "spring"]
  }'
```

---

### 2. 게시물 목록 조회

**Endpoint**: `GET /api/posts`

**쿼리 파라미터**:

- `search` (선택): 검색 키워드 (제목, 설명에서 검색)
- `tag` (선택): 태그 필터
- `sort` (선택, 기본값: `createdAt`): 정렬 기준 (`createdAt`, `viewCount`, `likeCount`)
- `page` (선택, 기본값: `0`): 페이지 번호 (0부터 시작)
- `size` (선택, 기본값: `20`): 페이지 크기

**응답** (`200 OK`):

```json
{
  "posts": [
    {
      "id": "post_abc123",
      "url": "https://example.com/article",
      "title": "흥미로운 기사",
      ...
    }
  ],
  "totalElements": 42,
  "totalPages": 3,
  "currentPage": 0,
  "pageSize": 20
}
```

**curl 예제**:

```bash
# 기본 조회
curl http://localhost:8080/api/posts

# 검색 + 정렬
curl "http://localhost:8080/api/posts?search=kotlin&sort=viewCount&page=0&size=10"

# 태그 필터
curl "http://localhost:8080/api/posts?tag=spring"
```

---

### 3. 게시물 상세 조회

**Endpoint**: `GET /api/posts/{id}`

**경로 파라미터**:

- `id`: 게시물 ID

**응답** (`200 OK`):

```json
{
  "id": "post_abc123",
  "url": "https://example.com/article",
  "title": "흥미로운 기사",
  "description": "이 기사는 매우 흥미롭습니다",
  "ogImage": "https://example.com/image.jpg",
  "aiSummary": "AI가 생성한 요약...",
  "content": "제 생각에는...",
  "viewCount": 42,
  "createdAt": "2024-12-07T13:20:00",
  "author": {
    "id": "temp-user-id",
    "nickname": "사용자",
    "profileImageUrl": null
  },
  "tags": ["kotlin", "spring"],
  "likeCount": 5,
  "bookmarkCount": 3,
  "commentCount": 2,
  "isLiked": false,
  "isBookmarked": false
}
```

**curl 예제**:

```bash
curl http://localhost:8080/api/posts/post_abc123
```

---

### 4. 게시물 수정

**Endpoint**: `PATCH /api/posts/{id}`

**경로 파라미터**:

- `id`: 게시물 ID

**요청 본문** (모든 필드 선택):

```json
{
  "title": "수정된 제목",
  "description": "수정된 설명",
  "content": "수정된 내용",
  "aiSummary": "수정된 AI 요약",
  "tags": ["kotlin", "jvm"]
}
```

**응답** (`200 OK`): 수정된 `PostResponse`

**curl 예제**:

```bash
curl -X PATCH http://localhost:8080/api/posts/post_abc123 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "수정된 제목",
    "tags": ["kotlin", "jvm"]
  }'
```

---

### 5. 게시물 삭제

**Endpoint**: `DELETE /api/posts/{id}`

**경로 파라미터**:

- `id`: 게시물 ID

**응답**: `204 No Content` (본문 없음)

**curl 예제**:

```bash
curl -X DELETE http://localhost:8080/api/posts/post_abc123
```

---

### 6. 북마크 토글

**Endpoint**: `POST /api/posts/{id}/bookmark`

**경로 파라미터**:

- `id`: 게시물 ID

**응답** (`200 OK`):

```json
{
  "isBookmarked": true
}
```

**curl 예제**:

```bash
curl -X POST http://localhost:8080/api/posts/post_abc123/bookmark
```

---

## 댓글 (Comments) API

### 1. 댓글 생성

**Endpoint**: `POST /api/comments`

**요청 본문**:

```json
{
  "postId": "post_abc123",
  "content": "좋은 글이네요!"
}
```

**필수 필드**:

- `postId`: 게시물 ID
- `content`: 댓글 내용

**응답** (`201 Created`):

```json
{
  "id": "comment_xyz789",
  "postId": "post_abc123",
  "content": "좋은 글이네요!",
  "createdAt": "2024-12-07T13:25:00",
  "updatedAt": "2024-12-07T13:25:00",
  "author": {
    "id": "temp-user-id",
    "nickname": "사용자",
    "profileImageUrl": null
  }
}
```

**curl 예제**:

```bash
curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{
    "postId": "post_abc123",
    "content": "좋은 글이네요!"
  }'
```

---

### 2. 댓글 목록 조회

**Endpoint**: `GET /api/comments`

**쿼리 파라미터**:

- `postId` (필수): 게시물 ID

**응답** (`200 OK`):

```json
[
  {
    "id": "comment_xyz789",
    "postId": "post_abc123",
    "content": "좋은 글이네요!",
    "createdAt": "2024-12-07T13:25:00",
    "updatedAt": "2024-12-07T13:25:00",
    "author": {
      "id": "temp-user-id",
      "nickname": "사용자",
      "profileImageUrl": null
    }
  }
]
```

**curl 예제**:

```bash
curl "http://localhost:8080/api/comments?postId=post_abc123"
```

---

### 3. 댓글 수정

**Endpoint**: `PATCH /api/comments/{id}`

**경로 파라미터**:

- `id`: 댓글 ID

**요청 본문**:

```json
{
  "content": "수정된 댓글 내용"
}
```

**응답** (`200 OK`): 수정된 `CommentResponse`

**curl 예제**:

```bash
curl -X PATCH http://localhost:8080/api/comments/comment_xyz789 \
  -H "Content-Type: application/json" \
  -d '{
    "content": "수정된 댓글 내용"
  }'
```

---

### 4. 댓글 삭제

**Endpoint**: `DELETE /api/comments/{id}`

**경로 파라미터**:

- `id`: 댓글 ID

**응답**: `204 No Content`

**curl 예제**:

```bash
curl -X DELETE http://localhost:8080/api/comments/comment_xyz789
```

---

## 반응 (Reactions) API

### 좋아요 토글

**Endpoint**: `POST /api/reactions`

**요청 본문**:

```json
{
  "postId": "post_abc123"
}
```

**필수 필드**:

- `postId`: 게시물 ID

**응답** (`200 OK`):

```json
{
  "isLiked": true
}
```

**curl 예제**:

```bash
curl -X POST http://localhost:8080/api/reactions \
  -H "Content-Type: application/json" \
  -d '{
    "postId": "post_abc123"
  }'
```

---

## Swagger UI

모든 API는 Swagger UI에서 인터랙티브하게 테스트할 수 있습니다:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger UI에서는:

- 모든 엔드포인트 목록 확인
- 요청/응답 스키마 확인
- "Try it out" 버튼으로 직접 API 호출
- 에러 응답 예시 확인

---

## Postman Collection

프로젝트에 포함된 Postman Collection을 사용하여 API를 테스트할 수 있습니다:

```
docs/LinkSphere_API.postman_collection.json
```

**사용 방법**:

1. Postman 실행
2. Import → File → `LinkSphere_API.postman_collection.json` 선택
3. Collection에서 원하는 요청 선택 후 Send

---

## 인증 (개발 모드)

현재 모든 API는 인증 없이 사용 가능하며, 임시 사용자 ID(`temp-user-id`)가 자동으로 할당됩니다.

**프로덕션 배포 시**:

- JWT 토큰 기반 인증 구현 예정
- `Authorization: Bearer <token>` 헤더 필요
- 사용자별 권한 관리 (본인 게시물/댓글만 수정/삭제)

---

## 페이지네이션

게시물 목록 조회 시 페이지네이션이 적용됩니다:

```
GET /api/posts?page=0&size=20
```

**응답 구조**:

```json
{
  "posts": [...],
  "totalElements": 100,  // 전체 게시물 수
  "totalPages": 5,       // 전체 페이지 수
  "currentPage": 0,      // 현재 페이지 (0부터 시작)
  "pageSize": 20         // 페이지 크기
}
```

---

## 에러 처리 예시

### 404 Not Found

```bash
curl http://localhost:8080/api/posts/invalid-id
```

**응답**:

```json
{
  "timestamp": "2024-12-07T13:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found",
  "path": "/api/posts/invalid-id"
}
```

### 400 Bad Request

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{}'
```

**응답**:

```json
{
  "timestamp": "2024-12-07T13:31:00",
  "status": 400,
  "error": "Bad Request",
  "message": "url: must not be null",
  "path": "/api/posts"
}
```

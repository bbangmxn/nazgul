<!-- PROJECT SHIELDS -->
<div align="center">

[![Java][java-shield]][java-url]
[![Spring Boot][spring-shield]][spring-url]
[![JWT][jwt-shield]][jwt-url]
[![License][license-shield]][license-url]

</div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/BbangMxn/Nazgul_server">
    <img src="docs/logo.png" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center">Nazgul Server</h3>

  <p align="center">
    취미 기반 소셜 매칭 플랫폼 백엔드
    <br />
    <a href="#-api"><strong>API 문서 »</strong></a>
    <br />
    <br />
    <a href="#-빠른-시작">빠른 시작</a>
    ·
    <a href="https://github.com/BbangMxn/Nazgul_server/issues">버그 리포트</a>
    ·
    <a href="https://github.com/BbangMxn/Nazgul_server/issues">기능 제안</a>
  </p>
</div>

<!-- ABOUT -->
## 프로젝트 소개

**Nazgul**은 취미를 기반으로 사람들을 연결하는 소셜 매칭 플랫폼입니다.

- 🎯 **취미 매칭** — 비슷한 취미를 가진 사람 추천
- 📝 **취미별 피드** — 관심 취미 게시물 모아보기
- 👥 **팔로우 시스템** — 관심 유저 팔로우
- 💬 **댓글 & 좋아요** — 소셜 인터랙션

### 기술 스택

[![Java][java-badge]][java-url]
[![Spring Boot][spring-badge]][spring-url]
[![Spring Security][security-badge]][security-url]
[![JPA][jpa-badge]][jpa-url]
[![H2][h2-badge]][h2-url]

---

## 📈 주요 기능

| 기능 | 설명 |
|-----|------|
| **사용자 인증** | JWT 기반 로그인/회원가입 |
| **취미 관리** | 44개 기본 취미, 숙련도 설정 |
| **취미 매칭** | 공통 취미 기반 사용자 추천 |
| **피드 시스템** | 팔로우/취미 기반 게시물 피드 |
| **소셜 기능** | 팔로우, 좋아요, 댓글, 대댓글 |

---

## 🔧 기술적 특징

### 1. 취미 기반 매칭 알고리즘

```java
// 사용자의 취미를 기반으로 비슷한 관심사를 가진 사용자 추천
@Query("SELECT DISTINCT u FROM User u " +
       "JOIN u.hobbies uh " +
       "WHERE uh.hobby.id IN :hobbyIds " +
       "AND u.id != :userId")
List<User> findByHobbiesIn(List<Long> hobbyIds, Long userId);
```

### 2. JWT 인증

```java
// Stateless 인증으로 확장성 확보
String token = jwtTokenProvider.createToken(user.getEmail());
```

### 3. 도메인 주도 설계

```
domain/
├── user/        # 사용자, 팔로우
├── hobby/       # 취미, 사용자-취미 연결
└── post/        # 게시물, 댓글, 좋아요
```

---

## 🏗️ 아키텍처

```
┌─────────────────────────────────────────────────────┐
│                   Controller Layer                   │
│    AuthController · UserController · PostController │
├─────────────────────────────────────────────────────┤
│                    Service Layer                     │
│      AuthService · UserService · PostService        │
├─────────────────────────────────────────────────────┤
│                   Repository Layer                   │
│   UserRepository · HobbyRepository · PostRepository │
├─────────────────────────────────────────────────────┤
│                    Entity Layer                      │
│       User · Hobby · Post · Comment · Follow        │
└─────────────────────────────────────────────────────┘
```

---

## 🚀 빠른 시작

```bash
# 1. 클론
git clone https://github.com/BbangMxn/Nazgul_server.git
cd Nazgul_server

# 2. 빌드 & 실행
./gradlew bootRun

# 서버: http://localhost:8080
# H2 콘솔: http://localhost:8080/h2-console
```

### 환경변수 (프로덕션)

```bash
cp .env.example .env
```

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/nazgul
JWT_SECRET=your-super-secret-key
```

---

## 📡 API

### 인증

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| `POST` | `/api/v1/auth/signup` | 회원가입 | - |
| `POST` | `/api/v1/auth/login` | 로그인 | - |

### 사용자

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| `GET` | `/api/v1/users/me` | 내 정보 | ✅ |
| `GET` | `/api/v1/users/:id` | 사용자 조회 | - |
| `PUT` | `/api/v1/users/me` | 프로필 수정 | ✅ |
| `POST` | `/api/v1/users/:id/follow` | 팔로우 | ✅ |
| `DELETE` | `/api/v1/users/:id/follow` | 언팔로우 | ✅ |
| `GET` | `/api/v1/users/recommended` | 추천 사용자 | ✅ |

### 취미

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| `GET` | `/api/v1/hobbies` | 전체 취미 | - |
| `GET` | `/api/v1/hobbies/category/:cat` | 카테고리별 | - |
| `GET` | `/api/v1/hobbies/my` | 내 취미 | ✅ |
| `POST` | `/api/v1/hobbies/my` | 취미 추가 | ✅ |
| `DELETE` | `/api/v1/hobbies/my/:id` | 취미 삭제 | ✅ |

### 게시물

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| `POST` | `/api/v1/posts` | 작성 | ✅ |
| `GET` | `/api/v1/posts/:id` | 상세 | - |
| `GET` | `/api/v1/posts/feed` | 피드 | ✅ |
| `GET` | `/api/v1/posts/hobby/:id` | 취미별 | - |
| `GET` | `/api/v1/posts/recommended` | 추천 | ✅ |
| `GET` | `/api/v1/posts/popular` | 인기 | - |
| `POST` | `/api/v1/posts/:id/like` | 좋아요 | ✅ |

### 댓글

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| `POST` | `/api/v1/posts/:id/comments` | 작성 | ✅ |
| `GET` | `/api/v1/posts/:id/comments` | 목록 | - |
| `DELETE` | `/api/v1/posts/:id/comments/:cid` | 삭제 | ✅ |

---

## 📂 프로젝트 구조

```
src/main/java/com/nazgul/
├── NazgulApplication.java
├── domain/
│   ├── user/
│   │   ├── entity/          # User, Follow
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── dto/
│   ├── hobby/
│   │   ├── entity/          # Hobby, UserHobby
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── dto/
│   └── post/
│       ├── entity/          # Post, Comment, PostLike
│       ├── repository/
│       ├── service/
│       ├── controller/
│       └── dto/
└── global/
    ├── config/              # Security, DataInitializer
    ├── security/            # JWT, UserDetails
    └── exception/           # GlobalExceptionHandler
```

---

## 🎯 취미 카테고리

| 카테고리 | 취미 예시 |
|---------|----------|
| **SPORTS** | 축구, 농구, 테니스, 헬스, 요가 |
| **MUSIC** | 기타, 피아노, 드럼, 노래 |
| **ARTS** | 그림, 사진, 영상, 공예 |
| **GAMES** | PC게임, 콘솔, 보드게임 |
| **OUTDOOR** | 등산, 캠핑, 낚시, 여행 |
| **CULTURE** | 독서, 영화, 연극, 전시회 |
| **FOOD** | 요리, 베이킹, 커피, 맛집탐방 |
| **TECH** | 코딩, 3D프린팅, 드론 |
| **PETS** | 강아지, 고양이, 식물 |

---

## 🗺️ 로드맵

- [x] 사용자 인증 (JWT)
- [x] 취미 시스템
- [x] 게시물 CRUD
- [x] 댓글/좋아요
- [x] 팔로우 시스템
- [x] 취미 기반 추천
- [ ] 실시간 알림 (WebSocket)
- [ ] 채팅 기능
- [ ] 이미지 업로드 (S3)

---

## 📄 라이선스

MIT License - [LICENSE](LICENSE)

---

<div align="center">
  
**[⬆ 맨 위로](#nazgul-server)**

</div>

<!-- MARKDOWN LINKS -->
[java-shield]: https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[java-url]: https://openjdk.org/
[spring-shield]: https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[spring-url]: https://spring.io/projects/spring-boot
[jwt-shield]: https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white
[jwt-url]: https://jwt.io/
[license-shield]: https://img.shields.io/badge/License-MIT-blue?style=for-the-badge
[license-url]: LICENSE

[java-badge]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[spring-badge]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[security-badge]: https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white
[security-url]: https://spring.io/projects/spring-security
[jpa-badge]: https://img.shields.io/badge/JPA-59666C?style=for-the-badge
[jpa-url]: https://spring.io/projects/spring-data-jpa
[h2-badge]: https://img.shields.io/badge/H2-0000BB?style=for-the-badge
[h2-url]: https://www.h2database.com/

# Nazgul Server

`server`는 헥사고날 구조로 정리한 Spring Boot 백엔드입니다. 현재는 소셜/JWT 코드를 제거하고 `rag`, `optimization`, `common(exception)`만 유지합니다.

## Modules

- `common`: 전역 예외 처리
- `rag`: Entity/Card 기반 검색 실험 모듈
- `optimization`: 최적화 작업 생성과 워커 모듈

자세한 구조는 [docs/server-structure.md](docs/server-structure.md), [docs/rag-hexagonal-architecture.md](docs/rag-hexagonal-architecture.md), [docs/optimization-hexagonal-architecture.md](docs/optimization-hexagonal-architecture.md)를 참고하세요.

## Package Layout

```text
src/main/java/com/nazgul
├── NazgulApplication.java
├── common
│   └── exception
├── optimization
│   ├── adapter
│   ├── application
│   ├── config
│   └── domain
└── rag
    ├── adapter
    ├── application
    ├── config
    └── domain
```

## Runtime Stack

- Java 17
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Flyway
- Redis
- Spring AI + pgvector
- Spring Actuator

## Supabase

- Supabase를 JPA로 붙일 때는 별도 SDK 대신 PostgreSQL JDBC 연결을 사용합니다.
- 연결 정보는 `.env.example`의 `SUPABASE_DB_*` 변수를 기준으로 넣으면 됩니다.
- `db/migration` 아래 Flyway 스크립트로 DB 확장을 관리할 수 있습니다.

## Run

```bash
./gradlew bootRun
```

필수 환경변수 예시는 [.env.example](.env.example)에 있습니다.

## Endpoints

- `GET /api/v1/rag/search?query=...`
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/prometheus`

## Next Focus

- RAG ingest 파이프라인 추가
- card-first retrieval 개선
- recommendation 후보 생성 계층 확장

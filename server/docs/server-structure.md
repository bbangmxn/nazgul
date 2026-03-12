# Server Structure

## Top Level

```text
server/
├── docs/                # 구조/설계 문서
├── gradle/              # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/com/nazgul
│   │   └── resources/
│   └── test/
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## Java Package Layout

```text
com.nazgul
├── common
│   └── exception
├── optimization
│   ├── adapter
│   │   ├── in
│   │   └── out
│   ├── application
│   ├── config
│   └── domain
└── rag
    ├── adapter
    ├── application
    ├── config
    └── domain
```

## Module Roles

- `common`: 전역 예외 처리
- `optimization`: 최적화 실험용 헥사고날 모듈
- `rag`: Entity/Card 기반 검색 실험용 헥사고날 모듈

## Current Direction

- 소셜/JWT 코드는 제거하고 알고리즘 연구용 서버만 유지
- 새 기능은 `optimization`, `rag`처럼 모듈 단위로 확장
- 공통 인프라는 필요한 범위만 `common`에 둔다
- DB 연결은 PostgreSQL/Supabase를 같은 JPA 계층으로 다룬다

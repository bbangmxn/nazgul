# Optimization Hexagonal Architecture

## Folder Layout

```text
server/src/main/java/com/nazgul/optimization
├── adapter
│   ├── in
│   │   ├── scheduler
│   │   └── web
│   └── out
│       ├── ai
│       ├── lock
│       ├── persistence
│       │   ├── entity
│       │   ├── mapper
│       │   └── repository
│       └── solver
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
├── config
└── domain
    └── model
```

## Why This Split

- `adapter/in/scheduler`: 워커, 배치, 메시지 컨슈머처럼 유스케이스를 호출하는 진입점
- `adapter/in/web`: 향후 REST API 진입점
- `adapter/out/ai`: LLM 기반 계획 생성 어댑터
- `adapter/out/lock`: Redis 기반 분산 락 어댑터
- `adapter/out/persistence/entity`: JPA 엔티티
- `adapter/out/persistence/mapper`: JPA 엔티티와 도메인 모델 변환
- `adapter/out/persistence/repository`: Spring Data JPA 리포지토리
- `adapter/out/solver`: 실제 솔버 연동 어댑터
- `application/port/in`: 최적화 유스케이스 진입 인터페이스
- `application/port/out`: 외부 의존성 포트
- `application/service`: 유스케이스 오케스트레이션
- `config`: 모듈 설정, `@ConfigurationProperties`
- `domain/model`: 최적화 코어 모델

## Supabase / PostgreSQL Notes

- Supabase를 JPA로 붙일 때 별도 Supabase SDK는 필요 없다.
- Spring Data JPA + PostgreSQL JDBC 드라이버 조합이면 충분하다.
- 운영 스키마 변경은 Hibernate 자동 생성보다 Flyway 마이그레이션 기준으로 관리하는 편이 안전하다.

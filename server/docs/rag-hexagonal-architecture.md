# RAG Hexagonal Architecture

## Folder Layout

```text
server/src/main/java/com/nazgul/rag
├── adapter
│   ├── in
│   │   └── web
│   └── out
│       └── persistence
│           ├── entity
│           ├── mapper
│           └── repository
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
├── config
└── domain
    ├── entity
    ├── enums
    └── vo
```

## Why This Split

- `domain/entity`: RAG의 핵심 엔티티
- `domain/enums`: 카드/문서/관계 타입
- `domain/vo`: evidence, retrieval scope 같은 값 객체
- `application/port/in`: 유스케이스 진입점
- `application/port/out`: 저장소, 검색기, 추적 저장 같은 외부 의존성
- `application/service`: 유스케이스 오케스트레이션
- `adapter/in/web`: REST API
- `adapter/out/persistence/entity`: JPA 엔티티
- `adapter/out/persistence/mapper`: JPA <-> 도메인 변환
- `adapter/out/persistence/repository`: Spring Data Repository

## Core Entities

- `RagDocument`: 원문 문서 단위
- `RagChapter`: 문서 내부 구조 단위
- `RagChunk`: 실제 근거 텍스트 단위
- `RagCard`: 검색과 추천의 중심이 되는 의미 단위
- `RagCardRelation`: 카드 간 관계
- `RagQueryTrace`: 검색 추적 로그

## Value Objects

- `EvidenceRef`: 카드가 참조하는 근거 청크
- `RetrievalScope`: card-first 검색 이후 chunk 검색 범위

## Search Flow

1. Query를 입력받는다.
2. `RagCard`를 먼저 검색한다.
3. 선택된 카드의 `documentId`, `chapterId`로 `RetrievalScope`를 만든다.
4. 제한된 범위에서 `RagChunk`를 검색한다.
5. 범위 결과가 없으면 global chunk 검색으로 한 번 fallback 한다.
6. 결과 카드/근거 청크를 반환하고 `RagQueryTrace`를 저장한다.

## Recommendation Expansion

현재 모듈은 검색까지 스캐폴딩한다. 이후 추천 AI는 아래 엔티티를 추가해 같은 코어 위에서 확장한다.

- `UserProfile`
- `UserInterest`
- `Interaction`
- `RecommendationTrace`

추천은 `card retrieval -> candidate generation -> rerank -> explanation` 순으로 얹는다.

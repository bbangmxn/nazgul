# Nazgul Server

`server`는 취미 기반 SNS 실험을 위한 Spring Boot 백엔드입니다. 현재는 기존 소셜/JWT 코드를 걷어내고, Notion에 정리한 `Library RAG`, `Entity Card`, `Technical Design Document` 기준으로 로컬 RAG 프로토타입을 만드는 데 집중하고 있습니다.

## Goal

이 서버의 목표는 단순한 chunk search가 아니라, `운영 가능한 RAG 엔진`을 로컬 환경에서 검증하는 것입니다.

핵심 기준:

- `Card + Chunk` 2계층 retrieval
- scope 제한 기반 chunk search
- trace 저장
- 증분 업데이트 가능한 구조
- 로컬 SQLite/Ollama 기준 빠른 실험

관련 원문:

- [Library RAG](https://www.notion.so/3170de9c810b80e198b4ea9f671bfbcc)
- [Entity Card](https://www.notion.so/3140de9c810b80929456ca854974d56c)
- [Technical Design Document](https://www.notion.so/3170de9c810b80daa6e4c159c55f4071)

## Library RAG Summary

`Library RAG` 문서에서 가장 중요한 문제 정의는 네 가지입니다.

- `Token Explosion`: 중복 chunk 때문에 토큰이 빠르게 불어난다.
- `Context Fragmentation`: 관련 정보가 흩어져 LLM이 재구성 비용을 떠안는다.
- `Update Cascade`: 문서 일부 수정에도 전체 재임베딩이 필요해진다.
- `Lack of Structural Awareness`: 의존, 반박, 버전 관계를 추적하기 어렵다.

이 프로젝트는 그 문제를 다음 원칙으로 풀고 있습니다.

- `Card`는 의미 단위, `Chunk`는 근거 단위입니다.
- chunk 검색은 항상 card/source scope를 좁힌 뒤 수행합니다.
- graph는 초기부터 별도 graph DB로 가지 않고 relation table로 제한합니다.
- trace를 남겨야 retrieval 실패 원인을 나중에 분석할 수 있습니다.
- 저장소는 교체 가능해야 하므로 storage 인터페이스를 먼저 고정합니다.

`Entity Card` 기준으로 card는 `고정 크기`, `근거 포인터`, `버전`, `갱신 가능성`을 가진 압축된 지식 인터페이스로 봅니다.

Nazgul에 매핑하면 이렇게 정리됩니다.

| Notion 개념 | 현재 구현 |
|---|---|
| Entity Card | `HobbyCard` |
| Evidence Chunk | `ContentChunk` |
| Graph Edge | `HobbyCardRelation` |
| Retrieval Trace | `RecommendationTrace` |
| Scope-limited retrieval | `hobbyId`, `sourceId`, `scopeKey` 기반 검색 |

상세 요약:

- [docs/library-rag-summary.md](docs/library-rag-summary.md)
- [docs/local-rag-evaluation-report.md](docs/local-rag-evaluation-report.md)

## Current Modules

- `common`: 전역 예외 처리
- `rag`: document/chunk/card/query-trace 중심의 일반 RAG 실험 모듈
- `optimization`: 취미 SNS용 `ContentChunk`, `HobbyCard`, `HobbyCardRelation`, `RecommendationTrace` 기반 실험 모듈

상세 구조:

- [docs/server-structure.md](docs/server-structure.md)
- [docs/rag-hexagonal-architecture.md](docs/rag-hexagonal-architecture.md)
- [docs/rag-entity-model.md](docs/rag-entity-model.md)
- [docs/optimization-hexagonal-architecture.md](docs/optimization-hexagonal-architecture.md)
- [docs/optimization-rag-quickstart.md](docs/optimization-rag-quickstart.md)

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

## Entity Model

현재 취미 SNS용 RAG 실험은 아래 엔티티를 기준으로 돌아갑니다.

- `ContentChunk`: POST/COMMENT 같은 원문을 쪼갠 근거 단위
- `HobbyCard`: 검색/추천의 중심이 되는 의미 단위 카드
- `HobbyCardRelation`: 카드 간 명시적 관계
- `RecommendationTrace`: 검색/추천 로그

이 구조는 Notion의 `Card = 운영 단위`, `Chunk = 검색/근거 단위` 원칙을 그대로 따른 것입니다.

## Runtime Stack

- Java 17
- Spring Boot 3.5
- Spring Data JPA
- SQLite
- PostgreSQL
- Flyway
- Redis
- Spring AI
- Ollama
- Spring Actuator

## Storage Strategy

현재 저장소 전략은 두 갈래입니다.

- 기본 서버 설정: PostgreSQL / pgvector / Flyway
- 로컬 RAG 테스트: SQLite 단일 파일 DB

Notion의 설계 원칙대로, DB는 정본 그 자체가 아니라 서빙 계층으로 취급하고 교체 가능하게 유지합니다.

## Local SQLite + Ollama Test

SQLite + Ollama 기준으로 취미 SNS용 RAG 프로토타입을 바로 돌릴 수 있습니다.

서버 실행:

```powershell
cd C:\Users\jixso\Desktop\zed\MyData\Nazgul\server
scripts\start-hobby-rag-sqlite.cmd -ChatModel "qwen3:8b"
```

검색/추천 테스트:

```powershell
scripts\test-hobby-rag.cmd -HobbyId 10 -Query "러닝 초보 루틴" -TimeoutSec 300
```

벤치마크:

```powershell
scripts\test-hobby-rag.cmd -HobbyId 10 -Benchmark -TimeoutSec 300
```

현재 SQLite 프로필에서는 임시 취미 데이터가 자동 주입됩니다.

- `hobbyId=10`: 러닝
- `hobbyId=20`: 홈카페
- `hobbyId=30`: 캠핑

주의:

- 지금 단계는 `품질 평가용 대규모 데이터셋`이 아니라 `파이프라인 검증용 seed 데이터`입니다.
- 결과가 잘 나온다고 바로 성능이 좋다는 뜻은 아닙니다.
- 실제 비교를 하려면 더 많은 테스트 데이터와 query-label 세트가 필요합니다.

## Latest Local Test Result

기준 날짜: `2026-03-12`

실행 환경:

- profile: `sqlite`
- provider: `ollama`
- chat model: `qwen3:8b`
- embedding model: `nomic-embed-text:latest`

대표 검색 결과:

- query: `러닝 초보 루틴`
- cards: `러닝화 선택 기준`, `무릎 통증 줄이기`, `러닝 입문 루틴`
- chunk count: `3`
- recommended post ids: `1002`, `1003`, `1001`

대표 벤치마크 결과:

- iterations: `1`
- benchmark queries: `러닝 초보 루틴`, `러닝화 추천`, `무릎 통증 줄이기`
- total latency: `181594 ms`
- average latency: `60531 ms / query`

해석:

- 단일 의도 질의인 `러닝화 추천`, `무릎 통증 줄이기`는 기대한 카드만 반환한다.
- 복합 질의인 `러닝 초보 루틴`은 관련 카드를 찾지만, 이상적인 top1인 `러닝 입문 루틴`이 첫 번째가 아니다.
- 따라서 현재 구조는 `작동 검증` 단계로는 충분하지만, `정량 성능 검증` 단계로 보기에는 아직 이르다.

사람이 읽기 쉬운 결과 해석 문서:

- [docs/local-rag-evaluation-report.md](docs/local-rag-evaluation-report.md)

## Endpoints

- `GET /api/v1/rag/search?query=...`
- `POST /api/v1/optimization/rag/ingest`
- `POST /api/v1/optimization/rag/relations`
- `GET /api/v1/optimization/rag/search`
- `GET /api/v1/optimization/rag/recommend`
- `GET /api/v1/optimization/rag/benchmark`
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/prometheus`

## Environment

필수 환경변수 예시는 [.env.example](.env.example)에 있습니다.

Supabase를 붙일 때는 별도 SDK 대신 PostgreSQL JDBC 연결을 사용합니다.

- `SUPABASE_DB_*`
- `DB_*`
- `OPENAI_*`
- `APP_RAG_OLLAMA_*`

## What Is Working

- hexagonal package structure
- SQLite 기반 로컬 RAG 테스트
- Ollama chat + embedding 연결
- card-first retrieval 실험
- relation expansion
- recommendation trace 저장
- terminal script 기반 수동 테스트

## What Is Not Solved Yet

- 대규모 테스트 데이터셋
- Recall@k, MRR, nDCG 같은 정량 평가
- chunk-only vs card-first 비교 자동화
- 증분 업데이트 파이프라인
- 실제 SNS 원본 테이블(`Post`, `User`, `Hobby`) 기반 ingest 자동화
- 느린 로컬 모델에서의 응답 지연 최적화

## Next Focus

- 취미별 테스트 데이터를 대량 생성
- query / expected result 세트 구축
- retrieval 품질 지표 추가
- fast mode와 rerank mode 분리
- SQLite baseline과 PostgreSQL/pgvector baseline 비교

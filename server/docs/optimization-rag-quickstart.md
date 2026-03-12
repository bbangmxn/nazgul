# Optimization RAG Quickstart

빠른 테스트 버전 기준으로 `optimization` 모듈에서 취미 SNS용 RAG 흐름을 바로 검증하는 방법이다.

## 구성

- 입력 원천: `POST`, `COMMENT` 같은 SNS 콘텐츠
- 중간 산출물: `ContentChunk`, `HobbyCard`, `HobbyCardRelation`
- 출력: 검색 결과, 추천 포스트 ID, LLM 요약, trace ID

핵심 진입점:

- `POST /api/v1/optimization/rag/ingest`
- `POST /api/v1/optimization/rag/relations`
- `GET /api/v1/optimization/rag/search`
- `GET /api/v1/optimization/rag/recommend`

## 실행

`.env.example`를 기준으로 환경변수를 맞춘 뒤 실행한다.
SQLite + Ollama 테스트는 `sqlite` 프로필과 `deepseek-r1:8b`를 기준으로 잡았다.

```powershell
cd C:\Users\jixso\Desktop\zed\MyData\Nazgul\server
.\scripts\start-hobby-rag-sqlite.ps1
```

모델을 바꿔 비교하려면:

```powershell
.\scripts\start-hobby-rag-sqlite.ps1 -ChatModel "qwen3:8b"
```

로컬 Ollama 서버가 떠 있고 `qwen3:8b`, `nomic-embed-text:latest` 모델이 준비되어 있으면:

- 답변 생성은 Ollama chat 모델
- 카드/청크 rerank는 Ollama embedding 모델

를 사용한다. Ollama가 응답하지 않으면 fallback 요약과 기본 정렬로 동작한다.

## 1. 콘텐츠 적재

```powershell
curl -X POST "http://localhost:8080/api/v1/optimization/rag/ingest" ^
  -H "Content-Type: application/json" ^
  -d "{\"sourceType\":\"POST\",\"sourceId\":101,\"hobbyId\":10,\"cardType\":\"TOPIC\",\"title\":\"러닝 입문 팁\",\"content\":\"러닝은 처음에는 짧게 시작하는 게 좋다. 주 3회로 적응하고 회복 시간을 두면서 점진적으로 거리를 늘려야 한다.\",\"tags\":[\"running\",\"beginner\"],\"scopeKey\":\"public\"}"
```

응답에는 생성된 `card`와 `chunks`가 포함된다.

## 2. 연관 카드 연결

두 번째 콘텐츠도 적재한 뒤 생성된 카드 ID를 사용해 relation을 만든다.

```powershell
curl -X POST "http://localhost:8080/api/v1/optimization/rag/relations" ^
  -H "Content-Type: application/json" ^
  -d "{\"sourceCardId\":\"CARD_ID_1\",\"targetCardId\":\"CARD_ID_2\",\"relationType\":\"RELATED_TO\",\"weight\":0.9}"
```

## 3. 검색 테스트

```powershell
.\scripts\test-hobby-rag.ps1 -HobbyId 10 -Query "러닝 초보 루틴"
```

응답 항목:

- `query`
- `answer`
- `recommendedPostIds`
- `cards`
- `chunks`
- `traceId`

## 4. 추천 테스트

```powershell
curl "http://localhost:8080/api/v1/optimization/rag/recommend?userId=1&hobbyId=10&query=%EB%9F%AC%EB%8B%9D%20%EC%B4%88%EB%B3%B4&limit=5"
```

응답 항목:

- `recommendedPostIds`
- `recommendedCards`
- `evidenceChunks`

## 5. 벤치마크 테스트

`sqlite` 프로필에서는 임시 취미 SNS 데이터가 자동으로 주입된다.

```powershell
.\scripts\test-hobby-rag.ps1 -HobbyId 10 -Benchmark
```

응답 항목:

- `provider`
- `chatModel`
- `embeddingModel`
- `iterations`
- `totalLatencyMs`
- `averageLatencyMs`
- `items[]`

## 6. 최근 로컬 테스트 결과

기준 날짜: `2026-03-12`

검색 예시:

- query: `러닝 초보 루틴`
- recommended post ids: `1002`, `1003`, `1001`
- returned cards:
  - `러닝화 선택 기준`
  - `무릎 통증 줄이기`
  - `러닝 입문 루틴`

벤치마크 예시:

- provider: `ollama`
- chat model: `qwen3:8b`
- embedding model: `nomic-embed-text:latest`
- total latency: `181594 ms`
- average latency: `60531.33 ms / query`

해석:

- 파이프라인은 정상 동작한다.
- 단순 질의는 비교적 자연스럽게 맞는다.
- 복합 질의 랭킹은 아직 불완전하다.
- 현재 병목은 SQLite보다 로컬 Ollama 응답 시간에 가깝다.

## 현재 구현 범위

- storage: SQLite 파일 DB
- retrieval: keyword 후보 + Ollama embedding rerank
- relation expansion: 1-hop 카드 확장
- grounding: 카드 기준 sourceId scope로 chunk 검색
- generation: Ollama chat 기반 요약
- fallback: Ollama가 응답하지 않으면 규칙 기반 요약
- trace: 검색/추천 호출마다 `RecommendationTrace` 저장

## 다음 단계

- `pgvector` 기반 카드/청크 임베딩 검색
- Supabase PostgreSQL 저장소 구현
- 클릭/저장 로그를 이용한 개인화 rerank
- `Post`, `Comment`, `UserHobby`에서 자동 ingest 파이프라인 연결

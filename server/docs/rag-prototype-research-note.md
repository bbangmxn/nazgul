# RAG Prototype Research Note

이 문서는 Nazgul의 로컬 RAG 실험을 `완성된 평가 보고서`가 아니라 `초기 연구 기록`으로 정리한 노트다. 원본 JSON 출력은 테스트 스크립트에서 그대로 확인하고, 여기서는 현재 프로토타입이 무엇을 검증했고 무엇이 아직 미정인지에 초점을 둔다.

## Test Setup

- date: `2026-03-12`
- environment: `Windows local machine`
- storage: `SQLite`
- LLM provider: `Ollama`
- chat model: `qwen3:8b`
- embedding model: `nomic-embed-text:latest`
- dataset: seed data only

현재 seed 데이터는 아래 취미군만 포함한다.

- `hobbyId=10`: 러닝
- `hobbyId=20`: 홈카페
- `hobbyId=30`: 캠핑

즉 이 문서는 `실서비스 성능 검증`이 아니라, `RAG 파이프라인이 실제로 동작하는지`를 확인하기 위한 연구 메모에 가깝다.

## What The Pipeline Does

현재 로컬 프로토타입의 흐름은 아래와 같다.

1. query를 받는다.
2. `HobbyCard` 후보를 찾는다.
3. relation과 점수 기준으로 카드를 정렬한다.
4. 선택된 카드 기준으로 `ContentChunk`를 찾는다.
5. chunk를 근거로 answer를 만든다.
6. 결과를 `RecommendationTrace`에 남긴다.

핵심은 `Card`와 `Chunk`를 분리했다는 점이다.

- `HobbyCard`: 의미 단위
- `ContentChunk`: 근거 단위

## Current Research Observation

예시 query:

- `러닝 초보 루틴`

예시 결과:

- recommended post ids: `1002`, `1003`, `1001`
- returned cards:
  - `러닝화 선택 기준`
  - `무릎 통증 줄이기`
  - `러닝 입문 루틴`
- returned chunks: `3`

현재 이 결과에서 관찰할 수 있는 것은 아래와 같다.

### 1. cards

`cards`는 시스템이 "이 질문과 관련 있다"고 판단한 의미 단위 요약이다.

- `러닝 입문 루틴`
- `러닝화 선택 기준`
- `무릎 통증 줄이기`

즉 시스템은 단순히 문장을 찾는 게 아니라, 질문과 관련된 주제 카드를 먼저 고르고 있다. 이것은 `card-first retrieval`이 최소한 구조적으로는 동작하고 있다는 관찰값이다.

### 2. chunks

`chunks`는 실제 근거 텍스트다. answer는 이 텍스트를 바탕으로 생성된다.

예:

- 러닝화 선택 기준에 대한 문장
- 무릎 통증 줄이기에 대한 문장
- 러닝 입문 루틴에 대한 문장

즉 `Card`가 후보 압축 역할을 하고, `Chunk`가 grounding 역할을 한다. 이는 `Card + Chunk` 이중 retrieval 구조가 프로토타입 수준에서는 이어지고 있음을 보여준다.

### 3. recommendedPostIds

이 값은 추천 결과다. 현재는 seed 데이터가 작아서 사실상 관련 포스트가 거의 모두 반환된다.

그래서 지금 이 값은 `추천 품질이 높다`는 뜻이 아니라, `후보 선정과 결과 반환이 정상적으로 동작한다`는 뜻에 가깝다. 현 단계에서는 추천 정확도보다 `retrieval 흐름이 연결되는지`를 보는 편이 더 맞다.

## Current Benchmark Observation

실행 결과:

- total latency: `181594 ms`
- average latency: `60531.33 ms / query`
- queries:
  - `러닝 초보 루틴`
  - `러닝화 추천`
  - `무릎 통증 줄이기`

이 수치에서 바로 확정할 수 있는 것은 많지 않지만, 현재 연구 단계에서 참고할 수 있는 관찰값은 있다.

### What is observable

- 단일 의도 질의는 비교적 자연스럽다.
  - `러닝화 추천` -> `러닝화 선택 기준`
  - `무릎 통증 줄이기` -> `무릎 통증 줄이기`
- answer, cards, chunks, recommendation이 한 번에 연결된다.
- trace가 남기 때문에 나중에 실패 분석이 가능하다.

### What remains unresolved

- 평균 `60초/query`는 실사용 기준으로 매우 느리다.
- `러닝 초보 루틴`처럼 복합 의도 질의에서 `러닝 입문 루틴`이 top1이 아니다.
- 데이터가 3개뿐이라서 추천 품질을 말하기 어렵다.

즉 현재 결과는 `works`에 가깝고, `good performance`에 대한 결론을 내릴 단계는 아니다.

## What This Research Confirms

현재 결과로 확실히 말할 수 있는 건 세 가지다.

1. `Card-first -> Chunk-grounding -> Answer -> Trace` 파이프라인은 동작한다.
2. 현재 병목은 SQLite보다 Ollama 응답 시간에 더 가깝다.
3. 성능 비교를 말하려면 더 큰 데이터셋과 정답 라벨이 필요하다.

## Open Research Questions

`러닝 초보 루틴`의 이상적인 top1은 보통 `러닝 입문 루틴`이어야 한다. 그런데 현재는 `러닝화 선택 기준`이 먼저 나온다.

이건 아래 가능성을 의미한다.

- seed 데이터가 너무 적다.
- 현재 scoring이 keyword overlap 또는 rerank 영향에 치우쳐 있다.
- relation expansion이 복합 질의 의도를 제대로 반영하지 못한다.

즉 구조 검증은 됐지만, ranking tuning은 아직 시작 단계다. 이 부분은 현재 결론이라기보다 다음 연구 질문에 더 가깝다.

## Next Research Steps

다음 단계는 아래 순서가 맞다.

1. 취미별 테스트 데이터를 `50~100개 이상`으로 늘린다.
2. query별 정답 포스트 라벨을 만든다.
3. `Recall@k`, `MRR`, `nDCG`를 추가한다.
4. latency를 분해해서 측정한다.
   - retrieval latency
   - rerank latency
   - generation latency
5. `FAST_MODE`와 full mode를 분리 비교한다.

## Why This Matters

이 노트에서 중요한 건 결과 수치 자체보다, 다음을 보여준다는 점이다.

- 단순 SNS 설계에서 시작했다.
- 검색/추천 문제를 다시 정의했다.
- `Card + Chunk + Relation + Trace` 구조로 재설계했다.
- 실제 로컬 환경에서 검증 가능한 프로토타입까지 만들었다.

즉 이 프로젝트는 단순 구현물이 아니라, 문제 정의와 구조 개선 과정을 보여주는 초기 연구형 포트폴리오로 읽는 것이 맞다.

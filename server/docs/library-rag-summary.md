# Library RAG Summary

원문:

- [Library RAG](https://www.notion.so/3170de9c810b80e198b4ea9f671bfbcc)

이 문서는 Notion의 `Library RAG` 페이지를 Nazgul 서버 기준으로 다시 정리한 요약본이다. 공개 README에는 짧게만 반영하고, 여기에는 실제 설계 판단에 필요한 핵심만 남긴다.

## Why This Exists

`Library RAG`가 풀려는 문제는 단순하다. 기존 chunk-only RAG는 검색은 되지만 운영이 어렵다.

- `Token Explosion`: 중복 chunk 때문에 컨텍스트가 쉽게 비대해진다.
- `Context Fragmentation`: 관련 정보가 흩어져 있어서 LLM이 재구성 비용을 떠안는다.
- `Update Cascade`: 문서 일부만 바뀌어도 전체 재임베딩이 필요해진다.
- `Structural Awareness` 부족: 의존, 반박, 버전 관계를 추적하기 어렵다.

Nazgul에서는 이 문제를 취미 SNS 추천/검색에도 그대로 적용할 수 있다고 본다. 포스트와 댓글이 쌓이면 결국 단순 키워드 검색만으로는 맥락, 관계, 후속 추천을 잘 다루기 어렵기 때문이다.

## Core Proposal

Notion 설계의 중심은 `Hierarchical Graph-based Entity Card Architecture`다.

- `Card`: 고정 크기 의미 단위
- `Chunk`: 검증 가능한 근거 단위
- `Relation Graph`: 카드 간 명시적 연결
- `Trace`: 검색 후보, 선택, 근거, 지연을 기록하는 관찰성 구조

핵심은 검색 단위를 하나로 두지 않는 것이다.

- `Entity Card`: 압축과 라우팅을 위한 단위
- `Evidence Chunk`: grounding과 citation을 위한 단위

즉 최종 컨텍스트는 `Selected Cards + Evidence Snippets`로 구성된다.

## Key Contributions

`Library RAG` 문서 기준으로 중요한 기여는 다섯 가지다.

1. `Entity Card`를 token-bounded retrieval unit으로 정의
2. `Card + Evidence Chunk`의 dual retrieval unit 제안
3. 그래프 확장을 token budget 제약 아래에서 수행
4. 부분 업데이트 가능한 indexing/serving 모델 제안
5. retrieval trace를 통한 운영 가능성 확보

## Retrieval Flow

Notion 문서의 제안 흐름을 Nazgul 실험 기준으로 바꾸면 이렇게 된다.

1. query를 받는다.
2. `HobbyCard` seed 후보를 찾는다.
3. `HobbyCardRelation` 기준으로 1-hop 또는 k-hop 확장한다.
4. 점수와 token budget으로 카드 집합을 고른다.
5. 선택된 카드의 `sourceId`, `hobbyId`, `scopeKey`로 범위를 줄인다.
6. 그 범위 안에서 `ContentChunk`를 찾는다.
7. 카드와 chunk를 함께 answer context로 묶는다.
8. 결과와 지연을 `RecommendationTrace`에 저장한다.

## Nazgul Mapping

Notion 설계를 현재 서버 코드에 대응시키면 다음과 같다.

| Library RAG 개념 | Nazgul 구현 |
|---|---|
| Entity Card | `HobbyCard` |
| Evidence Chunk | `ContentChunk` |
| Graph Edge | `HobbyCardRelation` |
| Retrieval Trace | `RecommendationTrace` |
| Scope-limited retrieval | `hobbyId`, `sourceId`, `scopeKey` 기반 검색 |
| Partial update | 카드/청크 단위 재생성 가능 구조 |

범용 실험 모듈에서는 별도로 아래 엔티티도 유지한다.

| Generic RAG 개념 | `rag` 모듈 구현 |
|---|---|
| Document | `RagDocument` |
| Chapter | `RagChapter` |
| Chunk | `RagChunk` |
| Card | `RagCard` |
| Relation | `RagCardRelation` |
| Query Trace | `RagQueryTrace` |

## Why It Matters For This Project

Nazgul은 원래 취미 기반 SNS였지만, 지금은 `취미 커뮤니티에서 검색과 추천을 더 잘하게 만드는 구조`를 같이 실험하고 있다. 그래서 `Library RAG`는 단순 참고 자료가 아니라 현재 백엔드 구조의 기준 문서다.

이 문서가 실제로 영향을 준 부분은 아래다.

- chunk-only 대신 `card-first retrieval`
- 추천 후보 생성을 위한 `explicit relation`
- 로컬 실험을 위한 `SQLite source-of-truth`
- 실패 분석을 위한 `trace`
- 추후 `pgvector`, `Supabase`, `hybrid search`로 확장 가능한 저장소 분리

## Evaluation Direction

현재 로컬 프로토타입은 `작동 여부`는 확인했지만, `성능이 좋다`고 말할 단계는 아니다. `Library RAG` 기준으로 진짜 평가하려면 최소 아래 항목이 필요하다.

- `Recall@k`
- `MRR`
- `nDCG`
- `token usage`
- `latency breakdown`
- `partial update cost`

또한 현재 seed 데이터는 너무 작기 때문에, 실제 평가를 위해서는 취미별 대량 데이터와 hard negative 세트가 필요하다.

## Open Risks

`Library RAG` 문서가 직접 지적한 한계도 그대로 남아 있다.

- 관계 품질이 낮으면 graph expansion이 오히려 노이즈가 된다.
- entity granularity를 너무 잘게 쪼개면 운영 비용이 커진다.
- 로컬 LLM 환경에서는 rerank와 generation latency가 병목이 된다.
- 작은 데이터셋에서는 구조가 좋아 보여도 실제 품질 차이를 측정하기 어렵다.

## Conclusion

Nazgul 서버의 현재 RAG 실험은 `Library RAG`의 핵심 메시지를 그대로 따른다.

- 검색 단위와 근거 단위를 분리한다.
- 관계를 명시적으로 저장한다.
- token budget을 전제로 retrieval을 설계한다.
- trace를 남겨 운영 가능한 시스템으로 만든다.

그래서 이 프로젝트는 단순한 SNS 백엔드가 아니라, `취미 도메인에서 동작하는 RAG 기반 추천/검색 구조`를 실험하는 서버로 보는 게 맞다.

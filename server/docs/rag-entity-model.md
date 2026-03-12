# RAG Entity Model

## Minimum Entities

| Entity | Role | Why it exists |
|---|---|---|
| `RagDocument` | 원문 문서 단위 | 수집 소스와 버전 기준점 |
| `RagChapter` | 문서 내부 구조 단위 | 검색 범위 축소 |
| `RagChunk` | 실제 근거 텍스트 단위 | citation과 evidence 반환 |
| `RagCard` | 의미 단위 엔티티 | 검색과 추천의 중심 |
| `RagCardRelation` | 카드 간 연결 | 후속 추천과 multi-hop 확장 |
| `RagQueryTrace` | 검색 추적 로그 | 검색 실패 분석과 추천 데이터 축적 |

## Value Objects

- `EvidenceRef`: 카드가 어떤 청크를 근거로 갖는지 표현
- `RetrievalScope`: 카드 검색 후 chunk 검색 범위를 표현

## Implementation Order

1. `RagDocument`
2. `RagChapter`
3. `RagChunk`
4. `RagCard`
5. `RagCardRelation`
6. `RagQueryTrace`

## Search Rule

1. query를 받는다.
2. `RagCard`를 먼저 찾는다.
3. 카드에서 `RetrievalScope`를 만든다.
4. scope 안에서 `RagChunk`를 찾는다.
5. 결과와 trace를 저장한다.

## Recommendation Expansion

추천으로 확장할 때도 `RagCard`와 `RagCardRelation`을 중심으로 간다.

- candidate generation: `RagCardRelation`, tag overlap, vector similarity
- rerank: query/session/user signal
- explanation: `EvidenceRef` 기반 근거 설명

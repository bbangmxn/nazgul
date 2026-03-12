# Nazgul

> 취미 기반 SNS에서 출발해, 개인화된 RAG 검색·추천 구조를 연구하는 프로토타입

[![React](https://img.shields.io/badge/React-18.2-61DAFB?style=flat-square&logo=react&logoColor=white)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.2-3178C6?style=flat-square&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3-003B57?style=flat-square&logo=sqlite&logoColor=white)](https://sqlite.org/)
[![Ollama](https://img.shields.io/badge/Ollama-Local%20LLM-000000?style=flat-square)](https://ollama.com/)

Nazgul은 원래 `취미 기반 SNS`를 목표로 개발한 프로젝트였습니다.  
이후 기존 RAG, 특히 단순 chunk 기반 검색 구조의 한계와 비판을 검토하면서, 도메인과 사용자 맥락을 반영하는 `개인화된 RAG 구조`를 직접 설계하고 검증해보고 싶다는 목적이 생겼습니다.  
현재는 기존 SNS 도메인을 실험 베이스로 삼아, 취미 커뮤니티에서 동작하는 `RAG 검색·추천 프로토타입`을 연구하고 있습니다.

## Portfolio Summary

- 목표: 취미 커뮤니티 도메인에서 `Card + Chunk + Relation + Trace` 구조가 실제로 동작하는지 검증
- 역할: 1인 개발
- 담당: 제품 방향 설정, 프론트엔드, 백엔드, 도메인 설계, 로컬 RAG 실험, 문서화
- 현재 상태: `초기 연구 프로토타입`

이 레포는 두 가지 흐름을 함께 보여줍니다.

- `client`: 취미 기반 SNS MVP 프론트엔드
- `server`: Java/Spring Boot 기반 로컬 RAG 검색·추천 실험 서버

## Why I Changed Direction

초기 SNS 설계만으로는 검색과 추천을 충분히 설명하기 어려웠습니다.  
특히 최근 RAG 논의를 보면서, 단순 chunk 검색만으로는 아래 문제가 반복된다고 판단했습니다.

- `Token Explosion`: 중복 chunk로 컨텍스트가 쉽게 커짐
- `Context Fragmentation`: 관련 정보가 흩어져 LLM이 다시 조합해야 함
- `Update Cascade`: 일부 수정에도 전체 재임베딩이 필요해짐
- `Lack of Structural Awareness`: 관계, 반박, 버전 정보를 추적하기 어려움

그래서 Nazgul은 단순 SNS 구현을 넘어, `취미 SNS 도메인을 기반으로 개인화된 RAG 구조를 연구하는 프로젝트`로 방향을 확장했습니다.

## What I Built

### 1. Hobby-based SNS MVP

- `44개 취미`, `9개 카테고리`
- 취미 교집합 기반 추천 아이디어
- cold start 완화를 고려한 취미 피드 구조
- React + TypeScript 프론트엔드
- Spring Boot + JPA 백엔드 설계

### 2. Local RAG Prototype

- `HobbyCard`: 의미 단위 카드
- `ContentChunk`: 근거 단위 텍스트
- `HobbyCardRelation`: 그래프 확장용 명시적 관계
- `RecommendationTrace`: 검색 실패와 추천 결과 추적 로그
- `SQLite + Ollama` 기반 로컬 실험 환경

즉 이 프로젝트는 `SNS를 만들다가 끝난 프로젝트`가 아니라, `SNS 도메인을 활용해 RAG 구조를 실험하는 프로젝트`입니다.

## Key Design Decisions

- `Card-first retrieval`
  - 먼저 의미 단위 카드를 찾고, 그 다음 근거 chunk를 가져오는 방식으로 retrieval을 분리했습니다.
- `Chunk as grounding`
  - answer는 카드만으로 만들지 않고, 실제 chunk를 근거로 사용하도록 구성했습니다.
- `Explicit relation`
  - relation을 자유로운 추론에 맡기지 않고 명시적 edge로 저장하는 방향을 택했습니다.
- `Trace-first observability`
  - 검색 후보, 결과, 근거, 지연을 기록해 나중에 실패 분석이 가능하도록 했습니다.
- `Swappable storage`
  - 현재는 SQLite로 실험하지만, 이후 PostgreSQL/pgvector로 확장할 수 있게 저장소를 분리했습니다.

관련 문서:

- [Library RAG](https://www.notion.so/3170de9c810b80e198b4ea9f671bfbcc)
- [Entity Card](https://www.notion.so/3140de9c810b80929456ca854974d56c)
- [Technical Design Document](https://www.notion.so/3170de9c810b80daa6e4c159c55f4071)
- [server/docs/library-rag-summary.md](./server/docs/library-rag-summary.md)
- [server/docs/rag-entity-model.md](./server/docs/rag-entity-model.md)
- [server/docs/rag-prototype-research-note.md](./server/docs/rag-prototype-research-note.md)

## Research Snapshot

2026년 3월 12일 기준, 로컬 Windows 환경에서 `SQLite + Ollama(qwen3:8b)` 조합으로 수동 테스트를 진행했습니다.

- 검색 예시: `러닝 초보 루틴`
- 반환 카드: `러닝화 선택 기준`, `무릎 통증 줄이기`, `러닝 입문 루틴`
- 반환 chunk 수: `3`
- 추천 포스트 ID: `1002`, `1003`, `1001`

벤치마크 관측값:

- 모델: `qwen3:8b`
- 임베딩 모델: `nomic-embed-text:latest`
- 총 질의 수: `3`
- 총 지연: `181594 ms`
- 평균 지연: `60531 ms / query`

이 수치는 `완성된 성능 지표`가 아니라 `초기 기술 검증에서 확보한 관측값`입니다.  
즉 지금 단계의 핵심은 “성능이 매우 좋다”가 아니라, `Card -> Chunk -> Answer -> Trace` 파이프라인이 실제로 연결된다는 점을 확인했다는 데 있습니다.

## Screenshots

초기 취미 SNS UI는 아래처럼 구성했습니다.

<p align="center">
  <img src="./docs/images/nazgul-login.png" alt="Nazgul 로그인" width="30%">
  <img src="./docs/images/nazgul-home.jpg" alt="Nazgul 홈 피드" width="30%">
  <img src="./docs/images/nazgul-profile.png" alt="Nazgul 프로필" width="30%">
</p>

<p align="center">
  <img src="./docs/images/nazgul-explore.png" alt="Nazgul 탐색" width="45%">
  <img src="./docs/images/nazgul-hobbies.png" alt="Nazgul 취미 선택" width="45%">
</p>

## Repository Layout

```text
Nazgul/
├── client/   # 취미 SNS 프론트엔드
├── server/   # Java/Spring Boot 기반 RAG 실험 서버
└── docs/     # README 이미지 및 보조 자산
```

## Local Run

### Client

```bash
cd client
npm install
npm run dev
```

### Server

```powershell
cd server
scripts\start-hobby-rag-sqlite.cmd -ChatModel "qwen3:8b"
```

검색 테스트:

```powershell
scripts\test-hobby-rag.cmd -HobbyId 10 -Query "러닝 초보 루틴" -TimeoutSec 300
```

벤치마크:

```powershell
scripts\test-hobby-rag.cmd -HobbyId 10 -Benchmark -TimeoutSec 300
```

## What This Project Shows

- 도메인 문제를 다시 정의하는 능력
- MVP 이후 구조를 다시 설계하는 능력
- `SNS -> RAG prototype`으로 문제를 확장하는 사고 과정
- Java/Spring 기반 헥사고날 백엔드 설계
- 로컬 AI 실험 환경을 실제로 붙여보는 구현력

## Current Limitations

- 데이터셋이 아직 작음
- 정량 평가 지표(`Recall@k`, `MRR`, `nDCG`)가 아직 충분하지 않음
- 복합 질의 랭킹이 안정적이지 않음
- 병목이 로컬 Ollama 응답 시간에 크게 좌우됨

즉 이 프로젝트는 `완성된 제품`보다는, 문제 정의와 구조 설계, 실험 과정을 보여주는 포트폴리오 프로젝트로 보는 것이 맞습니다.

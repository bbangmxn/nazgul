# Nazgul

> 취미 기반 SNS에서 출발해, 로컬 RAG 추천 실험으로 확장 중인 개인 프로젝트

[![React](https://img.shields.io/badge/React-18.2-61DAFB?style=flat-square&logo=react&logoColor=white)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.2-3178C6?style=flat-square&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3-003B57?style=flat-square&logo=sqlite&logoColor=white)](https://sqlite.org/)
[![Ollama](https://img.shields.io/badge/Ollama-Local%20LLM-000000?style=flat-square)](https://ollama.com/)

Nazgul은 원래 `취미 기반 소셜 매칭 플랫폼`으로 설계한 프로젝트입니다.  
현재는 그 방향을 확장해, `취미 커뮤니티에서 동작하는 로컬 RAG 검색/추천 엔진`을 함께 연구하고 있습니다.

즉 이 레포는 두 가지를 함께 보여줍니다.

- `client`: 원래 설계했던 취미 SNS 프론트엔드
- `server`: 헥사고날 구조로 재정리한 Java/Spring Boot 기반 RAG 실험 서버

## What This Repo Shows

- 취미 기반 제품을 처음에는 `소셜 매칭` 문제로 풀었던 설계
- 이후 추천/검색을 더 고도화하기 위해 `RAG + Entity Card` 구조로 확장한 백엔드 실험
- 로컬 환경에서 `SQLite + Ollama`로 바로 검증 가능한 프로토타입
- 기능 구현보다 `구조 실험`, `검색 흐름`, `trace`, `로컬 실행 가능성`에 초점을 둔 과정

## Project Evolution

### 1. Hobby-based social matching

초기 Nazgul은 공통 취미를 가진 사람을 연결하는 SNS였습니다.

- `44개 취미`, `9개 카테고리`
- 취미 교집합 기반 추천
- cold start 완화를 위한 취미 피드
- React + TypeScript 프론트엔드
- Spring Boot + JPA 백엔드

이 단계의 결과물은 지금도 `client`와 기존 설계 방향에서 확인할 수 있습니다.

### 2. Local RAG recommendation prototype

현재는 “취미 커뮤니티에서 검색과 추천을 더 잘하려면 어떤 RAG 구조가 맞는가?”를 실험하고 있습니다.

핵심 질문:

- chunk-only 검색보다 `card-first retrieval`이 더 좋은가
- 취미 도메인에서 `Entity Card`가 추천의 중심 노드가 될 수 있는가
- 로컬 PC에서도 `SQLite + Ollama`로 최소한의 검증이 가능한가

## Current Focus

현재 공개 기준으로 더 중요한 코드는 `server` 쪽입니다.

- `ContentChunk`
- `HobbyCard`
- `HobbyCardRelation`
- `RecommendationTrace`

이 구조로 취미 커뮤니티용 검색/추천을 실험하고 있습니다.

Notion에서 정리한 설계 문서를 기준으로 구현 중입니다.

- [Library RAG](https://www.notion.so/3170de9c810b80e198b4ea9f671bfbcc)
- [Entity Card](https://www.notion.so/3140de9c810b80929456ca854974d56c)
- [Technical Design Document](https://www.notion.so/3170de9c810b80daa6e4c159c55f4071)

핵심 원칙:

- `Card`는 의미 단위
- `Chunk`는 근거 단위
- chunk retrieval은 반드시 scope를 좁힌 뒤 수행
- trace를 남겨 retrieval 실패를 분석
- 저장소는 교체 가능해야 함

`Library RAG` 페이지에서 가져온 핵심 문제 정의는 이렇습니다.

- `Token Explosion`: 중복 chunk로 컨텍스트가 비대해짐
- `Context Fragmentation`: 관련 정보가 흩어져 LLM이 다시 조합해야 함
- `Update Cascade`: 일부 수정에도 전체 재임베딩이 필요해짐
- `Lack of Structural Awareness`: 문서 간 관계, 반박, 버전 정보를 추적하기 어려움

Nazgul의 현재 실험은 이 문제를 `Card + Chunk + Relation + Trace` 구조로 풀어보는 방향입니다.

- `HobbyCard`: 의미 단위 카드
- `ContentChunk`: 근거 단위 텍스트
- `HobbyCardRelation`: 그래프 확장용 명시적 관계
- `RecommendationTrace`: 검색 실패와 추천 결과를 추적하는 로그

정리 문서:

- [server/docs/library-rag-summary.md](./server/docs/library-rag-summary.md)
- [server/docs/rag-entity-model.md](./server/docs/rag-entity-model.md)
- [server/docs/local-rag-evaluation-report.md](./server/docs/local-rag-evaluation-report.md)

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
├── client/   # 원래 설계했던 취미 SNS 프론트엔드
├── server/   # 현재 실험 중인 Java/Spring RAG 백엔드
└── docs/     # README 이미지 및 보조 자산
```

## Server Highlights

`server`는 현재 헥사고날 구조로 재정리되어 있습니다.

- `common`: 공통 예외 처리
- `rag`: 범용 card/chunk/query-trace 실험 모듈
- `optimization`: 취미 SNS용 검색/추천 실험 모듈

로컬 테스트 스택:

- Java 17
- Spring Boot 3.5
- SQLite
- Ollama
- Spring Data JPA

상세 문서:

- [server/README.md](./server/README.md)
- [server/docs/optimization-rag-quickstart.md](./server/docs/optimization-rag-quickstart.md)
- [server/docs/rag-entity-model.md](./server/docs/rag-entity-model.md)

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

## Latest Local Test Result

2026년 3월 12일 기준, 로컬 Windows 환경에서 `SQLite + Ollama(qwen3:8b)` 조합으로 수동 테스트를 진행했습니다.

- 검색 예시: `러닝 초보 루틴`
- 반환 카드: `러닝화 선택 기준`, `무릎 통증 줄이기`, `러닝 입문 루틴`
- 반환 chunk 수: `3`
- 추천 포스트 ID: `1002`, `1003`, `1001`

벤치마크 기준:

- 모델: `qwen3:8b`
- 임베딩 모델: `nomic-embed-text:latest`
- 총 질의 수: `3`
- 총 지연: `181594 ms`
- 평균 지연: `60531 ms / query`

이 결과는 `파이프라인이 실제로 동작한다`는 증거로는 충분하지만, `성능이 우수하다`는 의미는 아닙니다. 현재는 seed 데이터가 작고, 복합 질의에서 `러닝 입문 루틴`이 1순위가 아니라는 점 때문에 랭킹 품질 개선이 여전히 필요합니다.

자세한 해석:

- [server/docs/local-rag-evaluation-report.md](./server/docs/local-rag-evaluation-report.md)

## What Is Ready

- 취미 SNS 프론트엔드 화면 구성
- Spring Boot 서버의 헥사고날 재구성
- SQLite 기반 로컬 RAG 테스트
- Ollama 연동
- card-first retrieval 실험
- recommendation trace 저장

## What Is Still In Progress

- 대규모 테스트 데이터셋
- 정량 평가 지표
  - `Recall@k`
  - `MRR`
  - `nDCG`
- retrieval-only / rerank / generation 분리 벤치마크
- 원본 SNS 엔티티에서 자동 ingest
- 로컬 저사양 환경 최적화

## Why This Is In The Portfolio

이 프로젝트는 단순히 기능 몇 개를 붙인 SNS가 아니라,

- 제품 문제를 정의하고
- 초기 MVP를 만들고
- 한계를 발견한 뒤
- 구조를 다시 설계하고
- 검색/추천 엔진으로 확장해보는 과정

을 보여주는 작업입니다.

즉 결과물 하나보다 `문제를 어떻게 다시 정의했고, 구조를 어떻게 바꿨는지`를 보여주는 프로젝트로 보는 게 맞습니다.

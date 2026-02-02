<!-- PROJECT SHIELDS -->
<div align="center">

[![React][react-shield]][react-url]
[![TypeScript][ts-shield]][ts-url]
[![Tailwind][tailwind-shield]][tailwind-url]
[![License][license-shield]][license-url]

</div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/BbangMxn/Nazgul_client">
    <img src="public/favicon.svg" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Nazgul Client</h3>

  <p align="center">
    취미 기반 소셜 매칭 플랫폼 프론트엔드
    <br />
    <strong>Threads + Notion 스타일 UI</strong>
    <br />
    <br />
    <a href="#-스크린샷">스크린샷</a>
    ·
    <a href="#-빠른-시작">빠른 시작</a>
    ·
    <a href="https://github.com/BbangMxn/Nazgul_client/issues">버그 리포트</a>
  </p>
</div>

<!-- ABOUT -->
## 프로젝트 소개

**Nazgul Client**는 취미 기반 소셜 매칭 플랫폼의 React 프론트엔드입니다.

- 🎨 **Threads 스타일** — 미니멀하고 깔끔한 UI
- 📱 **반응형** — 모바일/데스크톱 최적화
- ⚡ **빠른 UX** — Optimistic UI, 부드러운 애니메이션
- 🌙 **다크 모드** — 시스템 설정 연동 (준비중)

### 기술 스택

[![React][react-badge]][react-url]
[![TypeScript][ts-badge]][ts-url]
[![Tailwind][tailwind-badge]][tailwind-url]
[![Vite][vite-badge]][vite-url]
[![Zustand][zustand-badge]][zustand-url]

---

## 📱 스크린샷

| 홈 피드 | 취미 선택 | 프로필 |
|:-------:|:--------:|:------:|
| ![Home](docs/home.png) | ![Hobbies](docs/hobbies.png) | ![Profile](docs/profile.png) |

---

## ✨ 주요 기능

| 기능 | 설명 |
|-----|------|
| **인증** | 회원가입, 로그인 (JWT) |
| **피드** | 팔로우/취미 기반 게시물 |
| **취미** | 44개 취미, 9개 카테고리 |
| **게시물** | 작성, 좋아요, 댓글 |
| **프로필** | 수정, 팔로우/팔로워 |
| **탐색** | 인기/추천 게시물, 사용자 검색 |

---

## 🎨 디자인 시스템

### Threads 스타일 영감

```css
/* 깔끔한 카드 */
.card {
  border-bottom: 1px solid var(--border);
  transition: background 0.2s;
}

/* 미니멀 버튼 */
.btn-primary {
  background: black;
  border-radius: 12px;
}

/* 부드러운 애니메이션 */
.animate-fade-in {
  animation: fadeIn 0.2s ease-out;
}
```

### 색상 팔레트

| 용도 | 라이트 | 다크 |
|------|--------|------|
| Primary | `#000000` | `#FFFFFF` |
| Secondary | `#737373` | `#A3A3A3` |
| Background | `#FFFFFF` | `#101010` |
| Surface | `#F5F5F5` | `#1A1A1A` |
| Accent | `#0095F6` | `#0095F6` |

---

## 🚀 빠른 시작

```bash
# 1. 클론
git clone https://github.com/BbangMxn/Nazgul_client.git
cd Nazgul_client

# 2. 의존성 설치
npm install

# 3. 개발 서버
npm run dev

# http://localhost:3000
```

### 백엔드 연동

```bash
# Nazgul_server 실행 (포트 8080)
cd ../Nazgul_server
./gradlew bootRun
```

> Vite가 `/api` 요청을 `localhost:8080`으로 프록시합니다.

---

## 📂 프로젝트 구조

```
src/
├── components/        # 공통 컴포넌트
│   ├── Avatar.tsx
│   ├── Layout.tsx
│   ├── PostCard.tsx
│   ├── PostComposer.tsx
│   └── ...
├── pages/             # 페이지
│   ├── HomePage.tsx
│   ├── ExplorePage.tsx
│   ├── HobbiesPage.tsx
│   ├── ProfilePage.tsx
│   └── ...
├── stores/            # Zustand 상태
│   └── authStore.ts
├── lib/               # API, 유틸
│   ├── api.ts
│   └── utils.ts
├── types/             # TypeScript 타입
├── App.tsx
├── main.tsx
└── index.css          # Tailwind + 커스텀 스타일
```

---

## 📜 스크립트

```bash
npm run dev      # 개발 서버
npm run build    # 프로덕션 빌드
npm run preview  # 빌드 프리뷰
npm run lint     # ESLint
```

---

## 🧩 컴포넌트

### PostCard

```tsx
<PostCard
  post={post}
  onUpdate={(updatedPost) => updatePostInList(updatedPost)}
/>
```

### HobbyChip

```tsx
<HobbyChip
  icon="⚽"
  name="축구"
  active={isSelected}
  onClick={() => toggleHobby()}
/>
```

### Avatar

```tsx
<Avatar
  src={user.profileImage}
  name={user.nickname}
  size="lg"  // sm | md | lg | xl
/>
```

---

## 🗺️ 로드맵

- [x] 인증 (로그인/회원가입)
- [x] 홈 피드
- [x] 취미 선택/관리
- [x] 게시물 CRUD
- [x] 좋아요/댓글
- [x] 프로필 페이지
- [x] 탐색 페이지
- [ ] 다크 모드 토글
- [ ] 이미지 업로드
- [ ] 실시간 알림
- [ ] PWA 지원

---

## 📄 라이선스

MIT License - [LICENSE](LICENSE)

---

<div align="center">
  
**[⬆ 맨 위로](#nazgul-client)**

</div>

<!-- MARKDOWN LINKS -->
[react-shield]: https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black
[react-url]: https://react.dev/
[ts-shield]: https://img.shields.io/badge/TypeScript-5-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[ts-url]: https://www.typescriptlang.org/
[tailwind-shield]: https://img.shields.io/badge/Tailwind-3-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white
[tailwind-url]: https://tailwindcss.com/
[license-shield]: https://img.shields.io/badge/License-MIT-blue?style=for-the-badge
[license-url]: LICENSE

[react-badge]: https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black
[ts-badge]: https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[tailwind-badge]: https://img.shields.io/badge/Tailwind-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white
[vite-badge]: https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white
[vite-url]: https://vitejs.dev/
[zustand-badge]: https://img.shields.io/badge/Zustand-443E38?style=for-the-badge
[zustand-url]: https://zustand-demo.pmnd.rs/

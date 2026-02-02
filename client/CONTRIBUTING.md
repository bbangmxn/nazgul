# Contributing to Nazgul Client

기여에 관심을 가져주셔서 감사합니다! 🎉

## 개발 환경

```bash
# 1. 포크 후 클론
git clone https://github.com/<your-username>/Nazgul_client.git
cd Nazgul_client

# 2. 의존성 설치
yarn install

# 3. 환경변수 설정
cp .env.example .env

# 4. 개발 서버 실행
yarn start
```

## 커밋 컨벤션

```
feat: 새로운 기능
fix: 버그 수정
docs: 문서 수정
style: 스타일 변경 (CSS, 포맷팅)
refactor: 리팩토링
test: 테스트 추가/수정
chore: 빌드/설정 변경
```

## Pull Request

1. 이슈 생성
2. 브랜치 생성 (`feat/amazing-feature`)
3. 린트 확인 (`yarn lint`)
4. PR 생성

## 코드 스타일

- ESLint + Prettier
- 함수형 컴포넌트 선호
- Hooks 적극 활용

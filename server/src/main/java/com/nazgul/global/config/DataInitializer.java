package com.nazgul.global.config;

import com.nazgul.domain.hobby.entity.Hobby;
import com.nazgul.domain.hobby.repository.HobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HobbyRepository hobbyRepository;

    @Override
    public void run(String... args) {
        if (hobbyRepository.count() == 0) {
            List<Hobby> hobbies = List.of(
                // 스포츠
                Hobby.builder().name("축구").description("팀 스포츠의 대표").icon("⚽").category("SPORTS").build(),
                Hobby.builder().name("농구").description("실내외 모두 가능한 스포츠").icon("🏀").category("SPORTS").build(),
                Hobby.builder().name("테니스").description("라켓 스포츠").icon("🎾").category("SPORTS").build(),
                Hobby.builder().name("수영").description("수상 스포츠").icon("🏊").category("SPORTS").build(),
                Hobby.builder().name("러닝").description("달리기, 마라톤").icon("🏃").category("SPORTS").build(),
                Hobby.builder().name("헬스").description("웨이트 트레이닝").icon("💪").category("SPORTS").build(),
                Hobby.builder().name("요가").description("심신 수련").icon("🧘").category("SPORTS").build(),
                Hobby.builder().name("클라이밍").description("암벽 등반").icon("🧗").category("SPORTS").build(),
                
                // 음악
                Hobby.builder().name("기타").description("어쿠스틱, 일렉기타").icon("🎸").category("MUSIC").build(),
                Hobby.builder().name("피아노").description("건반 악기").icon("🎹").category("MUSIC").build(),
                Hobby.builder().name("드럼").description("타악기").icon("🥁").category("MUSIC").build(),
                Hobby.builder().name("노래").description("보컬, 성악").icon("🎤").category("MUSIC").build(),
                Hobby.builder().name("DJ").description("디제잉, 믹싱").icon("🎧").category("MUSIC").build(),
                
                // 예술
                Hobby.builder().name("그림").description("드로잉, 페인팅").icon("🎨").category("ARTS").build(),
                Hobby.builder().name("사진").description("포토그래피").icon("📷").category("ARTS").build(),
                Hobby.builder().name("영상").description("영상 제작, 유튜브").icon("🎬").category("ARTS").build(),
                Hobby.builder().name("공예").description("수공예, DIY").icon("🧵").category("ARTS").build(),
                Hobby.builder().name("서예").description("붓글씨").icon("✒️").category("ARTS").build(),
                
                // 게임
                Hobby.builder().name("PC게임").description("컴퓨터 게임").icon("🖥️").category("GAMES").build(),
                Hobby.builder().name("콘솔게임").description("플스, 닌텐도, Xbox").icon("🎮").category("GAMES").build(),
                Hobby.builder().name("보드게임").description("테이블탑 게임").icon("🎲").category("GAMES").build(),
                Hobby.builder().name("카드게임").description("TCG, 포커").icon("🃏").category("GAMES").build(),
                
                // 아웃도어
                Hobby.builder().name("등산").description("산행, 트레킹").icon("🏔️").category("OUTDOOR").build(),
                Hobby.builder().name("캠핑").description("야외 캠핑").icon("🏕️").category("OUTDOOR").build(),
                Hobby.builder().name("낚시").description("민물, 바다 낚시").icon("🎣").category("OUTDOOR").build(),
                Hobby.builder().name("자전거").description("사이클링").icon("🚴").category("OUTDOOR").build(),
                Hobby.builder().name("여행").description("국내외 여행").icon("✈️").category("OUTDOOR").build(),
                
                // 문화
                Hobby.builder().name("독서").description("책 읽기").icon("📚").category("CULTURE").build(),
                Hobby.builder().name("영화").description("영화 감상").icon("🎥").category("CULTURE").build(),
                Hobby.builder().name("연극/뮤지컬").description("공연 관람").icon("🎭").category("CULTURE").build(),
                Hobby.builder().name("전시회").description("미술관, 박물관").icon("🖼️").category("CULTURE").build(),
                
                // 음식
                Hobby.builder().name("요리").description("홈쿠킹").icon("👨‍🍳").category("FOOD").build(),
                Hobby.builder().name("베이킹").description("제빵, 제과").icon("🍰").category("FOOD").build(),
                Hobby.builder().name("커피").description("홈카페, 바리스타").icon("☕").category("FOOD").build(),
                Hobby.builder().name("와인").description("와인 테이스팅").icon("🍷").category("FOOD").build(),
                Hobby.builder().name("맛집탐방").description("맛집 투어").icon("🍽️").category("FOOD").build(),
                
                // 테크
                Hobby.builder().name("코딩").description("프로그래밍").icon("💻").category("TECH").build(),
                Hobby.builder().name("3D프린팅").description("3D 모델링, 출력").icon("🖨️").category("TECH").build(),
                Hobby.builder().name("드론").description("드론 비행, 촬영").icon("🚁").category("TECH").build(),
                Hobby.builder().name("IoT").description("스마트홈, 아두이노").icon("🔌").category("TECH").build(),
                
                // 반려동물
                Hobby.builder().name("강아지").description("반려견").icon("🐕").category("PETS").build(),
                Hobby.builder().name("고양이").description("반려묘").icon("🐈").category("PETS").build(),
                Hobby.builder().name("수족관").description("아쿠아리움").icon("🐠").category("PETS").build(),
                Hobby.builder().name("식물").description("가드닝, 플랜테리어").icon("🌱").category("PETS").build()
            );
            
            hobbyRepository.saveAll(hobbies);
        }
    }
}

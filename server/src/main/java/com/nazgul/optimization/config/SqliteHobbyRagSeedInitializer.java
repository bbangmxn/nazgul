package com.nazgul.optimization.config;

import com.nazgul.optimization.application.port.in.IngestHobbyContentCommand;
import com.nazgul.optimization.application.port.in.IngestHobbyContentResult;
import com.nazgul.optimization.application.port.in.IngestHobbyContentUseCase;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationCommand;
import com.nazgul.optimization.application.port.in.LinkHobbyCardRelationUseCase;
import com.nazgul.optimization.application.port.out.HobbyCardStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("sqlite")
@Component
@RequiredArgsConstructor
public class SqliteHobbyRagSeedInitializer implements ApplicationRunner {

    private final HobbyCardStore hobbyCardStore;
    private final IngestHobbyContentUseCase ingestHobbyContentUseCase;
    private final LinkHobbyCardRelationUseCase linkHobbyCardRelationUseCase;

    @Value("${app.rag.seed-enabled:true}")
    private boolean seedEnabled;

    @Override
    public void run(ApplicationArguments args) {
        if (!seedEnabled || !hobbyCardStore.search("", null, 1).isEmpty()) {
            return;
        }

        IngestHobbyContentResult runningBasics = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 1001L, 10L, "TOPIC", "러닝 입문 루틴",
                "러닝 초보자는 주 3회, 20분 내외로 시작하는 것이 좋다. 회복일을 반드시 두고 심박수를 천천히 올려야 부상 위험이 줄어든다.",
                List.of("running", "beginner", "routine"), "public"
        ));
        IngestHobbyContentResult runningShoes = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 1002L, 10L, "TIP", "러닝화 선택 기준",
                "러닝화는 쿠션감보다 발볼과 착지 습관이 더 중요하다. 초보자는 장거리용 한 켤레보다 범용 트레이닝화를 먼저 고르는 편이 안정적이다.",
                List.of("running", "shoes"), "public"
        ));
        IngestHobbyContentResult runningPain = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 1003L, 10L, "TROUBLESHOOT", "무릎 통증 줄이기",
                "러닝 후 무릎 통증이 계속되면 거리보다 케이던스와 회복을 먼저 점검해야 한다. 통증이 누적되면 강도를 낮추고 하체 보강운동을 함께 해야 한다.",
                List.of("running", "recovery", "injury"), "public"
        ));

        IngestHobbyContentResult coffeeBeans = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 2001L, 20L, "TOPIC", "홈카페 원두 고르기",
                "홈카페 입문자는 산미가 약하고 밸런스가 좋은 블렌드 원두부터 시작하는 편이 실패 확률이 낮다. 추출 변수보다 원두 신선도가 맛에 더 큰 영향을 준다.",
                List.of("coffee", "beans"), "public"
        ));
        IngestHobbyContentResult coffeeRatio = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 2002L, 20L, "HOWTO", "브루잉 비율 기본",
                "핸드드립 기본 비율은 1대15에서 1대16 사이가 안정적이다. 물줄기보다 분쇄도와 총 추출 시간이 먼저 맞아야 맛이 흔들리지 않는다.",
                List.of("coffee", "brew", "ratio"), "public"
        ));
        IngestHobbyContentResult coffeeGrinder = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 2003L, 20L, "TIP", "그라인더 업그레이드 기준",
                "홈카페에서 추출 일관성을 올리려면 드리퍼보다 그라인더 품질을 먼저 점검해야 한다. 미분이 많으면 쓴맛과 탁도가 함께 올라온다.",
                List.of("coffee", "grinder"), "public"
        ));

        IngestHobbyContentResult campingShelter = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 3001L, 30L, "TOPIC", "초보 캠핑 장비",
                "캠핑 입문자는 타프보다 설치가 쉬운 쉘터형 텐트부터 준비하는 편이 낫다. 장비 수를 줄여야 세팅과 철수가 빨라지고 피로가 줄어든다.",
                List.of("camping", "beginner", "gear"), "public"
        ));
        IngestHobbyContentResult campingCook = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 3002L, 30L, "TIP", "캠핑 조리 동선",
                "캠핑 조리는 화력보다 동선이 중요하다. 화로, 식재료, 세척 구역이 섞이면 준비 시간이 길어지고 안전 문제도 생긴다.",
                List.of("camping", "cooking"), "public"
        ));
        IngestHobbyContentResult campingRain = ingestHobbyContentUseCase.ingest(new IngestHobbyContentCommand(
                "POST", 3003L, 30L, "TROUBLESHOOT", "우천 캠핑 대비",
                "비 예보가 있으면 방수보다 배수 동선과 지면 상태를 먼저 확인해야 한다. 젖은 장비는 귀가 후 바로 건조하지 않으면 냄새와 곰팡이가 생긴다.",
                List.of("camping", "rain"), "public"
        ));

        link(runningBasics, runningShoes);
        link(runningBasics, runningPain);
        link(coffeeBeans, coffeeRatio);
        link(coffeeRatio, coffeeGrinder);
        link(campingShelter, campingCook);
        link(campingShelter, campingRain);
    }

    private void link(IngestHobbyContentResult source, IngestHobbyContentResult target) {
        linkHobbyCardRelationUseCase.link(new LinkHobbyCardRelationCommand(
                source.card().getId(),
                target.card().getId(),
                "RELATED_TO",
                0.9d,
                target.chunks().get(0).getId()
        ));
    }
}

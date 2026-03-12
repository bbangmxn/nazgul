package com.nazgul.optimization.adapter.out.ai;

import com.nazgul.optimization.application.port.out.OptimizationPlanningPort;
import com.nazgul.optimization.config.OptimizationProperties;
import com.nazgul.optimization.domain.model.OptimizationJob;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class SpringAiOptimizationPlanningAdapter implements OptimizationPlanningPort {

    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;
    private final OptimizationProperties optimizationProperties;

    @Value("${spring.ai.openai.api-key:}")
    private String openAiApiKey;

    @Override
    public String buildPlan(OptimizationJob job) {
        if (!StringUtils.hasText(openAiApiKey)) {
            return "OPENAI_API_KEY가 없어 AI 계획 생성을 건너뛰었습니다.";
        }

        ChatClient.Builder chatClientBuilder = chatClientBuilderProvider.getIfAvailable();
        if (chatClientBuilder == null) {
            return "Spring AI ChatClient를 사용할 수 없어 AI 계획 생성을 건너뛰었습니다.";
        }

        String prompt = optimizationProperties.promptTemplate()
                .replace("{{title}}", safe(job.getTitle()))
                .replace("{{objective}}", safe(job.getObjective()))
                .replace("{{inputSummary}}", safe(job.getInputSummary()))
                .replace("{{inputPayload}}", safe(job.getInputPayload()));

        String content = chatClientBuilder.build()
                .prompt()
                .user(prompt)
                .call()
                .content();

        if (!StringUtils.hasText(content)) {
            return "LLM이 비어 있는 계획 응답을 반환했습니다.";
        }

        return content;
    }

    private String safe(String value) {
        return StringUtils.hasText(value) ? value : "N/A";
    }
}

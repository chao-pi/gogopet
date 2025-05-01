package com.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class DeepSeekUtil {
    private static final Logger logger = LoggerFactory.getLogger(DeepSeekUtil.class);

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getResponse(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", message));
            requestBody.put("messages", messages);

            logger.info("Sending request to DeepSeek API: {}", requestBody);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);

            logger.info("Received response from DeepSeek API: {}", response);

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
                    return (String) messageResponse.get("content");
                }
            }
            return "抱歉，我无法处理您的请求。";
        } catch (Exception e) {
            logger.error("Error while getting response from DeepSeek API", e);
            return "抱歉，处理您的请求时出现错误。";
        }
    }

    public void getStreamResponse(String message, Consumer<String> chunkConsumer) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("stream", true);
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", message));
            requestBody.put("messages", messages);

            logger.info("Sending stream request to DeepSeek API: {}", requestBody);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            restTemplate.execute(apiUrl, org.springframework.http.HttpMethod.POST, 
                requestCallback -> {
                    requestCallback.getHeaders().setAll(headers.toSingleValueMap());
                    requestCallback.getBody().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsBytes(requestBody));
                },
                responseExtractor -> {
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(responseExtractor.getBody()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String jsonStr = line.substring(6);
                                if (jsonStr.equals("[DONE]")) {
                                    break;
                                }
                                Map<String, Object> response = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readValue(jsonStr, Map.class);
                                if (response.containsKey("choices")) {
                                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                                    if (!choices.isEmpty()) {
                                        Map<String, Object> choice = choices.get(0);
                                        Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                                        if (delta.containsKey("content")) {
                                            String content = (String) delta.get("content");
                                            chunkConsumer.accept(content);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return null;
                });
        } catch (Exception e) {
            logger.error("Error while getting stream response from DeepSeek API", e);
            chunkConsumer.accept("抱歉，处理您的请求时出现错误。");
        }
    }
} 
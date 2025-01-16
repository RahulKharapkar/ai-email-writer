package com.rk.ai_email_writer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGenService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private  String apiUrl;
    @Value("${gemini.api.key}")
    private  String apiKey ;

    public EmailGenService(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().build();
    }

    public String generateEmail(EmailRequest emailRequest) {
//        Build the prompt
        String prompt = buildPrompt(emailRequest);
//       Craft a Request
        Map<String, Object> requestBody = Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text",prompt)
                })
        });
// make the request and get the response

        String response = webClient.post()
                .uri(apiUrl+apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return responseContent(response);
    }

    private String responseContent(String response){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .get("text")
                    .asText();
        }catch (Exception e){
            throw new RuntimeException("Error processing response", e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(" Generate a Professional email reply for the following email. Please dont generate a subject line.");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append(" The tone of the email should be " + emailRequest.getTone());
        }
        prompt.append(" Email: " + emailRequest.getEmailContent());
        return prompt.toString();
    }
}

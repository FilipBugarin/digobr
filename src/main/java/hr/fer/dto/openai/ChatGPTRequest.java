package hr.fer.dto.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String userPrompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", userPrompt));
    }

    public ChatGPTRequest(String model, String userPrompt, String systemPrompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", systemPrompt));
        this.messages.add(new Message("user", userPrompt));
    }
}

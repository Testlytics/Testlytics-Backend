package com.example.Testlytics.DTO;
 
import lombok.*;
 
import java.util.List;
import java.util.UUID;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private UUID questionId;
    private UUID testId;
    private String questionText;
    private String answer;
    private List<OptionDTO> options;
    private String imageBase64; // Stores the image as a Base64-encoded string
 
    public QuestionDTO(UUID questionId, UUID testId, String questionText, String answer, List<OptionDTO> options, byte[] image) {
    }
}
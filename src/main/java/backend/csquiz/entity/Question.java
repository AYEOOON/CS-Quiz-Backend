package backend.csquiz.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options;  // 문제에 대한 옵션 리스트 (단순 문자열)

    @Column(nullable = false)
    private String answer;

    private String difficulty; // 난이도: easy, medium, hard
}
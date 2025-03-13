package backend.csquiz.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gameId; // UUID로 생성

    @Column(nullable = false)
    private String nickname;

    private int score = 0; // 초기 점수 설정

    private boolean isFinished = false; // 게임 종료 여부

    @ElementCollection // 별도 테이블 없이 리스트 형태로 저장
    private List<Long> questionIds;  // 출제된 질문들의 ID 목록

    // 새로운 게임을 생성할 때 사용할 생성자 추가
    public Game(String nickname, List<Long> questionIds) {
        this.gameId = UUID.randomUUID().toString();
        this.nickname = nickname;
        this.score = 0;
        this.questionIds = questionIds;
        this.isFinished = false;
    }

    public void finish() {
        this.isFinished = true;
    }
}

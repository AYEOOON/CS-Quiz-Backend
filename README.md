# cs-quiz-backend
## 개발 환경
- **백엔드**: Java 21, Spring Boot 3.4.2
- **데이터베이스**: MySQL, Redis
- **빌드 도구**: Gradle
- **개발 도구**: IntelliJ IDEA Community Edition 2023.3.2
- **버전 관리**: Git, GitHub
  
## 데이터베이스 관계도

### 테이블 구조
**Game**  
- id: 게임 고유 ID  
- nickname: 사용자의 닉네임  
- difficulty: 게임 난이도  
- score: 최종 점수  

**Question**  
- id: 문제 고유 ID
- question: 문제 내용
- options: 선택지
- answer: 정답
- difficulty: 난이도
  
**User**  
- id: 사용자 고유 ID
- nickname: 사용자 닉네임
- score: 점수

![image](https://github.com/user-attachments/assets/90889767-bc7a-417b-a8e4-2e985c961834)

## ERD
**1. Game**  
- 게임을 나타내는 엔티티로, 고유한 gameId와 nickname, score를 관리합니다.
- isFinished 필드로 게임의 종료 여부를 체크하며, 출제된 문제들의 ID 목록을 questionIds로 저장합니다.

**2. Question**  
- 퀴즈 문제를 나타내는 엔티티입니다.
- question 필드는 문제의 내용을, options는 선택지들을 저장하고, answer는 정답을, difficulty는 문제의 난이도를 나타냅니다.

**3. User**  
- 사용자를 나타내는 엔티티로, nickname과 score를 저장합니다.
- 각 사용자는 여러 게임을 진행할 수 있습니다.

![image](https://github.com/user-attachments/assets/24537b29-e706-467e-a857-f1127d49a60c)

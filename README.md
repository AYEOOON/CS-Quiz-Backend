# cs-quiz-backend
## Stack
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/caddy-1F88C0?style=for-the-badge&logo=caddy&logoColor=white">
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">  

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

![image](https://github.com/user-attachments/assets/efb617ba-6b58-45b5-ae00-42f86654a5da)


## 아키텍처 구조
이 프로젝트는 AWS EC2 인스턴스에서 실행되며, 아래와 같은 주요 구성 요소로 이루어져 있습니다.  

**주요 기술 스택**  
- 애플리케이션 서버: Spring Boot (Java)
- 데이터베이스: MySQL (EC2 내부에서 실행)
- 캐시 서버: Redis (EC2 내부에서 실행)
- Reverse Proxy & SSL: Caddy (Let's Encrypt SSL 인증서 자동화)
- 배포 환경: AWS EC2 (Ubuntu)

**서버 아키텍처 흐름**  
- 클라이언트 → Caddy (Reverse Proxy & SSL)
- Caddy → Spring Boot 애플리케이션 서버
- Spring Boot → Redis (캐싱)
- Spring Boot → MySQL (데이터 저장)
- MySQL & Redis: EC2 내부에서 실행

![image](https://github.com/user-attachments/assets/3024ed55-ee57-44e4-9229-2318adfc6fea)


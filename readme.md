### 카테고리 관리 API

#### API 사용법
1. Springboot 프로젝트를 빌드 후 인텔리제이 등을 이용하여 jar 를 실행합니다.
2. 8089 포트로 접속이 가능 합니다. (http://localhost:8089/swagger-ui.html)
3. Swagger 문서에 각 URI 에 대한 설명을 참고하여 테스트 진행이 가능 합니다.

#### 어플리케이션 구성
- Command
    - 카테고리 등록, 조회, 수정, 삭제 명령을 정의 합니다.
- CategoryManagerController
    - 카테고리 벌크 및 단건 등록, 리스트 조회, 단건 수정, 삭제(자식까지 삭제) 기능을 수행합니다. 
- Service
    - 카테고리 벌크저장, 단건저장, 조회, 수정, 삭제 기능을 수행합니다.
- QueryGenerator
    - 조회 서비스에서 사용되는 QueryDSL 생성하여 조회된 데이터를 리턴한다.
- Entity
    - CategoryInfo
        - 카테고리 엔터티를 정의 합니다.
    
#### 인메모리 H2 DB 정보
http://localhost:8089/h2-console/
- 계정 : sa / password

### 사용기술
- Frameworks: SpringBoot
- ORM Library : JPA, QueryDSL
- DB: H2
- WebFlux 를 사용하여 요청과 응답을 NIO 기반에서 작동이 될 수 있도록 구현
- Swagger를 이용하여 개발코드 문서 기반으로 빠르게 URL 테스트가 가능하도록 환경 구성
- JUnit 를 기반으로 하여 중요한 로직에 대해서는 해당 기능이 정상 작동하는지 확인하며 개발 진행
# meetup_study

[5/11 ~ 5/28]

모임 서비스

- mysql, jpa
  -baseentity 상속
- 스프링 시큐리티, 필터, 인터셉터
  - jwt 토큰 인증 및 권한 부여 방식
  - 인증 및 권한 예외처리
  - 소셜 로그인 (다른사이트 확장가능하도록)
- 유저 CRUD
  - fakeuser CRUD
  - dto
- 모임 룸 CRUD
  -첨부파일 업로드, 삭제, 다운로드
  -조회수 redis
- 스케쥴링 
  - 매일 오전 9시 1분 랜덤 이벤트 룸 찾아서 모든 고객에게 참여 이메일 전송
- 로깅 (aop)
  - 레벨별(일반, 에러) 날짜별 파일 구분
- swagger
- batch(스키마생성)
- git action(ci/cd)
- test (junit)
  - jacoco 테스트 커버리지
  - h2
 -env
- validation
- docker compose




[5/29 ~ 5/31, 6/10~] 

리팩토링 
-> DB 구조 변경 및 테이블 추가 -> dto, 컨트롤러, 서비스 일일히 다바꿔야하는데 복잡

![image](https://github.com/toy-k/meetup_study/assets/88143547/5c620932-8bb1-4c4e-a58d-37413b798494)


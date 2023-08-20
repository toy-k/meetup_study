# meetup_study (모임 서비스)
  - 사용기술
  - 실행방법
  - 스크린샷
  - 구현 기능
  - 할 일
  - 학습 자료


## [사용기술]

<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

## [실행방법]
```
./gradlew build

cd docker

docker-compose build

docker-compose up
```



## [스크린샷]

#adminer

![adminer](https://github.com/toy-k/meetup_study/assets/88143547/8b2109eb-93aa-4690-8ac8-417797fb5bc2)


#env 예시

![env](https://github.com/toy-k/meetup_study/assets/88143547/bdddb141-aa59-4c78-b7c2-d915c45bd14b)

#프론트 이미지

https://github.com/toy-k/vuejs_study/edit/main/README.md





## [구현 기능]

- mysql, jpa
  -baseentity 상속
- 스프링 시큐리티, 필터, 인터셉터
  - jwt 토큰 인증 및 권한 부여 방식
  - 인증 및 권한 예외처리
  - 소셜 로그인 (다른사이트 확장 적용 가능)
- 유저 CRUD
  - fakeuser CRUD
  - dto
  - 프로필 사진
- 모임 룸 CRUD
  -첨부파일 업로드, 다운로드, 삭제
  -조회수 redis
- 스케쥴링 
  - 매일 오전 9시 1분 랜덤 이벤트 룸 찾아서 모든 고객에게 참여 이메일 전송 (비활성화)
- 로깅 (aop)
  - 레벨별(일반, 에러) 날짜별 구분 파일 생성
- swagger
- batch(스키마 생성)
- git action(ci/cd) (깃 머지 전 테스트 , 비활성화)
- test (junit)
  - jacoco 테스트 커버리지
  - h2
 -env
- validation (Dto)
- docker compose
- 댓글 CRD
- 공지사항 CRUD
  - admin유저 (C, U, D)


리팩토링 
-> DB 구조 변경 및 테이블 추가 -> dto, 컨트롤러, 서비스 일일히 다바꿔야하는데 복잡 
-> 새로만드는 것보다 있는것 수정이 나음.
-> 프론트와 연결중
![image](https://github.com/toy-k/meetup_study/assets/88143547/5c620932-8bb1-4c4e-a58d-37413b798494)


쿼리 최적화 
-> fetch join으로 요청 쿼리 횟수 줄이기

DTO mapping
-> entity에서 Dto 변환 모듈 생성 및 관리

인덱스
-> User 엔티티의 username 필드에 index 셋팅. 원래 중복 허용했어서 카디널리티가 낮았는데, API 호출용으로 사용중이고, 적어도 이메일보단 짧아서 선택.

레디스
-> Room 조회, 수정, 삭제 시 캐싱
-> Cart(장바구니) 기능 추가





## [할 일]

  






## [학습자료]

유데미 
  - [NEW] Master Spring Boot 3 & Spring Framework 6 with Java(https://www.udemy.com/course/spring-hibernate-tutorial/)

인프런
  - 김영한 님 강의 일부


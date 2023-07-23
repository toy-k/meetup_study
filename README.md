# meetup_study (모임 서비스)
  - 실행방법
  - 스크린샷
  - 구현 기능
  - 할 일
  - 학습 자료


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

[5/11 ~ 5/28]

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



[5/29 ~ 5/31, 6/10~6/27] 

리팩토링 
-> DB 구조 변경 및 테이블 추가 -> dto, 컨트롤러, 서비스 일일히 다바꿔야하는데 복잡 
-> 새로만드는 것보다 있는것 수정이 나음.
-> 프론트와 연결중
![image](https://github.com/toy-k/meetup_study/assets/88143547/5c620932-8bb1-4c4e-a58d-37413b798494)




[7/20 ~]

쿼리 최적화 
-> fetch join으로 요청 쿼리 횟수 줄이는 중.
-> 고치면서 dto 고치니 사방에서 에러 터진상황

인덱스
-> User 엔티티의 username 필드에 index 셋팅. 원래 중복 허용했어서 카디널리티가 낮았는데, API 호출용으로 사용중이고, 적어도 이메일보단 짧아서 선택.




## [할 일]

  - 기타 소셜 로그인 추가
  - API 스트레스 테스트
  - 레디스 조회수 싱크 수정
  - 환경변수 통합 및 예시 업로드 (도커, 테스트 환경)
  - 실행환경 별 로깅 콘솔 출력 유무 변경
  - makefile로 도커 컨테이너 시작 중지 삭제 및 볼륨 초기화 명령어 셋팅
  - 파일 구조 정리
  - 분산 처리 시스템 디비 + 트랜잭션(프로파게이션) 테스트
  - DB 정규화 등 구조 수정
  - TDD 강의 수강 및 테스트 코드 수정 및 추가(유효 테스트 커버리지 80%)
      - 기능 추가하거나 구조 수정하면 테스트가 통째로 틀려서 빌드자체가 안되는 문제
          -> 일단 전부 주석
          -> 트랜잭션 등 핵심적 비즈니스 코드에만 최소 적용?
  - fetch join 추가 및 비효율적인 예외처리 중 사용하는 쿼리 최적화
  - 각 테이블 별 높은 카디널리티 + 비교 효율성 가진 컬럼 인덱싱
  - 룸 파일 업로드시 에러 수정
  - 도커 레디스 연결 에러 수정
  - 유저 프로필 이미지 수정 후 토큰 상태 유지 수정


  - 기능 추가 구현
    
  






## [학습자료]

유데미 
  - [NEW] Master Spring Boot 3 & Spring Framework 6 with Java(https://www.udemy.com/course/spring-hibernate-tutorial/)

인프런
  - 김영한 님 강의 일부


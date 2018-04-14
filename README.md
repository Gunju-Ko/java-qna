# 질문답변 게시판
## 진행 방법
* 질문답변 게시판에 대한 html template은 src/main/resources 디렉토리의 static에서 확인할 수 있다. html template을 통해 요구사팡을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## TODO 리스트
* 유저
  * Admin 유저, 일반 유저 구분
    * Admin 유저만 사용자 페이지를 볼 수 있도록 수정
    * 유저 페이지에서 일반 유저, Admin 유저 구분

* 프로필
    * 프로필 등록 기능 구현
    * 개인정보 수정 시 password 확인
    * 비밀번호 변경 기능 구현
    * 프로필 부분에서 본인이 질문한 내역 볼 수 있도록 수정

* UI 개선
  * 상단바 부분 개선
    * 개인정보수정을 클릭해서 페이지가 넘어가도 버튼활성화가 바뀌지 않는 문제 해결 
  * 유저 프로필은 유저가 등록한 프로필이 보이도록 수정 *완료*

* 회원 가입 기능 개선
  * 이메일 인증 기능 구현
  * 중복 이메일/아이디 확인 기능 구현
  * 패스워드 확인 기능 구현

* 로그인 기능
  * 자동 로그인 기능 구현
  * 아이디, 패스워드 기억하기 기능 구현

* 질문
  * 페이징 처리
  * 내가 질문한 질문만 수정/삭제 보이기

* 답변
  * 페이징 처리
  * 내가 답변한 답변만 수정/삭제 보이기

* 에러페이지
  * 에러페이지 개선
  
* 기타
  * SaveAction 플러그인 적용
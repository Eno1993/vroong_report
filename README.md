## Java project : 쇼핑몰 관리자 서버 구현하기
<br/>

### 환경 및 언어
> IntelliJ IDEA, Spring Boot, MySQL, JAVA
<br/>

### 프로젝트 목표
>쇼핑몰 관리자 서버를 구현하여 어떠한 유저가 어떤상품을 구매했는지를 상세하게 확인한다.<br/>
GET 매핑을 통하여 user의 아이디를 통해 해당 user에 대한 정보를 나타내고 그룹별 아이템에 대한 정보를 순차적으로 가져온다.<br/><br/>
user : 사용자 정보<br/>
item : 상품 정보<br/>
category : 상품 카테고리 정보<br/>
order_detail : 상품 주문 정보 <br/>
order_group : 상품 그룹 정보 <br/>
partner : 파트너 정보<br/>
admin_user : 관리자 정보<br/>

<br/>

### study 패키지 
![study 구성](https://user-images.githubusercontent.com/46813592/103863313-fad5cf00-5103-11eb-9b35-6ec361f5fe62.png)

### DB table 관계
![table_relation](https://user-images.githubusercontent.com/46813592/103867013-00361800-510a-11eb-8b79-b9738284cd8e.png)

### 실행 결과
> Chrome 환경에서 Talend API Tester를 확장 설치하여 user 1의 주문 정보를 받아오는 과정
<br/>

![user1_orderInfo](https://user-images.githubusercontent.com/46813592/103865539-b0eee800-5107-11eb-8fd0-d94ebd386004.png)


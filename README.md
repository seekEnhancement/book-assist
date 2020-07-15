# Book-Assist

## Requirements
  - 회원 가입/로그인, id/password, 비밀번호 암호화
  - 책 검색
    - 검색 keyword, 결과는 pagination 
    - kakao 책 검색 open api 활용
    - kakao api 장애 시 naver 책 검색 open api 활용
  - 내 검색 히스토리 - keyword, 일시 최신순
  - 인기 키워드 목록 - top 10 keyword(검색 횟수 포함)

## Technical Implementation
  - build tool : gradle (6.4)
  - java version : 11
    - amazon corretto jdk 11.0.5
  - base skeleton : start.spring.io
  - dependencies
    - spring boot 2.3.1
    - Junit 5
    - spring boot starter-web
    - spring security
    - spring data-jpa
    - spring cloud openfeign (with hystrix/ribbon/feign-httpclient/apache httpclient/spring-retry)
    - reactor-core
    - caffeine cache
    - awaitlity (for test)
    - springfox swagger
  - key features
    - kakao/naver openapi integration with declarative httpclient(openfeign)
      - hystrix circuit breaker / ribbon retry (apache httpclient connec/read timeout)
    - spring mvc HATEOAS style rest pagination
    - asynchronous search history db insert using reactor  
    - top 10 search keyword using caffeine in-memory cache (10s expire)
    - spring security database userdetails extension, basic authentication, [oauth token]
    - spring data jpa - entity/repository - Users, Searches, User-Search, [Books]
  - Load Test
    - jmeter random csv data file (user, keyword)
    - apache bench
    - [openapi mock server?]
  - Continuous Integration
    - gradle plugins - pmd, cpd, jacoco, sonarqube
    - jenkins pipeline
    - sonarqube (Sonar Way rule)
  - Containerize
    - cloud native build pack
    - tagging -> dockerhub push
  - Environment Setup
    - jenkins, sonarqube, performance test environment

## References
  - https://start.spring.io
  - https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide
  - https://developers.naver.com/docs/search/book/
  - https://www.baeldung.com/rest-api-pagination-in-spring
  - https://reflectoring.io/spring-security-password-handling/
  - https://www.baeldung.com/parameterized-tests-junit-5
  - https://www.baeldung.com/awaitlity-testing

## Book Search Test
  - start application
```
java -jar -Dfile.encoding=UTF-8 build/libs/bookassist-0.0.1-SNAPSHOT.jar
```
  - book search api using curl (with default basic auth user, json format with npm module - npm install json -g)
```
curl -v -u 'woo:woo00' "http://localhost:8080/book/search?query=java" | json
```
  - query parameter with Korean example (--data-urlencode, curl -G for GET)
```
curl -v -G -u 'woo:woo00' "http://localhost:8080/book/search" --data-urlencode "query=미움받을 용기" | json
```
  - user register api using curl invalid UserRequest example
```
curl -v -H 'Content-Type: application/json'  -d '{"id" : "s", "name" : "invalid name with more than 20 characters", "password": "pwd00", "passwordVerify": null, "email": "notvalidemail" }' "http://localhost:8080/user/register"
..
< HTTP/1.1 400 
..
{"status":400,"code":600001,"message":"passwordConfirm: passwordVerify should be equal to password., passwordVerify: 비어 있을 수 없습니다, name: 크기가 2에서 20 사이여야 합니다, id: 크기가 2에서 12 사이여야 합니다, email: 올바른 형식의 이메일 주소여야 합니다"}%   
```
  - user register api using curl valid UserRequest example
```
curl -v -H 'Content-Type: application/json'  -d '{"id" : "seek", "name" : "우병훈", "password": "pwd00", "passwordVerify": "pwd00", "email": "valid@woos.me" }' "http://localhost:8080/user/register"
..
< HTTP/1.1 200 
..
```
  - book search with registered user basic authentication (--data-urlencode, curl -G for GET)
    - you can check HATEOAS style rest pagination info in the response header `Link`
```
curl -v -G -u 'seek:pwd00' "http://localhost:8080/book/search" --data-urlencode "query=김미경" | json
```
  - in case of just 1 page - `Link` is empty
```
curl -v -G -u 'seek:pwd00' "http://localhost:8080/book/search" --data-urlencode "query=김미경의 리부트" | json
```
  - my book search history (retrieves all data currently)
```
curl -v -u 'seek:pwd00' "http://localhost:8080/book/mysearches" | json
```
  - top10 recommend queries
```
curl -v -u 'seek:pwd00' "http://localhost:8080/book/recommend" | json
```
  - check h2-console
```
browse http://localhost:8080/h2-console , jdbc:h2:mem:testdb, sa / password
check Users, Searches table data 
```
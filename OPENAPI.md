* kakao openapi - Daum 책 검색
```
➜  ~ curl -v -X GET "https://dapi.kakao.com/v3/search/book?target=title" --data-urlencode "query=미움받을 용기" -H "Authorization: KakaoAK ${DEVKAKAO_APP_KEY}" | json

  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying 203.217.239.16...
* TCP_NODELAY set
* Connected to dapi.kakao.com (203.217.239.16) port 443 (#0)
* ALPN, offering h2
* ALPN, offering http/1.1
* successfully set certificate verify locations:
*   CAfile: /etc/ssl/certs/ca-certificates.crt
  CApath: /etc/ssl/certs
} [5 bytes data]
..
> GET /v3/search/book?target=title HTTP/1.1
> Host: dapi.kakao.com
> User-Agent: curl/7.58.0
> Accept: */*
> Authorization: KakaoAK ############################
> Content-Length: 63
> Content-Type: application/x-www-form-urlencoded
> 
} [63 bytes data]
* upload completely sent off: 63 out of 63 bytes
{ [5 bytes data]
< HTTP/1.1 200 OK
< Server: nginx
< Date: Fri, 10 Jul 2020 15:50:21 GMT
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Connection: keep-alive
< Vary: Accept-Encoding
< X-Request-Id: 0f5d73b0-c2c5-11ea-8103-0d672e849aa9
< Access-Control-Allow-Origin: *
< Access-Control-Allow-Methods: GET, OPTIONS
< Access-Control-Allow-Headers: Authorization, KA, Origin, X-Requested-With, Content-Type, Accept
< 
{ [11969 bytes data]
100 11997    0 11934  100    63   200k   1086 --:--:-- --:--:-- --:--:--  201k
* Connection #0 to host dapi.kakao.com left intact
{
  "documents": [
    {
      "authors": [
        "기시미 이치로",
        "고가 후미타케"
      ],
      "contents": "그의 고민에 “인간은 변할 수 있고, 누구나 행복해 질 수 있다. 단 그러기 위해서는 ‘용기’가 필요하다”고 말한 철학자가 있다. 바로 프로이트, 융과 함께 ‘심리학의 3대 거장’으로 일컬어지고 있는 알프레드 아들러다.  『미움받을 용기』는 아들러 심리학에 관한 일본의 1인자 철학자 기시미 이치로와 베스트셀러 작가인 고가 후미타케의 저서로, 아들러의 심리학을 ‘대화체’로 쉽고 맛깔나게 정리하고 있다. 아들러 심리학을 공부한 철학자와 세상에 부정적이고 열등감",
      "datetime": "2014-11-17T00:00:00.000+09:00",
      "isbn": "8996991341 9788996991342",
      "price": 14900,
      "publisher": "인플루엔셜",
      "sale_price": 13410,
      "status": "정상판매",
      "thumbnail": "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038%3Ftimestamp%3D20200710154752",
      "title": "미움받을 용기",
      "translators": [
        "전경아"
      ],
      "url": "https://search.daum.net/search?w=bookpage&bookId=1467038&q=%EB%AF%B8%EC%9B%80%EB%B0%9B%EC%9D%84+%EC%9A%A9%EA%B8%B0"
    },
    {
      "authors": [
        "기시미 이치로",
        "고가 후미타케"
      ],
     ...
  ],
  "meta": {
    "is_end": false,
    "pageable_count": 13,
    "total_count": 14
  }
}

```

* naver openapi - 책 검색
```
➜  ~ curl "https://openapi.naver.com/v1/search/book.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1" \
    -H "X-Naver-Client-Id: ${DEVNAVER_CLIENT_ID}" \
    -H "X-Naver-Client-Secret: ${DEVNAVER_CLIENT_SECRET}" -v | json

  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying 125.209.234.165...
* TCP_NODELAY set
* Connected to openapi.naver.com (125.209.234.165) port 443 (#0)
...
> GET /v1/search/book.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1 HTTP/2
> Host: openapi.naver.com
> User-Agent: curl/7.58.0
> Accept: */*
> X-Naver-Client-Id: ####################
> X-Naver-Client-Secret: ##########
> 
{ [5 bytes data]
* Connection state changed (MAX_CONCURRENT_STREAMS updated)!
} [5 bytes data]
< HTTP/2 200 
< server: nginx
< date: Fri, 10 Jul 2020 16:05:45 GMT
< content-type: application/json; charset=UTF-8
< content-length: 8972
< vary: Accept-Encoding
< x-rate-limit: 10
< x-rate-limit-remaining: 9
< x-rate-limit-reset: 1594397146000
< vary: Accept-Encoding
< vary: Accept-Encoding
< x-powered-by: Naver
< vary: Accept-Encoding
< 
{ [8972 bytes data]
100  8972  100  8972    0     0  57512      0 --:--:-- --:--:-- --:--:-- 57512
* Connection #0 to host openapi.naver.com left intact
{
  "lastBuildDate": "Sat, 11 Jul 2020 01:05:45 +0900",
  "total": 25711,
  "start": 1,
  "display": 10,
  "items": [
    {
      "title": "선물주는산타의 <b>주식</b>투자 시크릿 (8천만 원 종잣돈으로 124배의 수익을 올린 투자 고수가 되기까지)",
      "link": "http://book.naver.com/bookdb/book_detail.php?bid=16323989",
      "image": "https://bookthumb-phinf.pstatic.net/cover/163/239/16323989.jpg?type=m1&udate=20200617",
      "author": "선물주는산타",
      "price": "16000",
      "discount": "14400",
      "publisher": "비즈니스북스",
      "pubdate": "20200416",
      "isbn": "1162541393 9791162541395",
      "description": "이후로 <b>주식</b>투자에 성공할 수 있었을까?\n“투자 원칙을 지켰을 뿐인데 자산이 100억으로 늘었다!”2020년 1월 아시아 지역부터 확산된 코로나19 사태로 <b>주식</b>시장은 사상 최악의 폭락장을 맞았다. 10년 만에... 그런데 이 예측 불가능한 증시에서 수만 명의 투자자들이 찾는 재야의 <b>주식</b>투자 고수가 있다. 바로... "
    },
...
  ]
}
```

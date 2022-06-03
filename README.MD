spring-boot-starter-security
인증처리가 되지 않았다면 다음과 같이 출력된다.
postman 에서 아무 url로 요청해보자

{
"timestamp": "2021-09-13T12:11:45.711+00:00",
"status": 401,
"error": "Unauthorized",
"path": "/users"
}

인증처리를 위한 내용을 같이 전달한다.
url에 포함시키는 것이 아니라
header값에 포함시켜보자.

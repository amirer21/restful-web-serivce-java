//초기 데이터값 넣어주기
//UserJpaController.java에서 사용자 추가하는 메서드 부분을 실행할때
//id값은 1부터로 하면 에러가 나므로 임의로 100부터 넣어주었다
insert into user values(100, sysdate(), 'user1', 'test1234', '112233-1122334');
insert into user values(101, sysdate(), 'user2', 'test1234', '222233-1122334');
insert into user values(102, sysdate(), 'user2', 'test1234', '332233-1122334');

insert into post values(201, 'My first 11 post', 101);
insert into post values(202, 'My 222 post', 101);

--회원관리
create table zmember(
 mem_num number not null,
 id varchar2(12) unique not null,
 auth number(1) default 2 not null, --회원등급:0탈퇴,1정지,2일반,9관리자
 constraint zmember_pk primary key (mem_num)
);
create table zmember_detail(
 mem_num number not null,
 name varchar2(30) not null,
 passwd varchar2(12) not null,
 phone varchar2(15) not null,
 email varchar2(50) not null,
 zipcode varchar2(5) not null,
 address1 varchar2(120) not null,
 address2 varchar2(90) not null,
 photo varchar2(400),
 reg_date date default sysdate not null,
 modify_date date,
 constraint zmember_detail_pk primary key (mem_num),
 constraint zmember_detail_fk foreign key (mem_num)
                              references zmember (mem_num)
);
create sequence zmember_seq;

--게시판
create table zboard(
 board_num number not null,
 title varchar2(150) not null,
 content clob not null,
 hit number(9) default 0 not null,
 reg_date date default sysdate not null,
 modify_date date,
 filename varchar2(400),
 ip varchar2(40) not null,
 mem_num number not null,
 constraint zboard_pk primary key (board_num),
 constraint zboard_fk foreign key (mem_num)
                         references zmember (mem_num)
);
create sequence zboard_seq;

--좋아요
create table zboard_fav(
 board_num number not null,
 mem_num number not null,
 constraint zboard_fav_fk1 foreign key (board_num)
                        references zboard (board_num),
 constraint zboard_fav_fk2 foreign key (mem_num)
                        references zmember (mem_num)
);


--댓글
create table zboard_reply(
	re_num number not null,
	re_content varchar2(900) not null, --300자 제한
	re_date date default sysdate not null,
	re_modifydate date,
	re_ip varchar2(40) not null,
	board_num number not null,
    mem_num number not null,
	constraint zreply_pk primary key (re_num),
	constraint zreply_fk1 foreign key (board_num) 
					references zboard(board_num),
	constraint zreply_fk2 foreign key (mem_num) 
					references zmember(mem_num)
);

create sequence zreply_seq;


--채팅방
create table zboard_chatroom(
	chatroom_num number not null, --채팅방 번호
	board_num number not null, --부모글번호
	writer_num number not null, -- 작성자 회원번호
	reader_num number not null, -- 읽은 사람(채팅 희망자) 회원번호
	room_date date default sysdate not null,
	constraint chatroom_pk primary key (chatroom_num),
	constraint chatroom_fk1 foreign key (board_num) references zboard(board_num),
	constraint chatroom_fk2 foreign key (writer_num) references zmember(mem_num),
	constraint chatroom_fk23 foreign key (reader_num) references zmember(mem_num)
);

create sequence zchatroom_seq;


--채팅
create table zboard_chat(
	chat_num number not null,
	chatroom_num number not null,
	mem_num number not null, -- 채팅 글 작성자
	message varchar2(900) not null,
	chat_date date default sysdate not null,
	read_check number(1) default 1 not null, -- 읽음 0 /읽지않음 1
	constraint chat_pk primary key (chat_num),
	constraint chat_fk1 foreign key (chatroom_num) references zboard_chatroom(chatroom_num),
	constraint chat_fk2 foreign key (mem_num) references zmember (mem_num)
);

create sequence zchat_seq;


--상품
create table zitem(
	item_num number not null,
	name varchar2(60) not null,
	price number(9) not null,
	quantity number(7) not null,
	photo1 varchar2(400) not null,
	photo2 varchar2(400) not null,
	detail clob not null,
	reg_date date default sysdate not null,
	modify_date date,
	status number(1) not null, --표시여부 (판매가능 여부) 1:미표시, 2:표시
	constraint zitem_pk primary key (item_num)
);

create sequence zitem_seq;


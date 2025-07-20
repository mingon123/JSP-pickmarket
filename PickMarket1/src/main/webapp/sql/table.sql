create table users(
 user_num number not null,
 id varchar2(50) unique not null,
 auth number(1) default 2 not null, --회원등급: 0:탈퇴,1:정지,2:일반,9:관리자
 report_count number(1) default 0 not null,
 constraint users_pk primary key (user_num)
);
--회원 상세정보
create table users_detail(
 user_num number not null,
 name varchar2(30) not null,
 nickname varchar2(30) unique not null,
 passwd varchar2(100) not null,
 phone varchar2(15) not null,
 photo varchar2(400),
 reg_date date default sysdate not null,
 modi_date date,
 score number default 100 not null,
 region_cd varchar2(10) not null,
 constraint users_detail_pk primary key (user_num),
 constraint users_detail_fk1 foreign key (user_num) references users(user_num),
 constraint users_detail_fk2 foreign key (region_cd) references location(region_cd)
);
create sequence users_seq;

--지역
create table location (
 region_cd varchar2(10) not null,
 sido_cd varchar2(5) not null,
 sgg_cd varchar2(5) not null,
 umd_cd varchar2(5) not null,
 region_nm varchar2(100) not null,
 constraint location_pk primary key (region_cd)
);

--카테고리
create table category(
 category_num number not null,
 category_name varchar2(30) not null,
 category_status number(1) default 1 not null,
 constraint category_pk primary key (category_num)
);
create sequence category_seq;

--상품
create table product(
 product_num number not null,
 title varchar2(150) not null,
 content clob not null,
 state number(1) default 0 not null, --0판매중, 1예약중, 2판매완료
 hit number default 0 not null,
 reg_date date default sysdate not null,
 modi_date date,
 user_num number not null,
 ip varchar2(40) not null,
 price number not null check (price >= 0),
 up_count number(1) default 0 not null,
 up_date date,
 category_num number not null,
 thumbnail_img varchar2(400) not null,
 hide_status number(1) default 0 not null, --0보임, 1숨김
 x_loc number(10,6) not null,
 y_loc number(10,6) not null,
 region_cd varchar2(10) not null,
 
 constraint product_pk primary key (product_num),
 constraint product_fk1 foreign key (user_num) references users (user_num),
 constraint product_fk2 foreign key (category_num) references category (category_num),
 constraint product_fk3 foreign key (region_cd) references location (region_cd)
);
create sequence product_seq;

--상품 이미지
create table product_img(
 product_num number not null,
 filename varchar2(400) not null,
 constraint product_img_fk foreign key (product_num) references product (product_num)
);

--상품 찜 (product_num,user_num을 복합 pk로 사용해서 중복 방지 가능)
create table product_fav(
 product_num number not null,
 user_num number not null,
 fav_date date default sysdate not null,
 alarm_flag number(1) default 0 not null, --0:알림 허용, 1:알림 거부
 constraint product_fav_fk1 foreign key (product_num) references product (product_num),
 constraint product_fav_fk2 foreign key (user_num) references users (user_num)
);

--상품 댓글
create table product_reply(
 reply_num number not null,
 reply_content varchar2(900) not null,
 reply_date date default sysdate not null,
 reply_modidate date,
 reply_ip varchar2(40) not null,
 product_num number not null,
 user_num number not null,
 constraint product_reply_pk primary key (reply_num),
 constraint product_reply_fk1 foreign key (product_num) references product (product_num),
 constraint product_reply_fk2 foreign key (user_num) references users (user_num)
);
create sequence product_reply_seq;

--최근 본 상품 목록
create table product_view (
 view_num number not null,
 user_num number not null,
 product_num number not null,
 view_date date default sysdate not null,
 constraint product_view_pk primary key (view_num),
 constraint product_view_fk1 foreign key (user_num) references users(user_num),
 constraint product_view_fk2 foreign key (product_num) references product(product_num),
 constraint uk_recent UNIQUE(user_num, product_num)
);
create sequence product_view_seq;

--매너평가
create table manner_rate(
 manner_num number not null,
 rated_num number not null, --평가받는 회원
 rater_num number not null, --평가하는 회원
 op1 number default 0 not null,
 op2 number default 0 not null,
 op3 number default 0 not null,
 op4 number default 0 not null,
 op5 number default 0 not null,
 op6 number default 0 not null,
 op7 number default 0 not null,
 op8 number default 0 not null,
 op9 number default 0 not null,
 op10 number default 0 not null,
 constraint manner_rate_pk primary key(manner_num),
 constraint manner_rate_fk1 foreign key(rated_num) references users (user_num),
 constraint manner_rate_fk2 foreign key(rater_num) references users (user_num)
);
create sequence manner_rate_seq;

--커뮤니티
create table board(
 board_num number not null,
 btitle varchar2(150) not null,
 bcontent clob not null,
 bhit number default 0 not null,
 breg_date date default sysdate not null,
 bmodi_date date,
 bfilename varchar2(400),
 bip varchar2(40) not null,
 user_num number not null,
 region_cd varchar(10) not null,
 
 constraint board_pk primary key(board_num),
 constraint board_fk foreign key(user_num) references users (user_num),
 constraint board_fk2 foreign key(region_cd) references location (region_cd)
);
create sequence board_seq;

--커뮤니티 좋아요
create table board_fav(
 board_num number not null,
 user_num number not null,
 constraint board_fav_fk1 foreign key(board_num) references board(board_num),
 constraint board_fav_fk2 foreign key(user_num) references users (user_num)
);

--커뮤니티 댓글
create table board_reply(
 breply_num number not null,
 breply_content varchar2(900) not null,
 breply_date date default sysdate not null,
 breply_modidate date,
 breply_ip varchar2(40) not null,
 board_num number not null,
 user_num number not null,
 constraint board_reply_pk primary key(breply_num),
 constraint board_reply_fk1 foreign key(board_num) references board (board_num),
 constraint board_reply_fk2 foreign key(user_num) references users (user_num)
);
create sequence board_reply_seq;

--채팅방
create table product_chatroom(
 chatroom_num number not null,
 product_num number not null,
 seller_num number not null,
 buyer_num number not null,
 room_date date default sysdate not null,
 deal_datetime timestamp(6) not null,
 deal_x_loc number(10,6) not null,
 deal_y_loc number(10,6) not null,
 constraint product_chatroom_pk primary key(chatroom_num),
 constraint product_chatroom_fk1 foreign key(product_num) references product (product_num),
 constraint product_chatroom_fk2 foreign key(seller_num) references users (user_num),
 constraint product_chatroom_fk3 foreign key(buyer_num) references users (user_num)
);
create sequence product_chatroom_seq;

--채팅
create table product_chat(
 chat_num number not null,
 chatroom_num number not null,
 user_num number not null,
 message varchar2(900) not null,
 filename varchar2(400),
 chat_date date default sysdate not null,
 read_check number(1) default 1 not null, --읽음 0, 읽지 않음 1
 deleted number(1) default 1 not null,
 constraint product_chat_pk primary key (chat_num),
 constraint product_chat_fk1 foreign key (chatroom_num) references product_chatroom (chatroom_num),
 constraint product_chat_fk2 foreign key (user_num) references users (user_num) 
);
create sequence product_chat_num;

--리뷰(후기)
create table review(
 re_num number not null,
 re_content clob,
 re_date date default sysdate not null,
 re_writer_num number not null, --리뷰 작성자
 re_getter_num number not null, --리뷰 받은자
 product_num number not null,
 constraint review_pk primary key(re_num),
 constraint review_fk1 foreign key(re_writer_num) references users (user_num),
 constraint review_fk2 foreign key(re_getter_num) references users (user_num),
 constraint review_fk3 foreign key(product_num) references product (product_num)
);
create sequence review_seq;

--신고
create table report(
 report_num number not null,
 reporter_num number not null,
 suspect_num number not null,
 report_content clob not null,
 report_img varchar2(400) not null,
 report_title varchar2(140) not null,
 report_date date default sysdate not null,
 constraint report_pk primary key(report_num),
 constraint report_fk1 foreign key(reporter_num) references users (user_num),
 constraint report_fk2 foreign key(suspect_num) references users (user_num)
);
create sequence report_seq;

--키워드(알림)
create table keyword(
 k_num number not null,
 user_num number not null,
 k_word varchar2(30) not null,
 constraint keyword_pk primary key (k_num),
 constraint keyword_fk foreign key (user_num) references users (user_num)
);
create sequence keyword_seq;

--차단
create table block(
 blocker_num number not null, --차단하는 사람
 blocked_num number not null, --차단당한 사람
 block_content varchar2(500) not null,
 block_date date default sysdate not null,
 constraint block_fk1 foreign key(blocker_num) references users (user_num),
 constraint block_fk2 foreign key(blocked_num) references users (user_num)
);

--1:1문의
create table qna(
 qna_num number not null,
 qna_title varchar2(400) not null,
 qna_content clob not null,
 qna_re varchar2(1000),
 q_date date default sysdate not null,
 a_date date,
 user_num number not null,
 constraint qna_pk primary key(qna_num),
 constraint qna_fk foreign key(user_num) references users (user_num)
);
create sequence qna_seq;

--공지사항
create table notice(
 noti_num number not null,
 noti_title varchar2(400) not null,
 noti_content clob not null,
 noti_view number default 0 not null,
 noti_date date default sysdate not null,
 constraint notice_pk primary key (noti_num)
);
create sequence notice_seq;

--운영정책
create table operational_policy(
 pol_num number not null,
 pol_title varchar2(400) not null,
 pol_content clob not null,
 pol_date date default sysdate not null,
 pol_modi_date date,
 constraint operational_policy_pk primary key (pol_num)
);
create sequence operational_policy_seq;

--최근검색어
create table search_word(
 s_num number not null,
 user_num number not null,
 s_word varchar2(150) not null,
 s_date date default sysdate not null,
 constraint search_word_pk primary key (s_num),
 constraint search_word_fk foreign key (user_num) references users (user_num),
 constraint search_word_uk unique(user_num,s_word) --유니크 키 추가
);
create sequence search_word_seq;

--자주묻는질문
create table faq(
 faq_num number not null,
 faq_title varchar2(400) not null,
 faq_content clob not null,
 faq_date date default sysdate not null,
 faq_modidate date,
 constraint faq_pk primary key (faq_num)
);
create sequence faq_seq;

package kr.chat.vo;

public class ProductChatVO {
	private long chat_num; //채팅번호
	private long chatroom_num; //채팅방번호
	private long user_num; //채팅 글 작성자
	private String message; //채팅 내용
	private String filename; //채팅 사진
	private String chat_date; //채팅 날짜
	private int read_check; //읽음 여부(읽음 0, 읽지 않음 1)
	private int deleted; //삭제 여부(삭제 0, 삭제 안함 1)
	private int processed; //버튼 사용 여부
	
	private String nickname;
	
	
	
	public int getProcessed() {
		return processed;
	}
	public void setProcessed(int processed) {
		this.processed = processed;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public String getChat_date() {
		return chat_date;
	}
	public void setChat_date(String chat_date) {
		this.chat_date = chat_date;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public long getChat_num() {
		return chat_num;
	}
	public void setChat_num(long chat_num) {
		this.chat_num = chat_num;
	}
	public long getChatroom_num() {
		return chatroom_num;
	}
	public void setChatroom_num(long chatroom_num) {
		this.chatroom_num = chatroom_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getRead_check() {
		return read_check;
	}
	public void setRead_check(int read_check) {
		this.read_check = read_check;
	}
	
}

package kr.board.vo;

public class BoardFavVO {
	private long board_num;
	private long user_num;
	
	private BoardVO boardVO;
	
	public BoardFavVO() {}
	
	public BoardFavVO(long board_num, long user_num) {
		this.board_num=board_num;
		this.user_num=user_num;
	}

	public long getBoard_num() {
		return board_num;
	}

	public void setBoard_num(long board_num) {
		this.board_num = board_num;
	}

	public long getUser_num() {
		return user_num;
	}

	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}

	public BoardVO getBoardVO() {
		return boardVO;
	}

	public void setBoardVO(BoardVO boardVO) {
		this.boardVO = boardVO;
	}
	
	
}

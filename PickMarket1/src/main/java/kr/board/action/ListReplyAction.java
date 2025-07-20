package kr.board.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.controller.Action;
import kr.util.PagingUtil;
import kr.util.StringUtil;

public class ListReplyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		String rowCount = request.getParameter("rowCount");
		if(rowCount==null) rowCount = "10";
		
		long board_num = Long.parseLong(
				         request.getParameter("board_num"));
		
		BoardDAO dao = BoardDAO.getInstance();
		int count = dao.getReplyBoardCount(board_num);
		
		/*
		 * ajax 방식으로 목록을 표시하기 때문에 PagingUtil은 페이지수
		 * 표시가 목적이 아니라 목록 데이터의 페이지 처리를 위해 rownum
		 * 번호를 구하는 것이 목적임
		 */
		//currentPage,count,rowCount
		PagingUtil page = new PagingUtil(
				                Integer.parseInt(pageNum),
				           count,Integer.parseInt(rowCount));
		
		List<BoardReplyVO> list = null;
		if(count > 0) {
			list = dao.getListReplyBoardd(page.getStartRow(),
					                     page.getEndRow(),
					                     board_num);
		}else {
			list = Collections.emptyList();
		}
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		Map<String,Object> mapAjax = new HashMap<String,Object>();
		mapAjax.put("count", count);
		mapAjax.put("list", list);
		//로그인한 사람이 작성자인지 체크하기 위해서 로그인한 회원번호 전송
		mapAjax.put("user_num", user_num);
		
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

}








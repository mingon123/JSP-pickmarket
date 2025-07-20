package kr.board.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardFavVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class WriteFavAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> mapAjax= new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Long user_num= (Long)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			mapAjax.put("result", "logout");
		}else {//로그인 된 경우
			long board_num = Long.parseLong(request.getParameter("board_num"));
			BoardFavVO favVO = new BoardFavVO();
			favVO.setBoard_num(board_num);
			favVO.setUser_num(user_num);
			
			BoardDAO dao = BoardDAO.getInstance();
			//좋아요 등록 여부 체크
			BoardFavVO db_fav = dao.selectFav(favVO);
			if(db_fav!=null) {//좋아요 등록 ㅇ
				//좋아요 삭제
				dao.deleteFav(db_fav);
				mapAjax.put("status", "noFav");				
			}else {//좋아요 등록 X
				//좋아요 등록
				dao.insertFav(favVO);
				mapAjax.put("status", "yesFav");
			}
			mapAjax.put("result", "success");
			mapAjax.put("count", dao.selectFavCount(board_num));
		}
		//JSON 데이터로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

}

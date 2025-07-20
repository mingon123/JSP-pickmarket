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

public class GetFavAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> mapAjax = new HashMap<String, Object>();

        try {
            // 게시물 번호 받아오기
            long board_num = Long.parseLong(request.getParameter("board_num"));

            HttpSession session = request.getSession();
            Long user_num = (Long) session.getAttribute("user_num");

            BoardDAO dao = BoardDAO.getInstance();

            // 로그인 여부에 따라 좋아요 상태 결정
            if (user_num == null) {  // 로그인되지 않은 경우
                mapAjax.put("status", "noFav");
            } else {  // 로그인된 경우
                BoardFavVO boardFav = dao.selectFav(new BoardFavVO(board_num, user_num));
                if (boardFav != null) {
                    mapAjax.put("status", "yesFav");
                } else {
                    mapAjax.put("status", "noFav");
                }
            }

            // 좋아요 개수
            mapAjax.put("count", dao.selectFavCount(board_num));

        } catch (NumberFormatException e) {
            mapAjax.put("status", "error");
            mapAjax.put("message", "잘못된 게시글 번호입니다.");
        } catch (Exception e) {
            mapAjax.put("status", "error");
            mapAjax.put("message", "서버 오류 발생");
        }

        return StringUtil.parseJSON(request, mapAjax);
    }
}

package kr.board.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class WriteReplyAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> mapAjax = new HashMap<>();

        HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");

        if (user_num == null) { // 로그인되지 않은 경우
            mapAjax.put("result", "logout");
        } else {
            String boardNumStr = request.getParameter("board_num");

            if (boardNumStr == null || boardNumStr.trim().isEmpty()) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "게시글 번호가 누락되었습니다.");
                return StringUtil.parseJSON(request, mapAjax);
            }

            try {
                BoardReplyVO reply = new BoardReplyVO();
                reply.setUser_num(user_num); // 댓글 작성자 회원번호
                reply.setBreply_content(request.getParameter("breply_content"));
                reply.setBreply_ip(request.getRemoteAddr());
                reply.setBoard_num(Long.parseLong(boardNumStr)); // 댓글의 부모 글 번호

                UserDAO userDAO = UserDAO.getInstance();
                userDAO.modifyTemperatureUser(user_num, 1);
                
                BoardDAO dao = BoardDAO.getInstance();
                dao.insertReplyBoard(reply);

                mapAjax.put("result", "success");
            } catch (NumberFormatException e) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "게시글 번호 형식이 잘못되었습니다.");
            } catch (Exception e) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "댓글 등록 중 오류가 발생했습니다.");
                e.printStackTrace(); // 로그 확인용
            }
        }

        // JSON 문자열로 반환
        return StringUtil.parseJSON(request, mapAjax);
    }
}

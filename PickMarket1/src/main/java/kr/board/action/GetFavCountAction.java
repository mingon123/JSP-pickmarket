package kr.board.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.board.dao.BoardDAO;
import kr.controller.Action;
import kr.util.StringUtil;

public class GetFavCountAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> mapAjax = new HashMap<String, Object>();

        try {
            long board_num = Long.parseLong(request.getParameter("board_num"));
            BoardDAO dao = BoardDAO.getInstance();
            int count = dao.selectFavCount(board_num);

            mapAjax.put("count", count);
        } catch (Exception e) {
            mapAjax.put("status", "error");
            mapAjax.put("message", "서버 오류 발생");
        }

        return StringUtil.parseJSON(request, mapAjax);
    }
}

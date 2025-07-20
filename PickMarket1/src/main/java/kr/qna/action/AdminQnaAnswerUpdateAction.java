package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.dao.NoticeDAO;
import kr.info.vo.FaqVO;
import kr.info.vo.NoticeVO;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;
import kr.util.StringUtil;

public class AdminQnaAnswerUpdateAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}

        // 입력값
        long qna_num = Long.parseLong(request.getParameter("qna_num"));
        QnaDAO dao = QnaDAO.getInstance();

        QnaVO qna = new QnaVO();
        qna.setQna_num(qna_num);
        qna.setQna_title(StringUtil.parseQuot(StringUtil.useNoHtml(request.getParameter("qna_title"))));
        qna.setQna_re(request.getParameter("qna_re"));
        
        String aDateStr = request.getParameter("a_date");
        if (aDateStr != null && !aDateStr.isEmpty()) {
        	qna.setA_date(java.sql.Date.valueOf(aDateStr));
        	request.setAttribute("notice_msg", "수정이 완료되었습니다.");
        }else {
        	request.setAttribute("notice_msg", "작성이 완료되었습니다.");
        }

        dao.insertReplyQnaByAdmin(qna);
        
		request.setAttribute("notice_url", request.getContextPath()+"/qna/adminQnaDetail.do?qna_num="+ qna_num);

		return "common/alert_view.jsp";

    }
}

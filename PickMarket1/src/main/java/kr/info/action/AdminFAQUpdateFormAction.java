package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.dao.NoticeDAO;
import kr.info.vo.FaqVO;
import kr.info.vo.NoticeVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class AdminFAQUpdateFormAction implements Action{

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
		
		//전송된 데이터 반환
		long faq_num = Long.parseLong(request.getParameter("faq_num"));
		
		FaqDAO dao = FaqDAO.getInstance();
		FaqVO faq = dao.getFAQDetailByAdmin(faq_num);
		
		//textarea에서 보여줄 줄바꿈 처리된 내용 반환
		String faq_content = faq.getFaq_content().replaceAll("<br>", "\n");
		
		request.setAttribute("faq", faq);
		request.setAttribute("faq_content", faq_content);
		
		return "info/adminFAQUpdateForm.jsp";
	}

}






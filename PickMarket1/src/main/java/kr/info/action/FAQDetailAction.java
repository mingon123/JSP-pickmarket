package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.vo.FaqVO;

public class FAQDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//비회원도 조회 가능
		long faq_num = Long.parseLong(request.getParameter("faq_num"));
		
		FaqDAO dao = FaqDAO.getInstance();
		FaqVO faq = dao.getFAQDetailByAdmin(faq_num);
		
		request.setAttribute("faq", faq);
		
		return "info/faqDetail.jsp";
	}

} //class

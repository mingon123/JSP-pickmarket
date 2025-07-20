package kr.info.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.vo.FaqVO;
import kr.util.PagingUtil;

public class FAQListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//비회원도 조회 가능
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		FaqDAO dao = FaqDAO.getInstance();
		int count = dao.getFAQCountByAdmin(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"adminFAQList.do");
		
		List<FaqVO> list = null;
		if(count > 0) {
			list = dao.getFAQListByAdmin(page.getStartRow(), page.getEndRow(),keyfield,keyword);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage()); 
		
		return "info/faqList.jsp";
	}

} //class

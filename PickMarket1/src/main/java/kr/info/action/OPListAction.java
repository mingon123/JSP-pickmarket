package kr.info.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.info.dao.OperationalPolicyDAO;
import kr.info.vo.OperationalPolicyVO;
import kr.util.PagingUtil;

public class OPListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//비회원도 조회 가능
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		OperationalPolicyDAO dao = OperationalPolicyDAO.getInstance();
		int count = dao.getOPCountByAdmin(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"opList.do");
		
		List<OperationalPolicyVO> list = null;
		if(count > 0) {
			list = dao.getOPListByAdmin(page.getStartRow(), page.getEndRow(),keyfield,keyword);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());

		return "info/opList.jsp";
	}

} //class

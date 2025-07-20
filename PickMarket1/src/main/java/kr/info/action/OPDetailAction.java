package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.info.dao.OperationalPolicyDAO;
import kr.info.vo.OperationalPolicyVO;
import kr.util.StringUtil;

public class OPDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//비회원도 조회 가능		
		long pol_num = Long.parseLong(request.getParameter("pol_num"));
		
		OperationalPolicyDAO dao = OperationalPolicyDAO.getInstance();
		OperationalPolicyVO op = dao.getOPDetailByAdmin(pol_num);
		
		request.setAttribute("op", op);
		
		return "info/opDetail.jsp";
	}

} //class

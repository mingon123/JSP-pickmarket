package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.vo.CategoryVO;
import kr.util.StringUtil;

public class CategoryDeleteAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//전송된 데이터 반환
		long category_num = Long.parseLong(request.getParameter("category_num"));
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		
		CategoryDAO dao = CategoryDAO.getInstance();
		CategoryVO db_category = dao.getCategory(category_num);
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		//관리자 여부 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if (user_num == null) { //로그인 되지 않은 경우
			mapAjax.put("result", "logout");
		}else if(user_auth == 9) {
			//관리자로 로그인한 경우
			dao.deleteCategory(category_num);
			mapAjax.put("result", "success");
		}else {
			//관리자로 로그인하지 않은 경우
			mapAjax.put("result", "wrongAccess");
		}
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);	
	}

} //class

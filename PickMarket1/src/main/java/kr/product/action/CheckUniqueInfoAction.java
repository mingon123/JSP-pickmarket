package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.util.StringUtil;

public class CheckUniqueInfoAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String category_name = request.getParameter("category_name");
		
		CategoryDAO dao = CategoryDAO.getInstance();
		boolean flag = dao.checkUniqueInfo(category_name);
		
		//잭슨 라이브러리가 JSON 형태로 만들어준다.
		Map<String,String> mapAjax = new HashMap<String,String>();
		if (flag) { //중복			
			mapAjax.put("result", "categoryDuplicated");			
		} else { //미중복 
			mapAjax.put("result", "categoryNotFound");
		} // if		
		
		//JSON 문자열로 변환하고 JSON문자열을 클라이언트로 전송할 JSP를 반환
		return StringUtil.parseJSON(request, mapAjax);
	}

} //class

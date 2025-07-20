package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.util.StringUtil;

public class UpdateToReservedStateAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> mapAjax = new HashMap<String,String>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			mapAjax.put("request", "logout");
		}else {
			ProductDAO dao = ProductDAO.getInstance();
			long product_num = Long.parseLong(request.getParameter("product_num"));
			dao.updateToReservedState(product_num);
			
			mapAjax.put("result", "success");
		}
		//JSON에 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
		
	}

}

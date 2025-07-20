package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductFavVO;
import kr.util.StringUtil;

public class GetFavAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//전송된 데이터 반환
		long product_num = Long.parseLong(request.getParameter("product_num"));
		
		Map<String,Object> mapAjax = new HashMap<String,Object>();
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		ProductDAO dao = ProductDAO.getInstance();				
		if (user_num == null) { //로그인 되지 않은 경우
			mapAjax.put("status", "noFav");
		} else { //로그인된 경우
			ProductFavVO productFav = dao.selectFav(new ProductFavVO(product_num,user_num));
			 if (productFav!=null) {
				//로그인한 회원이 찜 클릭
				mapAjax.put("status", "yesFav");
				mapAjax.put("alarm", productFav.getAlarm_flag());
			} else {
				//로그인한 회원이 찜 미클릭
				mapAjax.put("status", "noFav");
			}// if
		} //if
		
		//찜 개수
		mapAjax.put("count", dao.selectFavCount(product_num));
		
		//JSON 데이터로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}
	
} //class

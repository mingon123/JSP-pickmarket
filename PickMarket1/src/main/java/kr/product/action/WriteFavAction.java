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

public class WriteFavAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> mapAjax = new HashMap<String,Object>();
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			mapAjax.put("result", "logout");
		} else { //로그인된 경우
			long product_num = Long.parseLong(request.getParameter("product_num"));
			
			ProductFavVO favVO = new ProductFavVO();
			//setter 이용
			favVO.setProduct_num(product_num);
			favVO.setUser_num(user_num);
			
			ProductDAO dao = ProductDAO.getInstance();
			//찜 등록 여부 체크
			ProductFavVO db_fav = dao.selectFav(favVO);
			if (db_fav!=null) { //찜 등록 O
				//찜 삭제
				dao.deleteFav(db_fav);
				mapAjax.put("status", "noFav");
			} else { //찜 등록 X
				//찜 등록
				dao.insertFav(favVO);
				mapAjax.put("status", "yesFav");
			}// if
			mapAjax.put("result", "success");
			mapAjax.put("count", dao.selectFavCount(product_num));
			
		} //if
		
		//JSON 데이터로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}
} //class

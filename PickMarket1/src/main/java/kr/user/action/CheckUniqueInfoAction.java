package kr.user.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class CheckUniqueInfoAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		
		UserDAO dao = UserDAO.getInstance();
		boolean flag = dao.checkUniqueInfo(id);
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		if(flag) {//중복
			mapAjax.put("result", "idDuplicated");
		}else {//미중복
			mapAjax.put("result", "idNotFound");
		}
		
		/*
		 * JSON 형식으로 변환하기를 원하는 문자열을 HashMap에 
		 * key와 value의 쌍으로 저장한 후 ObjectMapper의
		 * writeValueAsString()에 Map객체를 전달해서 일반 문자열
		 * 데이터를 JSON 형식의 문자열 데이터로 변환 후 반환
		 */
		
		return StringUtil.parseJSON(request, mapAjax);
	}

}

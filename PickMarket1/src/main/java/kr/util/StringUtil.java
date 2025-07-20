package kr.util;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class StringUtil {
	/*
	 * HTML태그를 허용하면서 줄바꿈
	 */
	public static String useBrHtml(String str) {
		if(str == null) return null;
		
		return str.replaceAll("\r\n", "<br>")
				  .replaceAll("\r", "<br>")
				  .replaceAll("\n", "<br>");
	}
	/*
	 * HTML태그를 허용하지 않으면서 줄바꿈 
	 */
	public static String useBrNoHtml(String str) {
		if(str == null) return null;
		
		return str.replaceAll("<", "&lt;")
				  .replaceAll(">", "&gt;")
				  .replaceAll("\r\n", "<br>")
				  .replaceAll("\r", "<br>")
				  .replaceAll("\n", "<br>");
	}
	
	/*
	 * HTML태그를 허용하지 않음
	 */
	public static String useNoHtml(String str) {
		if(str == null) return null;
		
		return str.replaceAll("<", "&lt;")
				  .replaceAll(">", "&gt;");
	}
	
	/*
	 * 큰 따옴표 처리
	 */
	public static String parseQuot(String str) {
		if(str == null) return null;
		
		return str.replaceAll("\"", "&quot;");
	}
	
	/*
	 * 제네릭 표현의 ? : 제네릭 자료형을 특정 자료형으로 한정하지 않고
	 *                임의의 자료형으로 지정하고자 할 때 ?
	 *                (와이드 카드) 사용
	 */
	public static String parseJSON(
			           HttpServletRequest request,
			           Map<String,?> map)
	                 throws JsonGenerationException,
	                        JsonMappingException,
	                        IOException{
		//JSON 데이터 생성
		ObjectMapper mapper = new ObjectMapper();
		String ajaxData = mapper.writeValueAsString(map);
		
		//request에 JSON 문자열 저장
		request.setAttribute("ajaxData", ajaxData);
		//JSP 경로 반환
		return "common/ajax_view.jsp";
	}
}







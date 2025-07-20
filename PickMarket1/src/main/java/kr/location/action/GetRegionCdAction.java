package kr.location.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.util.StringUtil;

public class GetRegionCdAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String region_nm = request.getParameter("region_nm");
    	
    	Map<String, String> mapAjax = new HashMap<String, String>();
        LocationDAO dao = LocationDAO.getInstance();
        
        String region_cd = dao.findRegionCdByName(region_nm);
    	
        if (region_nm == null || region_nm.trim().isEmpty()) { // region_nm이 null 이거나 비어있는 경우 처리
            mapAjax.put("result", "region_nmIsNull");
        } else if (region_cd != null) {  // region_cd가 존재하면
            mapAjax.put("result", "success");
            mapAjax.put("region_cd", region_cd);  // region_cd를 응답에 포함
        } else {  // region_cd를 찾을 수 없을 경우 처리
            mapAjax.put("result", "region_cdNotFound");
        }
        
        // JSON 형식으로 결과 반환
        return StringUtil.parseJSON(request, mapAjax);
    }
}

package kr.location.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.location.vo.LocationVO;
import kr.util.DBUtil;

public class LocationDAO {
	//싱글턴 패턴
	private static LocationDAO instance = new LocationDAO();
	
	public static LocationDAO getInstance() {
		return instance;
	}
	private LocationDAO() {}
	
	// 지역명 -> 지역코드
	public String findRegionCdByName(String region_nm) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String regionCd = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT region_cd FROM location WHERE region_nm = ?";			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, region_nm);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				regionCd = rs.getString("region_cd");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return regionCd;
	}
	
	// rogion_cd -> LocationVO
	public LocationVO getLocationByCd(String region_cd) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LocationVO location = null;

		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT region_cd, region_nm FROM location WHERE region_cd = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, region_cd);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				location = new LocationVO();
				location.setRegion_cd(rs.getString("region_cd"));
				location.setRegion_nm(rs.getString("region_nm"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return location;
	}
	
	// 지역코드 -> 지역명
	public String findRegionNmByCode(String region_cd) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String regionNm = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT region_nm FROM location WHERE region_cd = ?";			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, region_cd);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				regionNm = rs.getString("region_nm");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return regionNm;
	}
	
	// 모든 지역 출력
	public List<LocationVO> getAllLocations() throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<LocationVO> list = null;
	    String sql = null;
	    
	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT region_cd, region_nm FROM location";
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        list = new ArrayList<LocationVO>();
	        while (rs.next()) {
	            LocationVO location = new LocationVO();
	            location.setRegion_cd(rs.getString("region_cd"));
	            location.setRegion_nm(rs.getString("region_nm"));
	            list.add(location);
	        }
	    } finally {
	    	DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return list;
	}
	
	// 지역코드 레코드 확인
	public boolean isValidRegionCode(String region_cd) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    String sql = null;
		boolean isValid = false;
	    
	    try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM location WHERE region_cd = ?";
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, region_cd);
	    	rs = pstmt.executeQuery();
	    	if (rs.next() && rs.getInt(1) > 0) {
                isValid = true;
            }
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	    return isValid;
	}

	
	
}

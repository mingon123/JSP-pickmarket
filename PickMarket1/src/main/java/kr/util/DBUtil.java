package kr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtil {
	private static final String DB_DRIVER = "oracle.jdbc.OracleDriver";
	private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String DB_ID = "user01";
	private static final String DB_PASSWORD = "1234";
	
	//Connection 객체를 생성해서 반환
	/*
	public static Connection getConnection()throws Exception{
		//JDBC 수행 1단계 : 드라이버 로드
		Class.forName(DB_DRIVER);
		//JDBC 수행 2단계 : Connection 객체 생성
		return DriverManager.getConnection(
				                  DB_URL,DB_ID,DB_PASSWORD);
	}
	*/
	
	//context.xml에서 설정 정보를 읽어들여 커넥션풀로부터 커넥션을
	//할당 받음.
	public static Connection getConnection()throws Exception{
		Context initCtx = new InitialContext();
		DataSource ds = 
				(DataSource)initCtx.lookup(
						        "java:comp/env/jdbc/xe");
		return ds.getConnection();
	}
	
	//자원정리
	public static void executeClose(ResultSet rs, 
			             PreparedStatement pstmt, 
			                      Connection conn) {
		if(rs!=null)try {rs.close();}catch(SQLException e) {}
		if(pstmt!=null)try {pstmt.close();}catch(SQLException e) {}
		if(conn!=null)try {conn.close();}catch(SQLException e) {}
	}
}








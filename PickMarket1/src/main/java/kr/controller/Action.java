package kr.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Action {        
	//추상메서드
	public String execute(HttpServletRequest request,
			              HttpServletResponse response)
	                      throws Exception;
}

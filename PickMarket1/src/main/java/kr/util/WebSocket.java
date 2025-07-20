package kr.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

//WebSocket의 호스트 주소 설정
@ServerEndpoint("/webSocket")

public class WebSocket {
	
	static List<Session> sessionList = 
			Collections.synchronizedList(new ArrayList<>());
	
	//WebSocket으로 브라우저가 접속하면 요청되는 함수
	@OnOpen
	public void handleOpen(Session userSession) {
		//콘솔에 접속 로그를 출력
		System.out.println("client is now connected..");
		//웹소켓 연결 시 세션리스트에 추가
		sessionList.add(userSession);
	}
	
	//WebSocket으로 메시지가 오면 요청되는 함수
	@OnMessage
	public void handleMessage(String message) throws Exception{
		//메시지 내용을 콘솔에 출력
		System.out.println("receive from client : " + message);
		//세션리스트에게 데이터를 보냄
		Iterator<Session> iterator = sessionList.iterator();
		while(iterator.hasNext()) {
			//해당 데이터 발송
			iterator.next().getBasicRemote().sendText(message);
		}
	}
	
	//WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose(Session userSession) {
		//콘솔에 접속 끊김 로그 출력
		System.out.println("client is now disconnected..");
		sessionList.remove(userSession);
	}
	
	//WebSocket과 브라우저 간에 통신에러가 발생하면 요청된 함수
	@OnError
	public void handleError(Throwable t) {
		//콘솔에 에러표시
		t.printStackTrace();
	}
	
	
}

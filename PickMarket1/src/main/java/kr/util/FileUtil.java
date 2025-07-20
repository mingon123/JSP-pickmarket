package kr.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class FileUtil {
	//업로드 상대 경로
	public static final String UPLOAD_PATH = "/upload";
	
	//파일 업로드
	public static String uploadFile(
			                    HttpServletRequest request,
			                        String param)
			            throws IOException,ServletException{
		//컨텍스트 경로상의 업로드 절대 경로
		String upload = request.getServletContext()
				               .getRealPath(UPLOAD_PATH);
		Part part = request.getPart(param);
		String filename = part.getSubmittedFileName();
		if(filename != null && !filename.isEmpty()) {
			//파일명이 중복되지 않도록 파일명 변경
			UUID uuid = UUID.randomUUID();
			//원래 파일명을 보존하지 않을 경우
			filename = uuid.toString() + 
				filename.substring(filename.lastIndexOf("."));
			//_ 이후에 원래 파일명을 보존할 경우
			//filename = uuid.toString() + "_" + filename;
			part.write(upload+"/"+filename);//지정된 경로에 파일 저장
		}
		return filename;
	}
	
	//(추가)다중파일 업로드: 여러 Part를 넘겨서 처리
	public static String uploadFile(HttpServletRequest request, Part part)
	        throws IOException, ServletException {
		//컨텍스트 경로상의 업로드 절대 경로
	    String upload = request.getServletContext().getRealPath(UPLOAD_PATH);
	    String filename = part.getSubmittedFileName();
	    
	    if (!filename.isEmpty()) {
	    	//파일명이 중복되지 않도록 파일명 변경
	    	UUID uuid = UUID.randomUUID();
	    	//원래 파일명을 보존하지 않을 경우
	    	filename = uuid.toString() + filename.substring(filename.lastIndexOf("."));
	        part.write(upload + "/" + filename); //지정된 경로에 파일 저장
	    }
	    return filename;
	}
	
	//파일 삭제
	public static void removeFile(HttpServletRequest request,
			                      String filename) {
		if(filename!=null) {
			String upload = request.getServletContext()
					               .getRealPath(UPLOAD_PATH);
			File file = new File(upload+"/"+filename);
			if(file.exists()) file.delete();
		}
	}
	
	public static String getFilename(String headerName) {
		for(String name : headerName.split(";")) {
			if(name.trim().startsWith("filename")) {
				String filename = name.substring(name.indexOf("=")+1)
						              .trim()
						              .replace("\"", "");
				int index = filename.lastIndexOf(File.separator);
				return filename.substring(index+1);
			}
		}
		return null;
	}
}

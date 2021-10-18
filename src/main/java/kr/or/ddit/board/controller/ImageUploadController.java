package kr.or.ddit.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.RequestPart;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.fileupload.MultipartFile;

@Controller
public class ImageUploadController {
	
	private String saveFolderURL = "/resources/boardImages";
	
	@RequestMapping(value="/board/imageUpload.do", method=RequestMethod.POST)
	public String upload(
		@RequestPart("upload") MultipartFile upload,
		HttpServletRequest req, HttpServletResponse resp
	) throws IOException {
		Map<String, Object> resultMap = new HashMap<>();
		String fileName = null;
		int uploaded = 0;
		String url = null;
		if(!upload.isEmpty()) {
			String mime = upload.getContentType();
			if(mime!=null && mime.startsWith("image/")) {
				File saveFolder = new File( req.getServletContext().getRealPath(saveFolderURL) );
				if(!saveFolder.exists()) saveFolder.mkdirs();
				String saveName = UUID.randomUUID().toString();
				upload.transferTo(new File(saveFolder, saveName));
				fileName = upload.getOriginalFilename();
				uploaded = 1;
				url = req.getContextPath() + saveFolderURL + "/" + saveName;
			}
		}
		
		resultMap.put("fileName", fileName);
		resultMap.put("uploaded", uploaded);
		resultMap.put("url", url);
	 	
		resp.setContentType("application/json;charset=UTF-8");
		try(
			PrintWriter out = resp.getWriter();	
		){
			new ObjectMapper().writeValue(out, resultMap);
		}
		return null;
	}
	
}









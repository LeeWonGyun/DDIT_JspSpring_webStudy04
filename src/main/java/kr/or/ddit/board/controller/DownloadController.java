package kr.or.ddit.board.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.AttatchVO;

@Controller
public class DownloadController {
	
	private BoardService service = new BoardServiceImpl();
	private String saveFolderPath = "d:/saveFiles";
	private File saveFolder = new File(saveFolderPath);
	
	
	@RequestMapping("/board/download.do")
	public String download(@RequestParam("what") int attNo, HttpServletResponse resp) throws IOException {
		AttatchVO atch = service.download(attNo);
		File saveFile = new File(saveFolder, atch.getAttSavename());
		if(!saveFile.exists()) throw new FileNotFoundException("저장위치에 해당 파일이 없음.");
		
		String filename = URLEncoder.encode(atch.getAttFilename(), "UTF-8").replaceAll("\\+", " ");
		resp.setHeader("Content-Length", atch.getAttFilesize().toString());
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attatchment;filename=\""+filename+"\"");
		try(
			OutputStream os = resp.getOutputStream();
		){
			FileUtils.copyFile(saveFile, os);
		}
		return null;
	}
}











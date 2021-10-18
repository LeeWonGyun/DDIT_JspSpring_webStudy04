package kr.or.ddit.prod.controller;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestPart;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.fileupload.MultipartFile;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.util.ValidateUtils;
import kr.or.ddit.validate.groups.InsertGroup;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdInsertController{
	private ProdService service = new ProdServiceImpl();

	private String saveFolderURL = "/resources/prodImages";
	
	@RequestMapping("/prod/prodInsert.do")
	public String prodForm() {
		return "prod/prodForm";
	}
	
	@RequestMapping(value="/prod/prodInsert.do", method=RequestMethod.POST)
	public String process(@ModelAttribute("prod") ProdVO prod, 
			@RequestPart("prodImage") MultipartFile prodImage,
			HttpServletRequest req) throws IOException{
		if(!prodImage.isEmpty()) {
			prod.setProdImage(prodImage);
			
			File saveFolder = new File(req.getServletContext().getRealPath(saveFolderURL));
			if(!saveFolder.exists()) {
				saveFolder.mkdirs();
			}
			String saveName = UUID.randomUUID().toString();
			File dest = new File(saveFolder, saveName);
			prodImage.transferTo(dest);
			prod.setProdImg(saveName);
		}
		
		Map<String, List<String>> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		boolean valid = ValidateUtils.validate(prod, errors, InsertGroup.class);
		
		String viewName = null;
		String message = null;
		if(valid) {
			ServiceResult result = service.createProd(prod);
			switch(result) {
			case OK:
				viewName = "redirect:/prod/prodView.do?what="+prod.getProdId();
				break;
			default:
				viewName = "prod/prodForm";
				message = "서버 오류, 잠시뒤 다시 해보셈.";
			}
			
		}else {
//		3-2. 불통
			viewName = "prod/prodForm";
			
		}
		
		req.setAttribute("message", message);
		
		return viewName;
		
	}
}

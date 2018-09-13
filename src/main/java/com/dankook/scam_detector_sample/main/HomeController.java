package com.dankook.scam_detector_sample.main;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dankook.scam_detector_sample.scam_detector.*;

@Controller
public class HomeController {

	// 메인 페이지
	@RequestMapping(value = "/main")
	public String main(HttpServletRequest request, Model model) {
		return "main";
	}
	
	
	// 스켐 디텍션 페이지
	@RequestMapping(value = "/detect_scam", method = RequestMethod.POST)
	public String detect_scam(HttpServletRequest request, Model model) {
		
		// Request 인코딩 => UTF-8 로 변환
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// User Input log
		System.out.println(request.getParameter("user_input"));

		// Detect Scam
		boolean scam_flag = Scam_Detector.detect_scam(request.getParameter("user_input"));
		
		// Return result
		model.addAttribute("user_input", request.getParameter("user_input"));
		if(scam_flag) {
			// Scam
			model.addAttribute("result", "This sentence is scam");
		}
		else {
			// Non-scam
			model.addAttribute("result", "This sentence is non-scam");
		}
		return "main";
	}
	
}

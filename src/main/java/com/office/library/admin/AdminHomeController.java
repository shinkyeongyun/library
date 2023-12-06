package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller							//프로젝트가 실행될 때 스프링IoC컨테이너에 빈 객체로 생성됩니다.
@RequestMapping("/admin")			//기본적으로 /admin에 대한 요청을 AdminHomeController가 처리하게된다(필수는 아님)
public class AdminHomeController {
	
	@RequestMapping(value= {"","/"}, method = RequestMethod.GET)	//"/admin"과 "/admin/" 요청 모두 처리
	public String home() {
		System.out.println("[AdminHomeController] home()");
		
		String nextPage = "admin/home";
		
		return nextPage;
	}
}
	
	


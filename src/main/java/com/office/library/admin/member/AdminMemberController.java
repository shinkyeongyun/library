package com.office.library.admin.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	@Autowired
	AdminMemberService adminMemberService;

	/* 
	 * 회원가입 : 특별히 할일은 없고, 관리자가 회원가입을 할 수 있도록 회원가입 양식이 있는 화면(create_acount_from.jsp)을 응답해주기만 하면된다
	 */
	@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
//	@GetMapping("/createAccountForm")			//위 매핑을 간단하게 표현(Get)
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");

		String nextPage = "admin/member/create_account_form";

		return nextPage;
	}
	
	/*
	 * create_acount_from.jsp에서 회원가입에 필요한 관리자 정보 입력 후 <create account> 버튼을 클릭하면 <form>의 action에 명시한 서버주소로 관리자 정보가 전송된다.
	 */
	@RequestMapping(value = "/createAccountConfirm", method = RequestMethod.POST)
//	@PostMapping("/createAccountForm")			//위 매핑을 간단하게 표현(Post)
	public String createAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberController] createAccountConfirm()");
		
		String nextPage = "admin/member/create_account_ok";
		
		int result = adminMemberService.createAccountConfirm(adminMemberVo);	//서비스의 createAccountConfirm을 이용한다.
		
		if(result <= 0)
			nextPage = "admin/member/create_account_ng";
		
		return nextPage;
	}
	
	/*
	 * 관리자 로그인 인증처리 구현
	 */
	
	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	//로그인 확인(session)
	@PostMapping("/loginConfirm")								// /loginConfirm 요청을
	public String loginConfirm(AdminMemberVo adminMemberVo,HttpSession session) {// loginConfirm()에 매핑하고 adminMemberVo를 파라미터로 받음
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = 
				adminMemberService.loginConfirm(adminMemberVo);	//서비스에 관리자 로그인 인증 업무를 지시하기위해 loginConfirm()을 호출하고, 매개변수로 받은 adminMemberVo를 함께 전달
																//서비스에서 null또는 AdminmemberVo로 객체반환 후 아래 세션에 Vo저장
		if(loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		} else {
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);	//session에 로그인 정보 저장:setAttribute("저장할 데이터이름",실제데이터)
			session.setMaxInactiveInterval(60*30);	//유효기간 설정(분단위 60*분)
		}
		
		return nextPage;
	}
	
	//로그아웃
	@GetMapping("/logoutConfirm")
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		
		String nextPage = "redirect:/admin";	// /admin으로 리다이렉트 -> (AdminHomeController) home()으로.
		
		session.invalidate();		//세션을 무효화시키는 것.
		
		return nextPage;
	}
	
	//관리자목록("super admin"로그인 시 관리자 목록을 노출한다(nav.jsp). 승인여부 확인용)
//	@GetMapping("/listupAdmin")
//	public String listupAdmin(Model model) {		
//		System.out.println("[AdminMemberController] listupAdmin()");
//		
//		String nextPage = "admin/member/listup_admins";
//		
//		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
//		
//		model.addAttribute("adminMemberVos",adminMemberVos);	//model은 서버에서 추가한(add) 데이터를 뷰에 전달하는 역할. addAttribute("전달하려는 데이터이름",실제데이터)
//		
//		return nextPage;
//	}
	
	//뷰와 데이터(model)을 하나의 객체 담아서 전달하는 ModelAndView. 
	@GetMapping("/listupAdmin")
	public ModelAndView listupAdmin() {		
		System.out.println("[AdminMemberController] listupAdmin()");
		
		String nextPage = "admin/member/listup_admins";
		
		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		
		ModelAndView modelAndView = new ModelAndView();				//ModelAndView 객체생성
		modelAndView.setViewName(nextPage);							//뷰를 설정
		modelAndView.addObject("adminMemberVos", adminMemberVos);	//데이터를 추가한다.
		
		return modelAndView;										//ModelAndView를 반환한다.
	}
	
	@GetMapping("/setAdminApproval")
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		System.out.println("[AdminMemberController] setAdminApproval()");
		
		String nextPage = "redirect:/admin/member/listupAdmin";	//승인 작업이 끝나면 관리자목록을 갱신하기 위해 리다이렉트
		
		adminMemberService.setAdminApproval(a_m_no);
		
		return nextPage;
	}
	
	//계정(정보) 수정
	@GetMapping("/modifyAccountForm")
	public String modifyAccountForm(HttpSession session) {	//여기서 세션은 관리자 정보를 수정하기위해 현재 관리자가 로그인되어있는지 확인하는 용도
		System.out.println("[AdminMemberController] modifyAccountForm()");
		
		String nextPage = "admin/member/modify_account_form";
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
		if(loginedAdminMemberVo == null) {	//세션에 저장되어있는 관리자정보가 null이라면 로그인화면으로 리다이렉트.
			nextPage = "redirect:/admin/member/loginForm";//1.이는 로그인하지 않고 도메인으로 직접 입력하는 것에 대한 대비. 2.로그인한 상태에서 세션만료(30분)시 리다이렉트
		}
		return nextPage;
	}
	//회원정보수정 확인
	@PostMapping("/modifyAccountConfirm")
	public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) {//수정된 관리자 정보(Vo)와 세션을 관리하는 HttpSession 매개변수
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "admin/member/modify_account_ok";
		
		int result = adminMemberService.modifyAccountConfirm(adminMemberVo);	//저장되어있던 관리자정보(Vo)를 서비스에 전달
		
		if(result > 0) {	//정보변경 성공. 세션의 관리자 정보도 가장 최근에 업데이트된 관리자 정보로 변경.
			AdminMemberVo loginedAdminMemberVo = adminMemberService.getloginedAdminMemberVo(adminMemberVo.getA_m_no());
			
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60*30);
		}else {				//정보 변경 실패.
			nextPage = "admin/member/modify_account_ng";
		}
		
		return nextPage;
	}
	
	//비밀번호찾기
	//find_password_form.jsp는 관리자의 아이디, 이름, 메일 주소를 서버로 전송하는 역할
	@GetMapping("/findPasswordForm")
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordForm()");
		
		String nextPage = "admin/member/find_password_form";
		
		return nextPage;
	}
	//비밀번호찾기 확인
	@PostMapping("findPasswordConfirm")
	public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberController] findPasswordConfirm()");
		
		String nextPage = "admin/member/find_password_ok";
		
		int result = adminMemberService.findPasswordConfirm(adminMemberVo);	//findPasswordConfirm을 이용해 새로운 비밀번호 생성
		
		if (result <= 0)
			nextPage = "admin/member/find_password_ng";
		
		return nextPage;
	}
	
}

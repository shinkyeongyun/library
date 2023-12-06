package com.office.library.book.user;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import com.office.library.book.user.BookService;
import com.office.library.user.member.UserMemberVo;

@Controller
@RequestMapping("/book/user") // book/admin 의 BookController 빈이 동일한 이름으로 충돌에러가남

/*
 * 해결방법 1. 빈의 아이디 다르게 설정하기
 * 
 * @Controller("admin.BookController") ,@Controller("user.BookController")
 * @Service("admin.BookService"),@Service("user.BookService")
 * @Conponent("admin.BookDao),@Conponent("user.BookDao)
 *
 * 매번 아이디를 다르게 지정하는 것이 불편할 수 있기에 해결방법 2. BeanNameGenerator 사용하기
 * com.office.library.config 에 LibraryBeanNameGeneratro 클래스 생성
 */
public class BookController {

	@Autowired
	BookService bookService;

	// 도서검색
	@GetMapping("/searchBookConfirm")
	public String searchBookConfirm(BookVo bookVo, Model model) {
		System.out.println("[UserBookController] searchBookConfirm()");

		String nextPage = "user/book/search_book";

		List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);

		model.addAttribute("bookVos", bookVos); // 모델에 도서리스트(bookVos)를 담아서 뷰에 전달한다

		return nextPage;
	}
	
	@GetMapping("/bookDetail")
	public String bookDetail(@RequestParam("b_no") int b_no,Model model) {
		System.out.println("[UserBookController] bookDetail()");
		
		String nextPage="user/book/book_detail";
		
		BookVo bookVo = bookService.bookDetail(b_no);//도서상세정보를 가져오기위해 book_Detail()을 호출합니다
			
		model.addAttribute("bookVo",bookVo);		 //bookDetail()의 결과는 model에 저장해서 뷰에 전달합니다
		
		return nextPage;
	}
	
	@GetMapping("/rentalBookConfirm")
	public String rentalBookConfirm(@RequestParam("b_no") int b_no,HttpSession session) {
		System.out.println("[UserBookController] rentalBookConfirm()");
		
		String nextPage = "user/book/rental_book_ok";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");

		//인터셉터 설정했으므로 아래 주석.
//		if(loginedUserMemberVo == null)
//			return "redirect:/user/member/loginForm";
		
		int result = bookService.rentalBookConfirm(b_no,loginedUserMemberVo.getU_m_no());
		
		if(result <= 0)
			nextPage = "user/book/rental_book_ng";
		
		return nextPage;
	}
	//대출중인 도서 목록 확인하기
	@GetMapping("/enterBookshelf")
	public String enterBookshelf(HttpSession session, Model model) {
		System.out.println("[UserBookController] enterBookshelf()");
		
		String nextPage = "user/book/bookshelf";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		
		List<RentalBookVo> rentalBookVos = bookService.enterBooshelf(loginedUserMemberVo.getU_m_no());//도서대출정보조회
		
		model.addAttribute("rentalBookVos",rentalBookVos);
		
		return nextPage;
	}
	
	//도서 대출 이력
	@GetMapping("/listupRentalBookHistory")
	public String listupRentalBookHistory(HttpSession session, Model model) {
		System.out.println("[UserBookController] listupRentalBookHistory()");
		
		String nextPage = "user/book/rental_book_history";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		
		List<RentalBookVo> rentalBookVos = bookService.listupRentalBookHistory(loginedUserMemberVo.getU_m_no());//도서대출정보조회

		model.addAttribute("rentalBookVos",rentalBookVos);
		
		return nextPage;
	}
	
	//희망 도서 요청
	@GetMapping("/requestHopeBookForm")
	public String listupRentalBookHistory() {
		System.out.println("[UserBookController] listupRentalBookHistory()");
		
		String nextPage = "user/book/request_hope_book_form";
		
		return nextPage;
	}
	
	//희망 도서 요청 확인
	@GetMapping("/requestHopeBookConfirm")
	public String requestHopeBookConfirm(HopeBookVo hopeBookVo, HttpSession session) {
		System.out.println("[UserBookController] requestHopeBookConfirm()");
		
		String nextPage = "user/book/request_hope_book_ok";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		hopeBookVo.setU_m_no(loginedUserMemberVo.getU_m_no());	//세션에서 로그인 정보를 가져와서 HopeBookVo에 저장한다
		
		int result = bookService.requestHopeBookConfirm(hopeBookVo);	//희망도서 요청 업무를 지시합니다
		
		if(result <= 0)
			nextPage = "user/book/request_hope_book_ng";
		
		return nextPage;
	}
	
	//희망 도서 요청 목록
	@GetMapping("/listupRequestHopeBook")
	public String listupRequestHopeBook(HttpSession session, Model model) {
		System.out.println("[UserBookController] listupRequestHopeBook()");
		
		String nextPage = "user/book/list_hope_book";
		
		UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
		
		List<HopeBookVo> hopeBookVos = bookService.listupRequestHopeBook(loginedUserMemberVo.getU_m_no());
		
		model.addAttribute("hopeBookVos",hopeBookVos);
		
		return nextPage;
	}
	
}

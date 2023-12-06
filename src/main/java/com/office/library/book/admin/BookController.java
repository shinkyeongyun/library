package com.office.library.book.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import com.office.library.book.admin.util.UploadFileService;

@Controller
@RequestMapping("/book/admin")
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	UploadFileService uploadFileService;		//파일업로드하는 역할 자동주입.
	
	//도서등록
	@GetMapping("/registerBookForm")
	public String registerBookForm() {
		System.out.println("[BookController] registerBookForm()");
		
		String nextPage = "admin/book/register_book_form";
		
		return nextPage;
	}
	
	@PostMapping("/registerBookConfirm")
	public String registerBookConfirm(BookVo bookVo, @RequestParam("file") MultipartFile file) {	//BookVo:도서정보(텍스트), MultipartFile:책표지(이미지파일)
		System.out.println("[BookController] registerBookConfirm()");
		
		String nextPage = "admin/book/register_book_ok";
		
		//파일저장
		String savedFileName = uploadFileService.upload(file);	//uploadFilerService의 upload()를 이용해서 서버에 파일을 저장. 이떄 관리자가 업로드한 파일을 매개변수로 전달합니다.
		
		if(savedFileName != null) {						//파일이 잘 저장되었다면
			bookVo.setB_thumbnail(savedFileName);		//저장된 파일이름을 bookVo의 b_thumbnail에 저장합니다.
			int result = bookService.registerBookConfirm(bookVo);//데이터베이스에 신규 도서를 추가합니다
			
			if(result <= 0)
				nextPage = "admin/book/register_book_ng";
		} else {
			nextPage = "admin/book/register_book_ng";
		}
		return nextPage;
	}
	
	//도서검색
	@GetMapping("/searchBookConfirm")
	public String searchBookConfirm(BookVo bookVo, Model model) {
		System.out.println("[BookController] searchBookConfirm()");
		
		String nextPage = "admin/book/search_book";
		
		List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);
		
		model.addAttribute("bookVos",bookVos);		//모델에 도서리스트(bookVos)를 담아서 뷰에 전달한다
		
		return nextPage;
	}
	
	@GetMapping("/bookDetail")
	public String bookDetail(@RequestParam("b_no") int b_no,Model model) {
		System.out.println("[BookController] bookDetail()");
		
		String nextPage="admin/book/book_detail";
		
		BookVo bookVo = bookService.bookDetail(b_no);//도서상세정보를 가져오기위해 book_Detail()을 호출합니다
			
		model.addAttribute("bookVo",bookVo);		 //bookDetail()의 결과는 model에 저장해서 뷰에 전달합니다
		
		return nextPage;
	}
	
	//도서 정보 수정
	@GetMapping("/modifyBookForm")
	public String modifyBookForm(@RequestParam("b_no") int b_no,Model model) {
		System.out.println("[BookController] modifyBookForm()");
		
		String nextPage="admin/book/modify_book_form";
		
		BookVo bookVo = bookService.modifyBookForm(b_no);
		
		model.addAttribute("bookVo",bookVo);
		
		return nextPage;
	}
	
	@PostMapping("/modifyBookConfirm")
	public String modifyBookConfirm(BookVo bookVo,@RequestParam("file") MultipartFile file) {
		System.out.println("[BookController] modifyBookConfirm()");
		
		String nextPage = "admin/book/modify_book_ok";
		
		if(!file.getOriginalFilename().equals("")) {	//관리자가 표지 이미지를 변경했는지(새 이미지 파일 업로드)체크하는 if문으로 파일이름이 존재한다면
			//파일저장
			String savedFileName = uploadFileService.upload(file); //upload()를 이용해서 서버에 파일을 저장합니다.
			if(savedFileName != null)
				bookVo.setB_thumbnail(savedFileName);
		}
		
		int result = bookService.modifyBookConfirm(bookVo);
		
		if(result <= 0)
			nextPage = "admin/book/modify_book_ng";
		
		return nextPage;
	}
	
	//도서 정보 삭제
	@GetMapping("/deleteBookConfirm")
	public String deleteBookConfirm(@RequestParam("b_no") int b_no) {
		System.out.println("[BookController] deleteBookConfirm()");
		
		String nextPage = "admin/book/delete_book_ok";
		
		int result = bookService.deleteBookConfirm(b_no);
		
		if(result <=0)
			nextPage = "admin/book/delete_book_ng";
		
		return nextPage;
	}
	
	//대출 도서 반납처리하기(관리자)
	@GetMapping("/getRentalBooks")
	public String getRentalBooks(Model model) {
		System.out.println("[BookController] getRentalBooks()");
		
		String nextPage = "admin/book/rental_books";
		
		List<RentalBookVo> rentalBookVos = bookService.getRentalBooks();	//대출 도서 목록을 조회하고, 결과를 model에 저장.
		
		model.addAttribute("rentalBookVos",rentalBookVos);
		
		return nextPage;
	}
	
	//도서 반납 확인
	@GetMapping("/returnBookConfirm")
	public String returnBookConfirm(@RequestParam("b_no") int b_no,
									@RequestParam("rb_no") int rb_no) {
		System.out.println("[BookController] returnBookConfirm()");
		
		String nextPage = "admin/book/return_book_ok";
		
		int result = bookService.returnBookConfirm(b_no,rb_no);	//도서반납처리진행
		
		if(result <= 0)
			nextPage = "admin/book/return_book_ng";
		
		return nextPage;
	}
	
	//희망 도서 목록
	@GetMapping("/getHopeBooks")
	public String getHopeBooks(Model model) {
		System.out.println("[BookController] getHopeBooks()");
		
		String nextPage = "admin/book/hope_books";
		
		List<HopeBookVo> hopeBookVos = bookService.getHopeBooks();	//희망 도서 요청목록을 조회합니다.
		
		model.addAttribute("hopeBookVos",hopeBookVos);
		
		return nextPage;
	}

	//희망 도서 등록(입고 처리)
	@GetMapping("/registerHopeBookForm")
	public String registerHopeBookForm(Model model, HopeBookVo hopeBookVo) {
		System.out.println("[BookController] registerHopeBookForm()");
		
		String nextPage = "admin/book/register_hope_book_form";	//희망 도서를 신규 도서로 등록하기 위해 도서 등록화면으로 이동한다.
		
		model.addAttribute("hopeBookVo",hopeBookVo);	//이동할때 사용자가 입력한 희망도서 정보를 같이 전달합니다.
		
		return nextPage;
	}
	
	//희망 도서등록(입고 처리) 확인
	@PostMapping("/registerHopeBookConfirm")
	public String registerHopeBookConfirm(BookVo bookVo,
					@RequestParam("hb_no") int hb_no,
					@RequestParam("file") MultipartFile file) {
		System.out.println("[BookController] registerHopeBookConfirm()");
		
		System.out.println("hb_no: "+hb_no);
		
		String nextPage = "admin/book/register_book_ok";
		
		//파일저장
		String savedFileName = uploadFileService.upload(file);
		
		if(savedFileName != null) {						//파일이 잘 저장되었다면
			bookVo.setB_thumbnail(savedFileName);		//저장된 파일이름을 bookVo의 b_thumbnail에 저장합니다.
			int result = bookService.registerHopeBookConfirm(bookVo,hb_no);//데이터베이스에 신규 도서를 추가합니다
			
			if(result <= 0)
				nextPage = "admin/book/register_book_ng";
		} else {
			nextPage = "admin/book/register_book_ng";
		}
		return nextPage;
	}
	
	//전체 도서 목록
	@GetMapping("/getAllBooks")
	public String getAllBooks(Model model) {
		System.out.println("[BookController] getAllBooks()");
		
		String nextPage = "admin/book/full_list_of_books";
		
		List<BookVo> bookVos = bookService.getAllBooks();
		
		model.addAttribute("bookVos",bookVos);
		
		return nextPage;
	}

}

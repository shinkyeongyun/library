package com.office.library.book.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import com.office.library.book.user.BookDao;

@Service
public class BookService {
	
	@Autowired		
	BookDao bookDao;
	
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBoosBySearch(bookVo);
	}
	
	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBook(b_no);
	}

	public int rentalBookConfirm(int b_no, int u_m_no) {
		System.out.println("[BookService] rentalBookConfirm()");
		
		int result = bookDao.insertRentalBook(b_no,u_m_no);	//대출이력을 추가
		
		if(result >= 0)
			bookDao.updateRentalBookAble(b_no);//대출에 성공하면 해당 도서의 대출 가능 컬럼(b_rantal_able)값을 0으로 변경.
		
		return result;
	}

	public List<RentalBookVo> enterBooshelf(int u_m_no) {
		System.out.println("[BookService] enterBooshelf()");
		return bookDao.selectRentalBooks(u_m_no);		//대출도서를 검색
	}

	public List<RentalBookVo> listupRentalBookHistory(int u_m_no) {
		System.out.println("[BookService] listupRentalBookHistory()");
		
		return bookDao.selectRentalBookHistory(u_m_no);	//전체 대출 이력을 조회해서 결과를 컨트롤러에 반환
	}

	public int requestHopeBookConfirm(HopeBookVo hopeBookVo) {
		System.out.println("[BookService] requestHopeBookConfirm()");
		
		return bookDao.insertHopeBook(hopeBookVo);		//희망도서요청정보를 데이터베이스에 추가(insert)합니다
	}

	public List<HopeBookVo> listupRequestHopeBook(int u_m_no) {
		System.out.println("[BookService] listupRequestHopeBook()");
		
		return bookDao.selectRequestHopeBooks(u_m_no);
	}
	
	


}

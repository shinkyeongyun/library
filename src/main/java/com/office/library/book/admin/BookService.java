package com.office.library.book.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;

@Service // BookService를 스프링 Ioc컨테이너에 빈으로 등록합니다
public class BookService {

	// 신규도서 등록결과를 상수로 선언.
	final static public int BOOK_ISBN_ALREADY_EXIST = 0; // 이미 등록된 도서
	final static public int BOOK_REGISTER_SUCCESS = 1; // 신규 도서 등록 성공
	final static public int BOOK_REGISTER_FAIL = -1; // 신규 도서 등록 실패

	@Autowired // DAO 빈을 자동 주입합니다
	BookDao bookDao;

	public int registerBookConfirm(BookVo bookVo) { // 신규 도서를 등록합니다
		System.out.println("[BookService] registerBookConfirm()");

		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn()); // 신규 도서 등록 전에 동일한 ISBN이 존재하는 확인합니다.

		if (!isISBN) {
			int result = bookDao.insertBook(bookVo);

			if (result > 0)
				return BOOK_REGISTER_SUCCESS;
			else
				return BOOK_REGISTER_FAIL;
		} else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}

	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");

		return bookDao.selectBoosBySearch(bookVo);
	}

	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] searchBookConfirm()");

		return bookDao.selectBook(b_no);
	}

	public BookVo modifyBookForm(int b_no) {
		System.out.println("[BookService] modifyBookForm()");

		return bookDao.selectBook(b_no);
	}

	public int modifyBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] modifyBookConfirm()");

		return bookDao.updateBook(bookVo);
	}

	public int deleteBookConfirm(int b_no) {
		System.out.println("[BookService] deleteBookConfirm()");

		return bookDao.deleteBook(b_no);
	}

	public List<RentalBookVo> getRentalBooks() {
		System.out.println("[BookService] getRentalBooks()");

		return bookDao.selectRentalBooks();// 대출 도서 목록을 조회하고 결과를 컨트롤러에 반환.
	}

	public int returnBookConfirm(int b_no, int rb_no) {
		System.out.println("[BookService] returnBookConfirm()");

		int result = bookDao.updateRentalBook(rb_no); // 대출도서 반납 처리합니다

		if (result > 0)
			result = bookDao.updateBook(b_no); // 도서의 상태를 '대출가능'으로 변경합니다.

		return result;
	}

	public List<HopeBookVo> getHopeBooks() {
		System.out.println("[BookService] getHopeBooks()");

		return bookDao.selectHopeBooks();
	}

	public int registerHopeBookConfirm(BookVo bookVo, int hb_no) {
		System.out.println("[BookService] registerHopeBookConfirm()");

		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn()); // 신규 도서 등록 전에 동일한 ISBN이 존재하는 확인합니다.

		if (!isISBN) {
			int result = bookDao.insertBook(bookVo);		//데이터베이스에 신규도서를 등록합니다

			if (result > 0) {
				bookDao.updateHopeBookResult(hb_no);		//희망 도서 요청 정보를 입고 완료로 업데이트합니다.
				
				return BOOK_REGISTER_SUCCESS;
			} else {
				return BOOK_REGISTER_FAIL;
			}
		} else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}

	public List<BookVo> getAllBooks() {
		System.out.println("[BookService] getAllBooks()");
		
		return bookDao.selectAllBooks();
	}

}

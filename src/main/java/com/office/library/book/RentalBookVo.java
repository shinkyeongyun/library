package com.office.library.book;

import lombok.Getter;
import lombok.Setter;

/*
 * RentalBookVo는 tbl_rental_book 테이블의 데이터를 저장할 수 있는 클래스입니다.
 * 
 * tbl_rental_book에는 대출정보 뿐만 아니라 도서정보와 회원정보가 '조인'될 수 있다.
 * 
 * 따라서 모든 멤버 필드를 선언한다.
 * 
 */

@Getter
@Setter
public class RentalBookVo {
	
	int rb_no;				//대출번호(자동증가)
	String rb_start_date;	//대출일 (default : '1000-01-01'	
	String rb_end_date;		//반납일
	String rb_reg_date;		//등록일
	String rb_mod_date;		//수정일
	
	int b_no;				//도서번호
	String b_thumbnail;
	String b_name;
	String b_author;	
	String b_publisher;
	String b_publish_year;
	String b_isbn;
	String b_call_number;
	int b_rantal_able;
	String b_reg_date;
	String b_mod_date;
	
	int u_m_no;				//사용자번호
	String u_m_id;
	String u_m_pw;
	String u_m_name;
	String u_m_gender;
	String u_m_mail;
	String u_m_phone;
	String u_m_reg_date;
	String u_m_mod_date;
}

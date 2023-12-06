package com.office.library.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HopeBookVo {

	int hb_no;					//요청 번호(자동증가)
	String hb_name;				//희망도서 이름
	String hb_author;			//희망 도서 저자
	String hb_publisher;		//희망 도서 출판사
	String hb_publish_year;		//희망 도서 출판연도
	String hb_reg_date;			//등록일
	String hb_mod_date;			//수정일
	int hb_result;				//입고 결과
	String hb_result_last_date;	//입고 결과 날짜
	
	int u_m_no;					//요청자 번호
	String u_m_id;
	String u_m_pw;
	String u_m_name;
	String u_m_gender;
	String u_m_mail;
	String u_m_phone;
	String u_m_reg_date;
	String u_m_mod_date;
	
}

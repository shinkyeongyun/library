package com.office.library.book;

/*
 * 관리자뿐만 아니라 일반 사용자도 사용하는 클래스로, 일반 사용자가 도서를 검색하거나 대출할때 사용될 수 있습니다.
 * 따라서 BookVO를 com.office.library.book.admin패키지에 만들지 않고, 
 * com.office.library.book 에 만들어서 관리자와 사용자가 공용으로 사용할 수 있게 패키지를 만들었습니다.
 */
public class BookVo {
	
	int b_no;				//번호(자동증가)
	String b_thumbnail;		//표지이미지
	String b_name;			//도서이름
	String b_author;		//저자
	String b_publisher;		//출판사
	String b_publish_year;	//출판연도
	String b_isbn;			//ISBN(발행자번호)
	String b_call_number;	//청구기호
	int b_rantal_able;		//대출가능여부(0:불가, 1:가능)
	String b_reg_date;		//등록일
	String b_mod_date;		//수정일	
	
	public int getB_no() {
		return b_no;
	}
	public void setB_no(int b_no) {
		this.b_no = b_no;
	}
	public String getB_thumbnail() {
		return b_thumbnail;
	}
	public void setB_thumbnail(String b_thumbnail) {
		this.b_thumbnail = b_thumbnail;
	}
	public String getB_name() {
		return b_name;
	}
	public void setB_name(String b_name) {
		this.b_name = b_name;
	}
	public String getB_author() {
		return b_author;
	}
	public void setB_author(String b_author) {
		this.b_author = b_author;
	}
	public String getB_publisher() {
		return b_publisher;
	}
	public void setB_publisher(String b_publisher) {
		this.b_publisher = b_publisher;
	}
	public String getB_publish_year() {
		return b_publish_year;
	}
	public void setB_publish_year(String b_publish_year) {
		this.b_publish_year = b_publish_year;
	}
	public String getB_isbn() {
		return b_isbn;
	}
	public void setB_isbn(String b_isbn) {
		this.b_isbn = b_isbn;
	}
	public String getB_call_number() {
		return b_call_number;
	}
	public void setB_call_number(String b_call_number) {
		this.b_call_number = b_call_number;
	}
	public int getB_rantal_able() {
		return b_rantal_able;
	}
	public void setB_rantal_able(int b_rantal_able) {
		this.b_rantal_able = b_rantal_able;
	}
	public String getB_reg_date() {
		return b_reg_date;
	}
	public void setB_reg_date(String b_reg_date) {
		this.b_reg_date = b_reg_date;
	}
	public String getB_mod_date() {
		return b_mod_date;
	}
	public void setB_mod_date(String b_mod_date) {
		this.b_mod_date = b_mod_date;
	}

}

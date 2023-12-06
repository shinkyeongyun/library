package com.office.library.user.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMemberVo {
	
	int u_m_no;				//번호(자동증가)
	String u_m_id;			//계정아이디
	String u_m_pw;			//비밀번호
	String u_m_name;		//이름
	String u_m_gender;		//성별
	String u_m_mail;		//메일주소
	String u_m_phone;		//연락처
	String u_m_reg_date;	//등록일
	String u_m_mod_date;	//수정일

}

package com.office.library.admin.member;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberService {
	
	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;		//회원가입 중복
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;		//회원가입 성공
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1;			//회원가입 실패(데이터베이스 insert 실패)
	
	@Autowired
	AdminMemberDao adminMemberDao;
	
	@Autowired
	JavaMailSenderImpl javaMailSenderImpl;	//스프링IoC컨테이너에 생성된 JavaMailSenderImpl빈을 사용하기 위해 자동주입.(mail-context.xml)
	
	public int createAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());
		
		if(!isMember) {					//isMember가 true이면 이미 사용 중인 아이디, false이면 사용 가능한 아이디.
			int result = adminMemberDao.insertAdminAccount(adminMemberVo);	//회원 정보를 데이터베이스에 추가한다. 성공한 경우 1이 반환, 그렇지 않은 경우 0이 반환
			
			if(result > 0)
				return ADMIN_ACCOUNT_CREATE_SUCCESS;
			else
				return ADMIN_ACCOUNT_CREATE_FAIL;
		}else {
			return ADMIN_ACCOUNT_ALREADY_EXIST;
		}
	}

	public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] loginConfirm()");
		
		AdminMemberVo loginedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);
		
		if(loginedAdminMemberVo != null) {	//DAO에 요청한 회원이 있으면 null이 아니고
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN SUCCESS!!");
		}else {								//회원이 없으면 null이다.
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN FAIL!!");
		}
		return loginedAdminMemberVo;		//null이 아닌 경우 세션에 VO 객체 저장.
	}

	public List<AdminMemberVo> listupAdmin() {
		System.out.println("[AdminMemberService] listupAdmin()");
		return adminMemberDao.selectAdmins();		//데이터 베이스의 관리자 목록을 요청합니다.
	}

	public void setAdminApproval(int a_m_no) {
		System.out.println("[AdminMemberService] setAdminApproval()");
		
		int result = adminMemberDao.updateAdminAccount(a_m_no);	//DAO에 관리자번호(a_m_no)를 전달해서 데이터베이스의 a_m_approval값을 0에서 1로 업데이트
		
	}

	//관리자 정보 업데이트
	public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] modifyAccountConfirm()");
		
		return adminMemberDao.updateAdminAccount(adminMemberVo);	//Dao결과값을 컨트롤러에 반환.
	}
	//가장 최근의 관리자 정보를 가져오는 역할
	public AdminMemberVo getloginedAdminMemberVo(int a_m_no) {
		System.out.println("[AdminMemberService] getloginedAdminMemberVo()");
		
		return adminMemberDao.selectAdmin(a_m_no);	//관리자 정보(a_m_no)를 전달해서 업데이트된 최신정보 요청.
	}

	public int findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] findPasswordConfirm()");
		
		AdminMemberVo selectAdminMemberVo = 
				adminMemberDao.selectAdmin(adminMemberVo.getA_m_id(),
						adminMemberVo.getA_m_name(),
						adminMemberVo.getA_m_mail());	//selectAdmin()을 이용해 관리자인증을 합니다(관리자가 입력한 id,이름,메일주소로)
	
		int result = 0;
		
		if(selectAdminMemberVo != null) {	//id,이름, 메일주소와 일치하는 관리자가 데이터베이스에 있다면,
			String newPassword = createNewPassword();	//createNewPassword()는 난수를 이용해 새로운 비밀번호 생성.
			result = adminMemberDao.updatePassword(adminMemberVo.getA_m_id(),newPassword);//새로운 비밀번호로 업데이트해서 result로 결과 반환
			
			if(result>0)	//비밀번호 업데이트가 정상적으로 됬다면,
				sendNewPasswordByMail(adminMemberVo.getA_m_mail(),newPassword);	//관리자 메일로 새로운 비밀번호를 발송한다.
		}
		
		return result;
	}

	private String createNewPassword() {
		System.out.println("[AdminMemberService] createNewPassword()");
		
		char[] chars = new char[] {
				'0','1','2','3','4','5','6','7','8','9',
				'a','b','c','d','e','f','g','h','i','j',
				'k','l','m','n','o','p','q','r','s','t',
				'u','v','w','x','y','z'};
		
		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom();		//Random보다 더강력한 난수를 생성하는 클래스.
		secureRandom.setSeed(new Date().getTime());
		
		int index = 0;
		int length = chars.length;
		for(int i=0;i<8;i++) {		//for문은 난수를 8번 생성하고 index에 저장, 이때 index가 짝수라면 대문자로 변경합니다.
			index = secureRandom.nextInt(length);
			
			if(index%2 == 0)
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
		}
		
		System.out.println("[AdminMemberService] NEW PASSWORD: " + stringBuffer.toString());
		
		return stringBuffer.toString();
	}

	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		System.out.println("[AdminMemberService] sendNewPasswordByMail()");
		
		//MimeMessagePreparator 를 구현한 익명 클래스를 생성합니다.
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
//				mimeMessageHelper.setTo(toMailAddr);					//받는 메일주소
				mimeMessageHelper.setTo("sky01091990@gmail.com");		//실습을 위해 내 메일로
				mimeMessageHelper.setSubject("[한국도서관] 새비밀번호 안내입니다");//제목
				mimeMessageHelper.setText("새비밀번호: "+newPassword,true);//내용
			}
		};
		javaMailSenderImpl.send(mimeMessagePreparator);
	}

}

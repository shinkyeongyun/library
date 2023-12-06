package com.office.library.user.member;

import java.security.SecureRandom;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class UserMemberService {
	
	final static public int USER_ACCOUNT_EXIST = 0;
	final static public int USER_ACCOUNT_CREATE_SUCCESS = 1;
	final static public int USER_ACCOUNT_CREATE_FAIL = -1;
	
	@Autowired
	UserMemberDao userMemberDao;
	
	@Autowired
	JavaMailSenderImpl javaMailSenderImpl;

	public int createAccountConfirm(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberService] createAccountConfirm()");
		
		boolean isMember = userMemberDao.isUserMember(userMemberVo.getU_m_id());
		
		if(!isMember) {
			int result = userMemberDao.insertUserAccount(userMemberVo);
			
			if(result > 0)
				return USER_ACCOUNT_CREATE_SUCCESS;
			else
				return USER_ACCOUNT_CREATE_FAIL;
		}else {
			return USER_ACCOUNT_EXIST;
		}
	}

	//로그인
	public UserMemberVo loginConfirm(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberService] loginConfirm()");
		
		UserMemberVo loginedUserMemberVo = userMemberDao.selectUser(userMemberVo);
		
		if(loginedUserMemberVo != null)
			System.out.println("[UserMemberService] USER MEMBER LOGIN SUCCESS");
		else
			System.out.println("[UserMemberService] USER MEMBER LOGIN FAIL!!");
			
		return loginedUserMemberVo;
	}

	public int modifyAccountConfirm(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberService] modifyAccountConfirm()");
		
		return userMemberDao.updateUserAccount(userMemberVo);	//DAO를 통해 회원정보를 업데이터하고 결과 값을 컨트롤러에 반환한다.
	}

	public UserMemberVo getLoginedUserMemberVo(int u_m_no) {
		System.out.println("[UserMemberService] getLoginedUserMemberVo()");
		
		return userMemberDao.selectUser(u_m_no);	//현재 로그인되어있는 사용자의 번호를 Dao에 전달해서 업데이트된 최근 정보를 요청한다.
	}

	public int findPasswordConfirm(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberService] findPasswordConfirm()");
		
		UserMemberVo selectedUserMemberVo = userMemberDao.selectUser(userMemberVo.getU_m_id(),
																userMemberVo.getU_m_name(),
																userMemberVo.getU_m_mail());//사용자가 입력한 정보를 selectUser()를 통해 조회.
		int result = 0;
		
		if(selectedUserMemberVo != null) {	//회원 인증에 성공했다면,
			String newPassword = createNewPassword();	//새로운비밀번호를 생성하고
			result = userMemberDao.updatePassword(userMemberVo.getU_m_id(),newPassword);	//새로운비밀번호로 업데이트
			
			if(result > 0)		//업데이트가 됬다면, 새로운 비밀번호를 메일로 전송
				sendNewPassWordByMail(userMemberVo.getU_m_mail(),newPassword);
		}
		return result;
	}

	

	private String createNewPassword() {
		System.out.println("[UserMemberService] createNewPassword()");
		
		char[] chars = new char[] {
				'0','1','2','3','4','5','6','7','8','9',
				'a','b','c','d','e','f','g','h','i','j',
				'k','l','m','n','o','p','q','r','s','t',
				'u','v','w','x','y','z'
				};
		
		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.setSeed(new Date().getTime());		//new Date() : 현재시간 가져오기 getTime() : 밀리초(long)로 가져오기
		
		int index = 0;
		int length = chars.length;
		for(int i=0;i<8;i++) {
			index = secureRandom.nextInt(length);
			
			if(index%2 == 0)
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
		}
		
		System.out.println("[UserMemberService] NEW PASSWORD: "+stringBuffer.toString());
		
		return stringBuffer.toString();
	}
	
	private void sendNewPassWordByMail(String toMailAddr, String newPassword) {
		System.out.println("[UserMemberService] sendNewPassWordByMail()");
		
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
//				mimeMessageHelper.setTo(toMailAddr);
				mimeMessageHelper.setTo("sky01091990@gmail.com");
				mimeMessageHelper.setSubject("[한국도서관]새로운 비밀번호 안내입니다.");
				mimeMessageHelper.setText("새비밀번호: "+newPassword,true);
			}
		};
		javaMailSenderImpl.send(mimeMessagePreparator);
	}

}

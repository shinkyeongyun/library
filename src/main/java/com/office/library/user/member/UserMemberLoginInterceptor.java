package com.office.library.user.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 * HandlerInterceptor 인터페이스 사용하기(ch13 p541)
 * 
 * 로그인이 안된 상태일때 매번 리다이렉트를 사용한다면 동일한 코드의 반복과 코드 유지 보수 효율성이 떨어질 수 있다.
 * 
 * 그래서 스프링 MVC에서는 HandlerInterceptor 인터페이스를 제공하는데
 * 
 * preHandle(), postHandle(), afterCompletion() 3개의 메서드를 선언하고 있다.
 * 
 * 클래스에서 HandlerInterceptor 인터페이스를 구현해야하는데, 모든 메서드를 재정의해야하는 번거로움이 있기에 
 * 
 * 스프링에서 HandlerInterceptor를 구현한 HandlerInterceptorAdapter 클래스를 상속해서 필요한 메서드만 재정의하면된다.
 * 
 */
public class UserMemberLoginInterceptor extends HandlerInterceptorAdapter {//인터페이스를 구현(implements)하지않고 상속(extends)하고 있다.
	
	@Override		//재정의
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			Object object = session.getAttribute("loginedUserMemberVo");
			if(object != null)		//세션의 회원정보가 null이 아니면 true반환해서 Handler(컨트롤러)실행.
				return true;
		}
		response.sendRedirect(request.getContextPath()+"/user/member/loginForm");//null인 경우 리다이렉트.
		
		return false;
	}
}

/*
 * 인터셉터 클래스(UserMemberLoginInterceptor)를 만들었으니 사용자의 어떤 요청시에 작동해야하되는지 인터셉터를 설정해야한다.
 * 
 * 인터셉터 설정이란 사용자의 특정 요청이 발생했을 때 인터셉터 클래스(UserMemberLoginInterceptor)가 작동하도록 설정하는것
 * 
 * -> sevlet-context.xml에 <interceptors>추가.
 */

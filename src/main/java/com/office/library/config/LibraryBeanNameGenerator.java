package com.office.library.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class LibraryBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		// 해결방법2 : BeanNameGenerator이용하기 (chapter13. p525)
		//인터페이스 추가하여 클래스만든 후 스프링컨테이너가 실행될때 이용할 수 있게 servlet-context.xml에 수정.
		System.out.println(definition.getBeanClassName());
		return definition.getBeanClassName();
	}

}

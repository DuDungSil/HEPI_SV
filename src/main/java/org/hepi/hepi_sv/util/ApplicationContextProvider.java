package org.hepi.hepi_sv.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
	
	private static ApplicationContext ctx;

	@Override
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}
	
	// get Bean with class
	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}
	
	//get Bean with beanName
	public static Object getBean(String beanNm) {
		return ctx.getBean(beanNm);
	}
}

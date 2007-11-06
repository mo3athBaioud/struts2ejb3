package com.googlecode.struts2ejb3;

import java.lang.reflect.Field;

import javax.naming.InitialContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Looks for any fields annotated with the InjectEJB annotation and injects the
 * given EJB into that field.
 */
public class InjectEJBInterceptor extends AbstractInterceptor implements Interceptor {

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Object action = actionInvocation.getAction();
		for (Field f : action.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(InjectEJB.class)) {
				InitialContext ic = new InitialContext();
				String serviceName = f.getType().getName();
				Object service = ic.lookup(serviceName);
				boolean wasAccessible = f.isAccessible();
				f.setAccessible(true);
				f.set(action, f.getType().cast(service));
				f.setAccessible(wasAccessible);
			}
		}
		return actionInvocation.invoke();
	}
}

package com.itmyhome.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统所有注释的容器
 */
public class Annotation {

	/**
	 * 只允许使用POST方式执行的Action方法
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface PostMethod {

	}
	
	/**
	 * 输出JSON格式的提示信息
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface JSONOutput {

	}
}

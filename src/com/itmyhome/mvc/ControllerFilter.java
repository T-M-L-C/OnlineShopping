package com.itmyhome.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局过滤器
 */
public class ControllerFilter implements Filter {

	private ServletContext context;
	
	public void init(FilterConfig cfg) throws ServletException {
		this.context = cfg.getServletContext();
	}

	/**
	 * 执行过滤操作
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
		throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		//初始化RequestContext对象，并将rc放到线程变量中
		RequestContext rc = RequestContext.begin(this.context, request, response);
		
		try {
			chain.doFilter(rc.request(), rc.response());
		} finally {
			//完成一次请求后，释放资料
			if(rc!=null) rc.end();
		}
	}

	public void destroy() {
	}

}

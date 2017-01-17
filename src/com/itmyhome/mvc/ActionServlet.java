package com.itmyhome.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 核心控制器，定义了映射规则
 * 业务处理方法入口，URI的映射逻辑：
 * /action/xxxxxx/xxxx -> com.kzw.web.XxxxxxAction.xxxx(RequestContext rc)
 * 如：/action/user/login  ==> com.kzw.web.UserAction.login(RequestContext rc)
 */
@SuppressWarnings("serial")
public final class ActionServlet extends HttpServlet {

	private final static String ERROR_PAGE = "error_page";
	private final static String GOTO_PAGE = "goto_page";
	private final static String ERROR_MSG = "error_msg";
	
	private final static String UTF_8 = "utf-8";	
	private List<String> action_packages = null;	//action存放的包
	private final static ThreadLocal<Boolean> g_json_enabled = new ThreadLocal<Boolean>();
	
	private final static HashMap<String, Object> actions = new HashMap<String, Object>();
	private final static HashMap<String, Method> methods = new HashMap<String, Method>();
	
	@Override
	public void init() throws ServletException {
		String tmp = getInitParameter("packages");
		action_packages = Arrays.asList(StringUtils.split(tmp, ','));
	}

	@Override
	public void destroy() {
		for(Object action : actions.values()){
			try{
				Method dm = action.getClass().getMethod("destroy");
				if(dm != null){
					dm.invoke(action);
					log("!!!!!!!!! " + action.getClass().getSimpleName() + 
						" destroy !!!!!!!!!");
				}
			}catch(NoSuchMethodException e){
			}catch(Exception e){
				log("Unabled to destroy action: " + action.getClass().getSimpleName(), e);
			}
		}
		super.destroy();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(RequestContext.get(), false);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(RequestContext.get(), true);
	}
	
	/**
	 * 执行Action方法并进行返回处理、异常处理
	 */
	protected void process(RequestContext req, boolean is_post) 
			throws ServletException, IOException {
		try{
			req.response().setContentType("text/html;charset=utf-8");
			if(_process(req, is_post)){ 
				String gp = req.param(GOTO_PAGE);
				if(StringUtils.isNotBlank(gp))
					req.redirect(gp);
			}
		}catch(InvocationTargetException e){
			Throwable t = e.getCause();
			if(t instanceof ActionException)
				handleActionException(req, (ActionException)t);
			else
				throw new ServletException(t);
		}catch(ActionException t){
			handleActionException(req, t);
		}catch(IOException e){
			throw e;
		}catch(Exception e){
			log("Exception in action process.", e);
			throw new ServletException(e);
		}finally{
			g_json_enabled.remove();
		}
	}
	
	/**
	 * Action业务异常
	 */
	protected void handleActionException(RequestContext req, ActionException t)	
		throws ServletException, IOException {		
		handleException(req, t.getMessage());
	}
	
	
	/**
	 * URL解码
	 */
	private static String _DecodeURL(String url, String charset) {
		if (StringUtils.isEmpty(url))
			return "";
		try {
			return URLDecoder.decode(url, charset);
		} catch (Exception e) {
		}
		return url;
	}

	protected void handleException(RequestContext req, String msg) 
		throws ServletException, IOException {
		String ep = req.param(ERROR_PAGE);
		if(StringUtils.isNotBlank(ep)){
			if(ep.charAt(0)=='%')
				ep = _DecodeURL(ep, UTF_8);
			ep = ep.trim();
			if(ep.charAt(0)!='/'){
				req.redirect(req.contextPath()+"/");
			}
			else{
				req.request().setAttribute(ERROR_MSG, msg);
				req.forward(ep.trim());
			}
		}
		else{
			if(g_json_enabled.get())
				req.outputJson("msg", msg);
			else
				req.print(msg);
		}
	}	
	
	/**
	 * 业务逻辑处理
	 * 真正处理业务逻辑的方法，每一个请求都会执行此方法
	 */
	private boolean _process(RequestContext req, boolean is_post)
			 throws InstantiationException,
					IllegalAccessException, 
					IOException, 
					IllegalArgumentException,
					InvocationTargetException
	{
		String requestURI = req.uri();  // -->  /project/action/test/hello
		String path = req.context().getContextPath();
		requestURI= requestURI.replace(path, "/"); // -->  //action/test/hello
		String[] parts = StringUtils.split(requestURI, '/');	
		if(parts.length<2){
			req.notFound();
			return false;
		}
		//加载Action类
		Object action = this._LoadAction(parts[1]);
		if(action == null){
			req.notFound();
			return false;
		}
		String action_method_name = (parts.length>2)?parts[2]:"index";
		Method m_action = this._GetActionMethod(action, action_method_name);
		if(m_action == null){
			req.notFound();
			return false;
		}
		
		//判断action方法是否只支持POST
		if (!is_post && m_action.isAnnotationPresent(Annotation.PostMethod.class)){
			req.notFound();
			return false;
		}
		
		g_json_enabled.set(m_action.isAnnotationPresent(Annotation.JSONOutput.class));
		
	
		//调用Action方法之准备参数
		int arg_c = m_action.getParameterTypes().length;
		switch(arg_c){
		case 0: // login()
			m_action.invoke(action);
			break ;
		case 1:	//login(RequestContext rc)
			m_action.invoke(action, req);
			break;
		case 2: // login(HttpServletRequest req, HttpServletResponse res)
			m_action.invoke(action, req.request(), req.response());
			break ;
		case 3: // login(HttpServletRequest req, HttpServletResponse res, String[] extParams)
			StringBuilder args = new StringBuilder();
			for(int i=3;i<parts.length;i++){
				if(StringUtils.isBlank(parts[i]))
					continue;
				if(args.length() > 0)
					args.append('/');
				args.append(parts[i]);
			}
			boolean isLong = m_action.getParameterTypes()[2].equals(long.class);
			m_action.invoke(action, req.request(), req.response(), isLong ? NumberUtils.toLong(
					args.toString(), -1L) : args.toString());
			break ;
		default:
			req.notFound();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 加载Action类
	 */
	protected Object _LoadAction(String act_name) 
		throws InstantiationException,IllegalAccessException {
		Object action = actions.get(act_name);
		if(action == null){
			for(String pkg : action_packages){
				String cls = pkg + '.' + StringUtils.capitalize(act_name) + "Action";
				action = _LoadActionOfFullname(act_name, cls);
				if(action != null)
					break;
			}
		}
		return action ;
	}
	
	private Object _LoadActionOfFullname(String act_name, String cls) 
		throws IllegalAccessException, InstantiationException {
		
		Object action = null;
		try {								
			action = Class.forName(cls).newInstance();
			try{
				Method action_init_method = action.getClass().getMethod("init", ServletContext.class);
				action_init_method.invoke(action, getServletContext());
			}catch(NoSuchMethodException e){
			}catch(InvocationTargetException excp) {
				excp.printStackTrace();
			}
			if(!actions.containsKey(act_name)){
				synchronized(actions){
					actions.put(act_name, action);
				}
			}
		} catch (ClassNotFoundException excp) {}
		return action;
	}
	
	/**
	 * 获取名为{method}的方法
	 */
	private Method _GetActionMethod(Object action, String method) {
		String key = action.getClass().getSimpleName() + '.' + method;
		Method m = methods.get(key);
		if(m != null) return m;
		for(Method m1 : action.getClass().getMethods()){
			if(m1.getModifiers()==Modifier.PUBLIC && m1.getName().equals(method)){
				synchronized(methods){
					methods.put(key, m1);
				}
				return m1 ;
			}
		}
		return null;
	}

}
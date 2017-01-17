package com.itmyhome.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;


import com.itmyhome.entity.User;
import com.itmyhome.mvc.MultipartRequest;
import com.itmyhome.mvc.RequestContext;
import com.itmyhome.mvc.Annotation.PostMethod;
import com.itmyhome.service.UserService;

public class UserAction {

	private UserService userService = new UserService();
	
	/**
	 * 文件上传�?
	 * 		�?���?��借助第三方的上传组件（Commons组件fileupload, cos�?
	 * 
	 * 文件下载�?
	 * 		1、需要设置response的头信息
	 * 		2、输出response.getOutputStream();
	 * */

	private static int pageNo=0;
	private static int pageSize=5;
	public void upload(RequestContext rc) throws Exception {
		
		User u = rc.form(User.class); //自动将请求参数封装成�?��对象
		System.out.println(u.getUsername());
		System.out.println(u.getPassword());
		
		File file = ((MultipartRequest)rc.request()).getFile("attache");
		
		//将此文件写到指定的文件夹�?
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(new File(RequestContext.root()+"/upload", file.getName()));
		byte[] b = new byte[1024];
		int len;
		while((len=fis.read(b)) != -1) {
			fos.write(b, 0, len);
			fos.flush();
		}
		fos.close();
		fis.close();
		
		rc.forward("/index.jsp");
	}
	//注册
	@PostMethod
	public void register(RequestContext rc) throws ServletException, IOException {
		
		User u = rc.form(User.class);
		userService.register(u);
		pageNo = rc.param("page", 1);
		Map<String,Object> map=this.getModelMap(pageNo,pageSize);
		rc.reqAttr("users", map.get("users"));
		rc.reqAttr("page", map.get("pageNo"));
		rc.reqAttr("pageTotal", map.get("pageTotal"));
		rc.forward("/list_user.jsp");
	}
	//查询
	public void list(RequestContext rc) throws ServletException, IOException {
		pageNo = rc.param("page", 1);

		Map<String,Object> map=this.getModelMap(pageNo,pageSize);
		rc.reqAttr("users", map.get("users"));
		rc.reqAttr("page", map.get("pageNo"));
		rc.reqAttr("pageTotal", map.get("pageTotal"));
		rc.forward("/list_user.jsp");
	}
	//编辑展示
	public void edit(RequestContext rc) throws ServletException,IOException{
		User u = rc.form(User.class); 
		u=userService.get(u.getId());
		rc.reqAttr("user",u);
		rc.forward("/edit_user.jsp");
	}
	//编辑保存
	public void editServlet(RequestContext rc) throws ServletException,IOException{
		User u = rc.form(User.class); 
		userService.saveOrupdate(u);
		pageNo = rc.param("page", 1);
		Map<String,Object> map=this.getModelMap(pageNo,pageSize);
		rc.reqAttr("users", map.get("users"));
		rc.reqAttr("page", map.get("pageNo"));
		rc.reqAttr("pageTotal", map.get("pageTotal"));
		rc.forward("/list_user.jsp");
	}
	//删除
	public void delete(RequestContext rc)throws ServletException ,IOException{
		User u=rc.form(User.class);
		userService.remove(u.getId());
		pageNo = rc.param("page", 1);
		Map<String,Object> map=this.getModelMap(pageNo,pageSize);
		rc.reqAttr("users", map.get("users"));
		rc.reqAttr("page", map.get("pageNo"));
		rc.reqAttr("pageTotal", map.get("pageTotal"));
		rc.forward("/list_user.jsp");
	}
	
	//登录
	public void login(RequestContext rc)throws ServletException,IOException{
		User u=rc.form(User.class);
	    u=userService.validate(u.getUsername(), u.getPassword());
	    if(u!=null){
			pageNo = rc.param("page", 1);
            Map<String,Object> map=this.getModelMap(pageNo,pageSize);
			rc.reqAttr("users",map.get("users"));
			rc.reqAttr("page", map.get("pageNo"));
			rc.reqAttr("pageTotal", map.get("pageTotal"));
			rc.forward("/list_user.jsp");
	    }else {
	    	rc.forward("/error.jsp");
		}
	}

	public Map<String,Object> getModelMap(int pageNo, int pageSize){
		Map<String,Object> map=new HashMap<String,Object>();
		if(pageNo<=0)
			pageNo=1;
		List<User> users = userService.listPage(pageNo, pageSize);
		map.put("users",users);
		map.put("pageNo",pageNo);
		map.put("pageTotal",(userService.list().size()-pageSize+1)/pageSize);
		return map;
	}
}

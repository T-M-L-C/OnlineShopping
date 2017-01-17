package com.itmyhome.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;


import com.itmyhome.entity.User;
import com.itmyhome.mvc.MultipartRequest;
import com.itmyhome.mvc.RequestContext;
import com.itmyhome.mvc.Annotation.JSONOutput;
import com.itmyhome.mvc.Annotation.PostMethod;
import com.itmyhome.service.UserService;

public class TestAction {

	private UserService userService = new UserService();
	
	public void hello(RequestContext rc) throws ServletException, IOException {
		System.out.println("hello world");
		List<User> users = userService.list();
		rc.reqAttr("users", users);
		rc.forward("/list_user.jsp");
	}
	
	@PostMethod
	@JSONOutput
	public void hello2(RequestContext rc) throws IOException {
		System.out.println("hello world");
		rc.outputJson("msg", "你好");
	}
	
	public void hello3(RequestContext rc) {
		System.out.println("hello world");
	}
	
	public void hello4(RequestContext rc) {
		System.out.println("hello world");
	}
	
	/**
	 * 文件上传下载
	 * @throws IOException 
	 * */
	public  void hello5(RequestContext rc) throws IOException {
		System.out.println(rc.request().getParameter("uname"));
		System.out.println(rc.request().getParameter("attache"));
		File f = ((MultipartRequest)rc.request()).getFile("attache");
		FileInputStream fis = new FileInputStream(f);
		
		FileOutputStream fos = new FileOutputStream("d:\\" + f.getName());
		byte[] b = new byte[fis.available()];
		fis.read(b);
		fos.write(b);
		fos.flush();
		fis.close();
		fos.close();
		
	}
}

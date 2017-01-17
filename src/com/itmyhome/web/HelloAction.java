package com.itmyhome.web;

import java.io.IOException;

import javax.servlet.ServletException;

import com.itmyhome.mvc.RequestContext;


public class HelloAction {

	//  --> /action/hello/test1
	public void test1(RequestContext rc) throws ServletException, IOException {
		
		System.out.println("hello");
		rc.forward("/index.jsp");
	}
}

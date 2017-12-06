<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% 
String a = (String)request.getAttribute("xml");
response.setContentType("text/html;charset:UTF-8"); 
response.setHeader("Cache-Control", "no-cache"); 
response.getWriter().print(a); 
%>
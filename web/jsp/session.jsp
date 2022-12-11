<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%
	out.println(session.getAttribute("id"));	// session 내장 객체에 있는 id값을 읽어오는 역할
%>
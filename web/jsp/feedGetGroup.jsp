<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="util.*" %>
<%@ page import="dao.*" %>
<%
	String str = (new FeedDAO()).getGroup(request.getParameter("frids"), request.getParameter("maxNo"));
	out.print(str);
%>
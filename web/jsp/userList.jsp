<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>
<%@ page import="util.*" %>
<%
	String str = (new UserDAO()).getList();	// jsp는 dao에 대한 인터페이스 역할만 함
	
	/* html 생성 파트는 html 쪽으로 보냄 -> 클라이언트에서 실행
	String str = "<table align=center>";
	str += "<tr><th colspan=3>가입자 리스트</th></tr>";
	for (UserObj user : users) {
		str += "<tr><td colspan=3><hr></td></tr>";
		str += "<tr>";
		str += "<td>" + user.getId() + "</td>";
		str += "<td>" + user.getName() + "</td>";
		str += "<td>&nbsp;<small>(" + user.getTs() + ")</small></td>";
		str += "</tr>";
	}
	str += "</table>";
	*/
	out.print(str);
%> 
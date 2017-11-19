<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="opta.boot.cloudbalancing.CloudBalancingAction" %>
<%
  new CloudBalancingAction().solve(session);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="REFRESH" content="0;url=<%=application.getContextPath()%>/cloudbalancing/solving"/>
</head>
<body>
</body>
</html>

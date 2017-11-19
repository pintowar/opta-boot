<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>OptaPlanner webexamples: Cloud Balancing</title>
  <jsp:include page="/WEB-INF/jsp/common/head.jsp"/>

  <!-- HACK to refresh this page automatically every 2 seconds -->
  <!-- TODO: it should only refresh the image -->
  <meta http-equiv="REFRESH" content="2;url=<%=application.getContextPath()%>/cloudbalancing/solving"/>
</head>
<body>

<div class="container">
  <div class="row">
    <div class="col-md-3">
      <jsp:include page="/WEB-INF/jsp/common/menu.jsp"/>
    </div>
    <div class="col-md-9">
      <header class="main-page-header">
        <h1>Cloud balancing</h1>
      </header>
      <p>Assign processes to computers.</p>
      <p>Solving... Below is a temporary solution, refreshed every 2 seconds.</p>
      <div>
        <button class="btn" onclick="window.location.href='<%=application.getContextPath()%>/cloudbalancing/terminateEarly'"><i class="icon-stop"></i> Terminate early</button>
      </div>
      <jsp:include page="cloudBalancingPage.jsp"/>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/foot.jsp"/>
</body>
</html>

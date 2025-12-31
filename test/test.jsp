<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Test JSP - Sprint4-bis</title>
</head>
<body>
  <h1>JSP de test (Sprint4-bis)</h1>
  <p>Cette page est rendue via ModelView et un forward du FrontServlet.</p>
  <p>Message (Sprint5): <strong><%= request.getAttribute("message") %></strong></p>
  <p><a href="/FrameworkResume/home">Accueil</a></p>
</body>
</html>

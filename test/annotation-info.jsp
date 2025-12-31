<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="test.java.AnnotationInfoController.ClassInfo" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title><%= request.getAttribute("titre") %></title>
  <style>
    body {
      font-family: Arial, sans-serif;
      max-width: 900px;
      margin: 50px auto;
      padding: 20px;
      background-color: #f5f5f5;
    }
    .container {
      background-color: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    h1 {
      color: #333;
      border-bottom: 2px solid #4CAF50;
      padding-bottom: 10px;
    }
    .info {
      background-color: #e3f2fd;
      padding: 15px;
      border-left: 4px solid #2196F3;
      margin-bottom: 20px;
    }
    .section {
      margin: 30px 0;
    }
    .section h2 {
      color: #555;
      margin-bottom: 15px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    th, td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    th {
      background-color: #4CAF50;
      color: white;
      font-weight: bold;
    }
    tr:hover {
      background-color: #f5f5f5;
    }
    .has-controller {
      color: #28a745;
      font-weight: bold;
    }
    .no-controller {
      color: #dc3545;
      font-weight: bold;
    }
    .badge {
      display: inline-block;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 0.85em;
    }
    .badge-success {
      background-color: #d4edda;
      color: #155724;
    }
    .badge-danger {
      background-color: #f8d7da;
      color: #721c24;
    }
    .value {
      color: #666;
      font-style: italic;
    }
    a {
      color: #4CAF50;
      text-decoration: none;
      margin-right: 15px;
    }
    a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1><%= request.getAttribute("titre") %></h1>
    
    <div class="info">
      <p><strong>Sprint 2-bis:</strong> Cette page affiche quelles classes ont l'annotation @Controller et lesquelles n'en ont pas.</p>
      <p>Seules les classes avec @Controller sont traitees par le FrontServlet lors du scan.</p>
    </div>
    
    <%
      @SuppressWarnings("unchecked")
      List<ClassInfo> classes = (List<ClassInfo>) request.getAttribute("classes");
      
      if (classes != null) {
        // Separer les classes avec et sans @Controller
        List<ClassInfo> withController = new java.util.ArrayList<>();
        List<ClassInfo> withoutController = new java.util.ArrayList<>();
        
        for (ClassInfo info : classes) {
          if (info.hasController()) {
            withController.add(info);
          } else {
            withoutController.add(info);
          }
        }
    %>
    
    <div class="section">
      <h2>Classes AVEC @Controller (<%= withController.size() %>)</h2>
      <p>Ces classes sont traitees par le FrontServlet et leurs methodes avec @HandleUrl sont mappees.</p>
      <table>
        <thead>
          <tr>
            <th>Nom de la classe</th>
            <th>Annotation @Controller</th>
            <th>Valeur de l'annotation</th>
          </tr>
        </thead>
        <tbody>
          <% for (ClassInfo info : withController) { %>
          <tr>
            <td><strong><%= info.getClassName() %></strong></td>
            <td><span class="badge badge-success has-controller">OUI</span></td>
            <td class="value"><%= info.getControllerValue() != null ? info.getControllerValue() : "(vide)" %></td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
    
    <div class="section">
      <h2>Classes SANS @Controller (<%= withoutController.size() %>)</h2>
      <p>Ces classes sont ignorees par le FrontServlet lors du scan. Leurs methodes ne sont pas accessibles via URL.</p>
      <table>
        <thead>
          <tr>
            <th>Nom de la classe</th>
            <th>Annotation @Controller</th>
            <th>Statut</th>
          </tr>
        </thead>
        <tbody>
          <% for (ClassInfo info : withoutController) { %>
          <tr>
            <td><strong><%= info.getClassName() %></strong></td>
            <td><span class="badge badge-danger no-controller">NON</span></td>
            <td class="value">Ignoree par le FrontServlet</td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
    
    <div class="section">
      <h2>Resume</h2>
      <p><strong>Total de classes verifiees:</strong> <%= classes.size() %></p>
      <p><strong>Classes avec @Controller:</strong> <span class="has-controller"><%= withController.size() %></span></p>
      <p><strong>Classes sans @Controller:</strong> <span class="no-controller"><%= withoutController.size() %></span></p>
    </div>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/home">Accueil</a>
      <a href="/FrameworkResume/annotation-info">Actualiser</a>
    </p>
    
    <% } %>
  </div>
</body>
</html>


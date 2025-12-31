<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="test.java.Emp" %>
<%@ page import="test.java.Departement" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title><%= request.getAttribute("titre") %></title>
  <style>
    body {
      font-family: Arial, sans-serif;
      max-width: 800px;
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
    .success {
      background-color: #d4edda;
      padding: 15px;
      border-left: 4px solid #28a745;
      margin: 20px 0;
    }
    .info {
      background-color: #e3f2fd;
      padding: 15px;
      border-left: 4px solid #2196F3;
      margin: 20px 0;
    }
    .data {
      background-color: #f9f9f9;
      padding: 20px;
      margin: 20px 0;
      border-radius: 4px;
    }
    .data h3 {
      margin-top: 0;
      color: #555;
    }
    .data p {
      margin: 10px 0;
    }
    .label {
      font-weight: bold;
      color: #555;
      display: inline-block;
      min-width: 120px;
    }
    .value {
      color: #333;
    }
    .departement-item {
      background-color: white;
      padding: 15px;
      margin: 10px 0;
      border-left: 3px solid #4CAF50;
      border-radius: 4px;
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
    
    <div class="success">
      <p><strong>✓ <%= request.getAttribute("message") %></strong></p>
    </div>
    
    <div class="info">
      <p><strong>Sprint 8-bis:</strong> L'objet <code>Emp</code> a ete automatiquement instancie et rempli avec les valeurs du formulaire.</p>
      <p>Vous pouvez maintenant utiliser <code>e.getName()</code>, <code>e.getEmail()</code>, etc. directement dans le controleur.</p>
    </div>
    
    <%
      Emp emp = (Emp) request.getAttribute("emp");
    %>
    
    <% if (emp != null) { %>
    <div class="data">
      <h3>Informations de l'employe:</h3>
      
      <p><span class="label">Nom:</span> <span class="value"><%= emp.getName() != null ? emp.getName() : "(vide)" %></span></p>
      <p><span class="label">Email:</span> <span class="value"><%= emp.getEmail() != null ? emp.getEmail() : "(vide)" %></span></p>
      <p><span class="label">Age:</span> <span class="value"><%= emp.getAge() != null ? emp.getAge() : "(vide)" %></span></p>
      
      <% if (emp.getDepartement() != null && !emp.getDepartement().isEmpty()) { %>
      <h3 style="margin-top: 20px;">Departements:</h3>
      <% for (int i = 0; i < emp.getDepartement().size(); i++) { 
           Departement dept = emp.getDepartement().get(i);
      %>
      <div class="departement-item">
        <p><span class="label">Departement <%= i + 1 %> - Nom:</span> <span class="value"><%= dept.getName() != null ? dept.getName() : "(vide)" %></span></p>
        <p><span class="label">Departement <%= i + 1 %> - Code:</span> <span class="value"><%= dept.getCode() != null ? dept.getCode() : "(vide)" %></span></p>
      </div>
      <% } %>
      <% } else { %>
      <p style="color: #666; font-style: italic;">Aucun departement renseigne.</p>
      <% } %>
    </div>
    
    <div class="data" style="background-color: #fff3cd; border-left: 4px solid #ffc107;">
      <h3>Representation de l'objet:</h3>
      <pre style="background-color: #f9f9f9; padding: 10px; border-radius: 4px; overflow-x: auto;"><%= emp.toString() %></pre>
    </div>
    <% } else { %>
    <div class="data">
      <p style="color: #dc3545;">Erreur: L'objet Emp est null.</p>
    </div>
    <% } %>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/emp/save">Creer un autre employe</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>


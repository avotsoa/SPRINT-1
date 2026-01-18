<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="test.java.Emp" %>
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
      border-bottom: 2px solid #2196F3;
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
    .warning {
      background-color: #fff3cd;
      padding: 15px;
      border-left: 4px solid #ffc107;
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
    .operation-badge {
      display: inline-block;
      padding: 5px 15px;
      border-radius: 20px;
      font-weight: bold;
      margin-left: 10px;
    }
    .badge-add {
      background-color: #28a745;
      color: white;
    }
    .badge-get {
      background-color: #2196F3;
      color: white;
    }
    .badge-update {
      background-color: #ffc107;
      color: #333;
    }
    .badge-delete {
      background-color: #f44336;
      color: white;
    }
    a {
      color: #2196F3;
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
    <h1><%= request.getAttribute("titre") %>
      <%
        String operation = (String) request.getAttribute("operation");
        if (operation != null) {
          String badgeClass = "";
          if (operation.equals("AJOUTER")) badgeClass = "badge-add";
          else if (operation.equals("RECUPERER")) badgeClass = "badge-get";
          else if (operation.equals("MODIFIER")) badgeClass = "badge-update";
          else if (operation.equals("SUPPRIMER")) badgeClass = "badge-delete";
      %>
      <span class="operation-badge <%= badgeClass %>"><%= operation %></span>
      <% } %>
    </h1>
    
    <div class="success">
      <p><strong>✓ <%= request.getAttribute("message") %></strong></p>
    </div>
    
    <div class="info">
      <p><strong>Sprint 11:</strong> Opération de session effectuée avec succès.</p>
      <p>Les données sont maintenant <%= operation != null && operation.equals("SUPPRIMER") ? "supprimées de" : "stockées dans" %> la session HTTP.</p>
    </div>
    
    <%
      Emp emp = (Emp) request.getAttribute("emp");
      if (emp != null) {
    %>
    <div class="data">
      <h3>Informations de l'employé:</h3>
      
      <p><span class="label">Nom:</span> <span class="value"><%= emp.getName() != null ? emp.getName() : "(vide)" %></span></p>
      <p><span class="label">Email:</span> <span class="value"><%= emp.getEmail() != null ? emp.getEmail() : "(vide)" %></span></p>
      <p><span class="label">Age:</span> <span class="value"><%= emp.getAge() != null ? emp.getAge() : "(vide)" %></span></p>
    </div>
    
    <div class="data" style="background-color: #fff3cd; border-left: 4px solid #ffc107;">
      <h3>Représentation de l'objet:</h3>
      <pre style="background-color: #f9f9f9; padding: 10px; border-radius: 4px; overflow-x: auto;"><%= emp.toString() %></pre>
    </div>
    <% } else if (operation != null && operation.equals("RECUPERER")) { %>
    <div class="warning">
      <p><strong>⚠ Aucun employé trouvé dans la session.</strong></p>
      <p>Veuillez d'abord ajouter un employé en session.</p>
    </div>
    <% } %>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/emp/session">Retour à la gestion de session</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>

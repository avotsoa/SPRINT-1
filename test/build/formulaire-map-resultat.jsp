<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
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
      padding: 15px;
      margin: 20px 0;
      border-radius: 4px;
    }
    .data table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    .data th, .data td {
      padding: 10px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    .data th {
      background-color: #4CAF50;
      color: white;
      font-weight: bold;
    }
    .data tr:hover {
      background-color: #f5f5f5;
    }
    .label {
      font-weight: bold;
      color: #555;
    }
    .value {
      color: #333;
      font-family: monospace;
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
      <p><strong>Sprint 8:</strong> Cette page demontre l'utilisation de Map&lt;String,String&gt; dans le controleur.</p>
      <p>La Map contient automatiquement tous les parametres de request.getParameter().</p>
      <p><strong>Nombre de parametres recus:</strong> <%= request.getAttribute("paramsCount") %></p>
    </div>
    
    <%
      @SuppressWarnings("unchecked")
      Map<String, String> params = (Map<String, String>) request.getAttribute("params");
    %>
    
    <div class="data">
      <h3>Parametres recus dans Map&lt;String,String&gt;:</h3>
      <% if (params != null && !params.isEmpty()) { %>
      <table>
        <thead>
          <tr>
            <th>Nom du parametre</th>
            <th>Valeur</th>
          </tr>
        </thead>
        <tbody>
          <% for (Map.Entry<String, String> entry : params.entrySet()) { %>
          <tr>
            <td class="label"><%= entry.getKey() %></td>
            <td class="value"><%= entry.getValue() != null ? entry.getValue() : "(vide)" %></td>
          </tr>
          <% } %>
        </tbody>
      </table>
      <% } else { %>
      <p>Aucun parametre recu.</p>
      <% } %>
    </div>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/formulaire-map">Retour au formulaire</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>


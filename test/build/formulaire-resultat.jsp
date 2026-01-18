<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title><%= request.getAttribute("titre") %></title>
  <style>
    body {
      font-family: Arial, sans-serif;
      max-width: 600px;
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
      background-color: #fff3cd;
      padding: 15px;
      border-left: 4px solid #ffc107;
      margin: 20px 0;
    }
    .data {
      background-color: #f9f9f9;
      padding: 15px;
      margin: 20px 0;
      border-radius: 4px;
    }
    .data p {
      margin: 8px 0;
    }
    .label {
      font-weight: bold;
      color: #555;
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
      <p><strong>Sprint 7:</strong> Cette page a ete generee via une requete <strong>POST</strong>.</p>
      <p>Le formulaire a ete traite par la methode <code>@PostMapping("/formulaire")</code>.</p>
    </div>
    
    <div class="data">
      <h3>Donnees recues:</h3>
      <p><span class="label">Nom:</span> <%= request.getParameter("nom") != null ? request.getParameter("nom") : "Non fourni" %></p>
      <p><span class="label">Email:</span> <%= request.getParameter("email") != null ? request.getParameter("email") : "Non fourni" %></p>
      <p><span class="label">Message:</span> <%= request.getParameter("message") != null ? request.getParameter("message") : "Non fourni" %></p>
    </div>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/formulaire">Retour au formulaire</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>


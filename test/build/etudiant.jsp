<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Details de l'Etudiant - Sprint 6</title>
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
    .info {
      margin: 20px 0;
      padding: 15px;
      background-color: #f9f9f9;
      border-left: 4px solid #4CAF50;
    }
    .info p {
      margin: 8px 0;
    }
    .label {
      font-weight: bold;
      color: #555;
    }
    .value {
      color: #333;
    }
    .message {
      color: #4CAF50;
      font-style: italic;
      margin-top: 20px;
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
    <h1>Details de l'Etudiant</h1>
    
    <div class="info">
      <p><span class="label">ID:</span> <span class="value"><%= request.getAttribute("id") %></span></p>
      <p><span class="label">Nom:</span> <span class="value"><%= request.getAttribute("nom") %></span></p>
      <p><span class="label">Email:</span> <span class="value"><%= request.getAttribute("email") %></span></p>
    </div>
    
    <p class="message"><%= request.getAttribute("message") %></p>
    
    <p>
      <a href="/FrameworkResume/etudiants">Liste des etudiants</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
    
    <hr style="margin-top: 30px; border: none; border-top: 1px solid #ddd;">
    <p style="color: #888; font-size: 0.9em; margin-top: 20px;">
      <strong>Sprint 6:</strong> Cette page demontre la gestion des parametres d'URL dynamiques.<br>
      L'URL <code>/etudiant/{id}</code> a ete mappee a la methode <code>get(int id)</code>.
    </p>
  </div>
</body>
</html>


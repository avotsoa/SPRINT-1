<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title><%= request.getAttribute("titre") %></title>
  <style>
    body {
      font-family: Arial, sans-serif;
      max-width: 700px;
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
    .form-group {
      margin: 20px 0;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
      color: #555;
    }
    input[type="text"],
    input[type="email"],
    input[type="number"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }
    button {
      background-color: #2196F3;
      color: white;
      padding: 12px 24px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
      margin-top: 10px;
    }
    button:hover {
      background-color: #0b7dda;
    }
    .info {
      background-color: #fff3cd;
      padding: 15px;
      border-left: 4px solid #ffc107;
      margin-bottom: 20px;
    }
    .departement-section {
      background-color: #f9f9f9;
      padding: 15px;
      margin: 20px 0;
      border-radius: 4px;
      border: 1px solid #ddd;
    }
    .departement-section h3 {
      margin-top: 0;
      color: #555;
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
    <h1><%= request.getAttribute("titre") %></h1>
    
    <div class="info">
      <p><strong>Sprint 9:</strong> Ce formulaire retourne les donnees en format JSON.</p>
      <p>Contrairement au Sprint 8-bis qui affiche une page HTML, cette methode retourne directement du JSON.</p>
    </div>
    
    <p><%= request.getAttribute("message") %></p>
    
    <form action="/FrameworkResume/emp/json" method="POST">
      <h2>Informations de l'employe</h2>
      
      <div class="form-group">
        <label for="e.name">Nom:</label>
        <input type="text" id="e.name" name="e.name" required>
        <small style="color: #666;">Convention: e.name (e = nom du parametre dans la methode)</small>
      </div>
      
      <div class="form-group">
        <label for="e.email">Email:</label>
        <input type="email" id="e.email" name="e.email" required>
      </div>
      
      <div class="form-group">
        <label for="e.age">Age:</label>
        <input type="number" id="e.age" name="e.age" min="18" max="100">
      </div>
      
      <div class="departement-section">
        <h3>Departements</h3>
        <p style="font-size: 0.9em; color: #666;">Utilisation de la notation e.departement[0].name pour les tableaux/listes</p>
        
        <div class="form-group">
          <label for="e.departement[0].name">Departement 1 - Nom:</label>
          <input type="text" id="e.departement[0].name" name="e.departement[0].name">
        </div>
        
        <div class="form-group">
          <label for="e.departement[0].code">Departement 1 - Code:</label>
          <input type="text" id="e.departement[0].code" name="e.departement[0].code">
        </div>
        
        <div class="form-group">
          <label for="e.departement[1].name">Departement 2 - Nom:</label>
          <input type="text" id="e.departement[1].name" name="e.departement[1].name">
        </div>
        
        <div class="form-group">
          <label for="e.departement[1].code">Departement 2 - Code:</label>
          <input type="text" id="e.departement[1].code" name="e.departement[1].code">
        </div>
      </div>
      
      <button type="submit">Envoyer (Retour JSON)</button>
    </form>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/emp/save">Sprint 8-bis (HTML)</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>

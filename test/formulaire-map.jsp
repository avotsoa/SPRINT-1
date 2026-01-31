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
    textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }
    textarea {
      height: 100px;
      resize: vertical;
    }
    button {
      background-color: #4CAF50;
      color: white;
      padding: 12px 24px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background-color: #45a049;
    }
    .info {
      background-color: #fff3cd;
      padding: 15px;
      border-left: 4px solid #ffc107;
      margin-bottom: 20px;
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
      <p><strong>Sprint 8:</strong> Ce formulaire demontre l'utilisation de Map&lt;String,String&gt; dans le controleur.</p>
      <p>Tous les parametres du formulaire seront automatiquement places dans une Map.</p>
    </div>
    
    <p><%= request.getAttribute("message") %></p>
    
    <form action="/FrameworkResume/formulaire-map" method="POST">
      <div class="form-group">
        <label for="nom">Nom:</label>
        <input type="text" id="nom" name="nom" required>
      </div>
      
      <div class="form-group">
        <label for="prenom">Prenom:</label>
        <input type="text" id="prenom" name="prenom" required>
      </div>
      
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
      </div>
      
      <div class="form-group">
        <label for="telephone">Telephone:</label>
        <input type="text" id="telephone" name="telephone">
      </div>
      
      <div class="form-group">
        <label for="adresse">Adresse:</label>
        <textarea id="adresse" name="adresse"></textarea>
      </div>
      
      <div class="form-group">
        <label for="ville">Ville:</label>
        <input type="text" id="ville" name="ville">
      </div>
      
      <button type="submit">Envoyer (POST avec Map)</button>
    </form>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/home">Accueil</a>
      <a href="/FrameworkResume/formulaire">Formulaire normal</a>
    </p>
  </div>
</body>
</html>


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
      background-color: #e3f2fd;
      padding: 15px;
      border-left: 4px solid #2196F3;
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
      <p><strong>Sprint 7:</strong> Cette page demontre la differentiation des methodes HTTP.</p>
      <p>Cette page est accessible via <strong>GET</strong> uniquement.</p>
      <p>Le formulaire ci-dessous sera soumis via <strong>POST</strong>.</p>
    </div>
    
    <p><%= request.getAttribute("message") %></p>
    
    <form action="/FrameworkResume/formulaire" method="POST">
      <div class="form-group">
        <label for="nom">Nom:</label>
        <input type="text" id="nom" name="nom" required>
      </div>
      
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
      </div>
      
      <div class="form-group">
        <label for="message">Message:</label>
        <textarea id="message" name="message" required></textarea>
      </div>
      
      <button type="submit">Envoyer (POST)</button>
    </form>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/home">Accueil</a>
      <a href="/FrameworkResume/liste">Liste (GET)</a>
    </p>
  </div>
</body>
</html>


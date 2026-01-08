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
    input[type="file"] {
      width: 100%;
      padding: 10px;
      border: 2px dashed #ddd;
      border-radius: 4px;
      background-color: #f9f9f9;
      cursor: pointer;
    }
    input[type="file"]:hover {
      border-color: #4CAF50;
      background-color: #f0f8f0;
    }
    button {
      background-color: #4CAF50;
      color: white;
      padding: 12px 24px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
      margin-top: 10px;
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
    .note {
      background-color: #fff3cd;
      padding: 10px;
      margin-top: 10px;
      border-radius: 4px;
      font-size: 0.9em;
      color: #666;
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
      <p><strong>Sprint 10:</strong> Ce formulaire demontre l'upload de fichiers.</p>
      <p>Les fichiers seront automatiquement places dans une Map&lt;String, Byte[]&gt; dans le controleur.</p>
    </div>
    
    <p><%= request.getAttribute("message") %></p>
    
    <form action="/FrameworkResume/upload" method="POST" enctype="multipart/form-data">
      <div class="form-group">
        <label for="fichier1">Fichier 1:</label>
        <input type="file" id="fichier1" name="fichier1" accept="*/*">
        <div class="note">Le nom du champ (fichier1) sera la cle dans la Map</div>
      </div>
      
      <div class="form-group">
        <label for="fichier2">Fichier 2 (optionnel):</label>
        <input type="file" id="fichier2" name="fichier2" accept="*/*">
        <div class="note">Vous pouvez uploader plusieurs fichiers</div>
      </div>
      
      <div class="form-group">
        <label for="document">Document (optionnel):</label>
        <input type="file" id="document" name="document" accept=".pdf,.doc,.docx,.txt">
        <div class="note">Types acceptes: PDF, DOC, DOCX, TXT</div>
      </div>
      
      <button type="submit">Uploader les fichiers</button>
    </form>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>


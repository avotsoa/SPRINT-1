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
    h2 {
      color: #555;
      margin-top: 30px;
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
      margin: 10px 5px 10px 0;
    }
    button:hover {
      background-color: #0b7dda;
    }
    button.secondary {
      background-color: #4CAF50;
    }
    button.secondary:hover {
      background-color: #45a049;
    }
    button.danger {
      background-color: #f44336;
    }
    button.danger:hover {
      background-color: #da190b;
    }
    .info {
      background-color: #fff3cd;
      padding: 15px;
      border-left: 4px solid #ffc107;
      margin-bottom: 20px;
    }
    .session-info {
      background-color: #e3f2fd;
      padding: 15px;
      border-left: 4px solid #2196F3;
      margin: 20px 0;
    }
    a {
      color: #2196F3;
      text-decoration: none;
      margin-right: 15px;
    }
    a:hover {
      text-decoration: underline;
    }
    .operations {
      display: flex;
      gap: 10px;
      margin: 20px 0;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1><%= request.getAttribute("titre") %></h1>
    
    <div class="info">
      <p><strong>Sprint 11:</strong> Gestion de la session HTTP dans le controller.</p>
      <p>Vous pouvez ajouter, récupérer, modifier et supprimer des données dans la session.</p>
    </div>
    
    <p><%= request.getAttribute("message") %></p>
    
    <%
      Emp empSession = (Emp) request.getAttribute("empSession");
      if (empSession != null) {
    %>
    <div class="session-info">
      <h3>Employé actuellement en session:</h3>
      <p><strong>Nom:</strong> <%= empSession.getName() != null ? empSession.getName() : "(vide)" %></p>
      <p><strong>Email:</strong> <%= empSession.getEmail() != null ? empSession.getEmail() : "(vide)" %></p>
      <p><strong>Age:</strong> <%= empSession.getAge() != null ? empSession.getAge() : "(vide)" %></p>
    </div>
    <% } else { %>
    <div class="session-info">
      <p><em>Aucun employé en session actuellement.</em></p>
    </div>
    <% } %>
    
    <h2>Opérations de session</h2>
    
    <div class="operations">
      <form action="/FrameworkResume/emp/session/get" method="GET" style="display: inline;">
        <button type="submit" class="secondary">Récupérer de la session</button>
      </form>
      
      <form action="/FrameworkResume/emp/session/delete" method="GET" style="display: inline;">
        <button type="submit" class="danger">Supprimer de la session</button>
      </form>
    </div>
    
    <h2>Ajouter/Modifier un employé en session</h2>
    
    <form action="/FrameworkResume/emp/session/add" method="POST">
      <div class="form-group">
        <label for="e.name">Nom:</label>
        <input type="text" id="e.name" name="e.name" value="<%= empSession != null && empSession.getName() != null ? empSession.getName() : "" %>" required>
      </div>
      
      <div class="form-group">
        <label for="e.email">Email:</label>
        <input type="email" id="e.email" name="e.email" value="<%= empSession != null && empSession.getEmail() != null ? empSession.getEmail() : "" %>" required>
      </div>
      
      <div class="form-group">
        <label for="e.age">Age:</label>
        <input type="number" id="e.age" name="e.age" value="<%= empSession != null && empSession.getAge() != null ? empSession.getAge() : "" %>" min="18" max="100">
      </div>
      
      <button type="submit">Ajouter en session</button>
    </form>
    
    <% if (empSession != null) { %>
    <form action="/FrameworkResume/emp/session/update" method="POST" style="margin-top: 10px;">
      <input type="hidden" name="e.name" value="<%= empSession.getName() != null ? empSession.getName() : "" %>">
      <input type="hidden" name="e.email" value="<%= empSession.getEmail() != null ? empSession.getEmail() : "" %>">
      <input type="hidden" name="e.age" value="<%= empSession.getAge() != null ? empSession.getAge() : "" %>">
      <p><em>Ou modifiez les valeurs ci-dessus et cliquez sur "Modifier en session"</em></p>
    </form>
    <% } %>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/home">Accueil</a>
      <a href="/FrameworkResume/emp/save">Sprint 8-bis</a>
      <a href="/FrameworkResume/emp/json">Sprint 9</a>
    </p>
  </div>
</body>
</html>

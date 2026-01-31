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
      max-width: 900px;
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
    .data table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    .data th, .data td {
      padding: 12px;
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
    .size {
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
    
    <div class="success">
      <p><strong>✓ <%= request.getAttribute("message") %></strong></p>
    </div>
    
    <div class="info">
      <p><strong>Sprint 10:</strong> Les fichiers ont ete recus dans une Map&lt;String, Byte[]&gt;.</p>
      <p><strong>Nombre de fichiers:</strong> <%= request.getAttribute("filesCount") %></p>
      <p><strong>Taille totale:</strong> <span class="size"><%= formatSize((Long) request.getAttribute("totalSize")) %></span></p>
    </div>
    
    <%
      @SuppressWarnings("unchecked")
      Map<String, Byte[]> files = (Map<String, Byte[]>) request.getAttribute("files");
    %>
    
    <div class="data">
      <h3>Fichiers recus:</h3>
      <% if (files != null && !files.isEmpty()) { %>
      <table>
        <thead>
          <tr>
            <th>Nom du champ</th>
            <th>Taille (bytes)</th>
            <th>Taille (formatee)</th>
          </tr>
        </thead>
        <tbody>
          <% for (Map.Entry<String, Byte[]> entry : files.entrySet()) { 
               Byte[] fileBytes = entry.getValue();
               long size = fileBytes != null ? fileBytes.length : 0;
          %>
          <tr>
            <td class="label"><%= entry.getKey() %></td>
            <td class="value"><%= size %></td>
            <td class="size"><%= formatSize(size) %></td>
          </tr>
          <% } %>
        </tbody>
      </table>
      <% } else { %>
      <p>Aucun fichier recu.</p>
      <% } %>
    </div>
    
    <p style="margin-top: 30px;">
      <a href="/FrameworkResume/upload">Uploader d'autres fichiers</a>
      <a href="/FrameworkResume/home">Accueil</a>
    </p>
  </div>
</body>
</html>

<%!
  private String formatSize(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    } else if (bytes < 1024 * 1024) {
      return String.format("%.2f KB", bytes / 1024.0);
    } else {
      return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
  }
%>


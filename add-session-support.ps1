# Script pour ajouter le support de HttpSession (Sprint 11) dans FrontServlet.java

$filePath = "framework\src\com\framework\FrontServlet.java"
$content = Get-Content $filePath -Raw

# Texte à rechercher
$searchText = @"
            Class<?> paramType = param.getType();
            
            // Sprint 10: Vérifier si le paramètre est de type Map<String,Byte[]>
"@

# Texte de remplacement
$replaceText = @"
            Class<?> paramType = param.getType();
            
            // Sprint 11: Vérifier si le paramètre est de type HttpSession
            if (paramType == HttpSession.class) {
                // Passer la session de la requête
                args[i] = req.getSession();
                continue;
            }
            
            // Sprint 10: Vérifier si le paramètre est de type Map<String,Byte[]>
"@

# Effectuer le remplacement
$newContent = $content.Replace($searchText, $replaceText)

# Vérifier si le remplacement a été effectué
if ($content -eq $newContent) {
    Write-Host "ERREUR: Aucun remplacement effectué. Le texte recherché n'a pas été trouvé." -ForegroundColor Red
    exit 1
} else {
    # Sauvegarder le fichier
    Set-Content -Path $filePath -Value $newContent -NoNewline
    Write-Host "SUCCESS: Support de HttpSession ajouté dans FrontServlet.java" -ForegroundColor Green
}

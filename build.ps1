# Script de compilation et deploiement du projet resume
# Ce script compile le framework, les tests, et genere un WAR

$APP_NAME = "FrameworkResume"

# === CHEMINS ===
$FRAMEWORK_SRC = "framework\src"
$FRAMEWORK_BUILD = "framework\build"
$FRAMEWORK_JAR = "framework.jar"

$TEST_SRC = "test\java"
$TEST_BUILD = "test\build"
$TEST_WEBINF = "test\WEB-INF"

# Emplacement du servlet-api.jar utilise pour la compilation (present dans le projet)
$SERVLET_API = "test\WEB-INF\lib\servlet-api.jar"

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "  COMPILATION DU FRAMEWORK RESUME" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# === ETAPE 1: Compilation du Framework ===
Write-Host "[1/5] Compilation du framework..." -ForegroundColor Yellow

# Nettoyer et creer build
if (Test-Path $FRAMEWORK_BUILD) {
    Remove-Item -Path $FRAMEWORK_BUILD -Recurse -Force
}
New-Item -Path $FRAMEWORK_BUILD -ItemType Directory -Force | Out-Null

# Lister tous les fichiers Java du framework
$frameworkFiles = Get-ChildItem -Path $FRAMEWORK_SRC -Filter "*.java" -Recurse | 
                  Select-Object -ExpandProperty FullName

if ($frameworkFiles.Count -eq 0) {
    Write-Host "[ERREUR] Aucun fichier Java trouve dans le framework!" -ForegroundColor Red
    exit 1
}

Write-Host "   Fichiers Java trouves: $($frameworkFiles.Count)" -ForegroundColor Gray

# Creer fichier sources temporaire
[System.IO.File]::WriteAllLines("sources_framework.txt", $frameworkFiles, 
                                (New-Object System.Text.UTF8Encoding $false))

# Compiler
javac -cp $SERVLET_API -d $FRAMEWORK_BUILD "@sources_framework.txt"
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] Erreur de compilation du framework!" -ForegroundColor Red
    Remove-Item "sources_framework.txt"
    exit 1
}
Remove-Item "sources_framework.txt"

Write-Host "   [OK] Framework compile" -ForegroundColor Green

# === ETAPE 2: Creation du JAR ===
Write-Host "[2/5] Creation du JAR du framework..." -ForegroundColor Yellow

jar -cvf $FRAMEWORK_JAR -C $FRAMEWORK_BUILD . | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] Erreur lors de la creation du JAR!" -ForegroundColor Red
    exit 1
}

Write-Host "   [OK] JAR cree: $FRAMEWORK_JAR" -ForegroundColor Green

# === ETAPE 3: Compilation des tests ===
Write-Host "[3/5] Compilation des classes de test..." -ForegroundColor Yellow

# Nettoyer et creer build
if (Test-Path $TEST_BUILD) {
    Remove-Item -Path $TEST_BUILD -Recurse -Force
}
New-Item -Path "$TEST_BUILD\WEB-INF\classes" -ItemType Directory -Force | Out-Null
New-Item -Path "$TEST_BUILD\WEB-INF\lib" -ItemType Directory -Force | Out-Null

# Lister fichiers Java des tests
$testFiles = Get-ChildItem -Path $TEST_SRC -Filter "*.java" -Recurse | 
             Select-Object -ExpandProperty FullName

if ($testFiles.Count -eq 0) {
    Write-Host "[ERREUR] Aucun fichier de test trouve!" -ForegroundColor Red
    exit 1
}

Write-Host "   Fichiers de test trouves: $($testFiles.Count)" -ForegroundColor Gray

[System.IO.File]::WriteAllLines("sources_test.txt", $testFiles, 
                                (New-Object System.Text.UTF8Encoding $false))

# Compiler avec le framework JAR dans le classpath
javac -cp "$SERVLET_API;$FRAMEWORK_JAR" -d "$TEST_BUILD\WEB-INF\classes" "@sources_test.txt"
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] Erreur de compilation des tests!" -ForegroundColor Red
    Remove-Item "sources_test.txt"
    exit 1
}
Remove-Item "sources_test.txt"

Write-Host "   [OK] Tests compiles" -ForegroundColor Green

# === ETAPE 4: Assemblage du WAR ===
Write-Host "[4/5] Assemblage du WAR..." -ForegroundColor Yellow

# Copier web.xml
Copy-Item -Path "$TEST_WEBINF\web.xml" -Destination "$TEST_BUILD\WEB-INF\" -Force

# Copier le framework JAR dans WEB-INF/lib
Copy-Item -Path $FRAMEWORK_JAR -Destination "$TEST_BUILD\WEB-INF\lib\" -Force

# Copier index.html
Copy-Item -Path "test\index.html" -Destination $TEST_BUILD -Force

Write-Host "   [OK] Fichiers assembles" -ForegroundColor Green

# === ETAPE 5: Generation du WAR ===
Write-Host "[5/5] Generation du fichier WAR..." -ForegroundColor Yellow

jar -cvf "$TEST_BUILD\$APP_NAME.war" -C $TEST_BUILD . | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] Erreur lors de la creation du WAR!" -ForegroundColor Red
    exit 1
}

Write-Host "   [OK] WAR genere: $TEST_BUILD\$APP_NAME.war" -ForegroundColor Green

# === VERIFICATION ===
Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "  VERIFICATION DU CONTENU" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

$warContent = jar tf "$TEST_BUILD\$APP_NAME.war"

# Verifier les fichiers principaux
$checks = @(
    @{Name="framework.jar"; Pattern="WEB-INF/lib/framework.jar"},
    @{Name="TestController.class"; Pattern="TestController.class"},
    @{Name="SimpleClass.class"; Pattern="SimpleClass.class"},
    @{Name="web.xml"; Pattern="WEB-INF/web.xml"},
    @{Name="index.html"; Pattern="index.html"}
)

$allOk = $true
foreach ($check in $checks) {
    if ($warContent -match $check.Pattern) {
        Write-Host "[OK] $($check.Name) present" -ForegroundColor Green
    }
    else {
        Write-Host "[MANQUANT] $($check.Name) manquant!" -ForegroundColor Red
        $allOk = $false
    }
}

Write-Host ""
if ($allOk) {
    Write-Host "BUILD REUSSI!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Fichier WAR: $TEST_BUILD\$APP_NAME.war" -ForegroundColor Cyan
    Write-Host ""
    
    # === DEPLOIEMENT AUTOMATIQUE DANS TOMCAT ===
    Write-Host "===============================================" -ForegroundColor Cyan
    Write-Host "  DEPLOIEMENT AUTOMATIQUE" -ForegroundColor Cyan
    Write-Host "===============================================" -ForegroundColor Cyan
    
    # Charger la configuration si elle existe
    $configTomcat = ""
    if (Test-Path "config.ps1") {
        . .\config.ps1
        $configTomcat = $TOMCAT_HOME
    }
    
    # Chemins Tomcat possibles
    $tomcatPaths = @(
        $configTomcat,
        "$env:CATALINA_HOME",
        "C:\apache-tomcat-10.1.28",
        "C:\apache-tomcat-10.1.30",
        "C:\apache-tomcat-10.0",
        "C:\Program Files\Apache Software Foundation\Tomcat 10.1",
        "C:\Program Files\Apache Software Foundation\Tomcat 10.0",
        "C:\tomcat"
    )
    
    $tomcatFound = $false
    $tomcatWebapps = $null
    
    foreach ($path in $tomcatPaths) {
        if ($path -and (Test-Path "$path\webapps")) {
            $tomcatWebapps = "$path\webapps"
            $tomcatFound = $true
            Write-Host "Tomcat detecte: $path" -ForegroundColor Green
            break
        }
    }
    
    if ($tomcatFound) {
        try {
            # Supprimer l'ancien WAR et dossier deploye si existants
            if (Test-Path "$tomcatWebapps\$APP_NAME.war") {
                Remove-Item "$tomcatWebapps\$APP_NAME.war" -Force
                Write-Host "Ancien WAR supprime" -ForegroundColor Gray
            }
            if (Test-Path "$tomcatWebapps\$APP_NAME") {
                Remove-Item "$tomcatWebapps\$APP_NAME" -Recurse -Force
                Write-Host "Ancien dossier deploye supprime" -ForegroundColor Gray
            }
            
            # Copier le nouveau WAR
            Copy-Item "$TEST_BUILD\$APP_NAME.war" -Destination $tomcatWebapps -Force
            Write-Host ""
            Write-Host "[OK] WAR deploye dans Tomcat!" -ForegroundColor Green
            Write-Host "     $tomcatWebapps\$APP_NAME.war" -ForegroundColor Gray
            Write-Host ""
            Write-Host "Pour tester:" -ForegroundColor Yellow
            Write-Host "  1. Demarrez Tomcat si ce n'est pas fait" -ForegroundColor White
            Write-Host "  2. Accedez a: http://localhost:8080/$APP_NAME" -ForegroundColor Cyan
            Write-Host ""
        }
        catch {
            Write-Host "[ERREUR] Impossible de copier le WAR: $_" -ForegroundColor Red
        }
    }
    else {
        Write-Host "[INFO] Tomcat non detecte automatiquement" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Solutions:" -ForegroundColor Yellow
        Write-Host "  1. Editez config.ps1 et specifiez le chemin Tomcat" -ForegroundColor Cyan
        Write-Host "  2. Ou definissez CATALINA_HOME" -ForegroundColor Cyan
        Write-Host "  3. Ou deployez manuellement:" -ForegroundColor Cyan
        Write-Host "     - Copiez $TEST_BUILD\$APP_NAME.war" -ForegroundColor White
        Write-Host "     - Dans le dossier webapps de Tomcat" -ForegroundColor White
        Write-Host "     - Demarrez Tomcat" -ForegroundColor White
        Write-Host "     - Accedez a http://localhost:8080/$APP_NAME" -ForegroundColor White
        Write-Host ""
    }
}
else {
    Write-Host "Build termine avec avertissements" -ForegroundColor Yellow
}

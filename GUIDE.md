# Guide du Framework Résumé

## 📁 Structure du Projet

```
resumer/
├── framework/           # Code source du framework
│   └── src/
│       └── com/
│           ├── annotations/     # Annotations @Controller et @HandleUrl
│           │   ├── Controller.java
│           │   └── HandleUrl.java
│           └── framework/       # Classes principales du framework
│               ├── ClassScanner.java    # Scan des classes du classpath
│               ├── FrontServlet.java    # Servlet principal
│               └── Mapping.java         # Association URL <-> Méthode
│
├── test/                # Application de test
│   ├── java/            # Contrôleurs de test
│   │   ├── TestController.java    # Contrôleur avec @Controller
│   │   └── SimpleClass.java       # Classe sans @Controller
│   ├── WEB-INF/
│   │   └── web.xml      # Configuration de l'application
│   └── index.html       # Page d'accueil
│
└── build.ps1            # Script de compilation (UN SEUL fichier!)
```

## 🚀 Compilation et Déploiement

### Méthode Rapide: Build et Déploiement Automatique

```powershell
cd resumer
.\build.ps1
```

Le script effectue automatiquement:
1. Compilation du framework (5 fichiers Java)
2. Création du framework.jar
3. Compilation des classes de test (2 fichiers Java)
4. Assemblage de l'application
5. Génération du WAR: `test\build\FrameworkResume.war`
6. **🎯 Déploiement automatique dans Tomcat** (si détecté)

### Configuration du Chemin Tomcat

Si Tomcat n'est pas détecté automatiquement, éditez le fichier **`config.ps1`**:

```powershell
# Spécifiez le chemin de votre Tomcat
$TOMCAT_HOME = "C:\apache-tomcat-10.1.28"
```

Le script cherche automatiquement Tomcat dans ces emplacements:
- Chemin configuré dans `config.ps1`
- Variable d'environnement `CATALINA_HOME`
- `C:\apache-tomcat-10.1.28`
- `C:\Program Files\Apache Software Foundation\Tomcat 10.1`
- Autres emplacements courants

### Déploiement Manuel (si nécessaire)

1. Copiez `test\build\FrameworkResume.war` dans le dossier `webapps` de Tomcat
2. Démarrez Tomcat
3. Accédez à: http://localhost:8080/FrameworkResume

## 📚 Comment Fonctionne le Framework

### 1. Annotations

#### @Controller
Marque une classe comme contrôleur:
```java
@Controller("MonController")
public class TestController {
    // ...
}
```

#### @HandleUrl
Mappe une méthode à une URL:
```java
@HandleUrl("/home")
public String home() {
    return "<html>...</html>";
}
```

### 2. FrontServlet (Servlet Principal)

Au démarrage:
1. Lit le paramètre `controllerPackage` depuis web.xml
2. Utilise `ClassScanner` pour scanner toutes les classes du package
3. Détecte les classes avec `@Controller`
4. Pour chaque classe, trouve les méthodes avec `@HandleUrl`
5. Crée un mapping URL → Méthode

À chaque requête HTTP:
1. Extrait l'URL demandée
2. Cherche dans les mappings
3. Si trouvé: invoque la méthode et retourne le résultat
4. Sinon: cherche une ressource statique ou affiche 404

### 3. ClassScanner

Scanne le classpath pour trouver des classes:
- `scanPackage(String)` : trouve toutes les classes d'un package
- `filterByAnnotation(List, Class)` : filtre par annotation
- Utilise la réflexion Java pour charger les classes

### 4. Mapping

Simple POJO qui associe:
- Une instance de contrôleur (Object)
- Une méthode à invoquer (Method)

## 🎯 URLs Disponibles

Après déploiement, les URLs suivantes sont disponibles:

- `/` ou `/index.html` → Page d'accueil avec design moderne
- `/home` → Page d'accueil du contrôleur (TestController)
- `/about` → Page "À propos"
- `/contact` → Page de contact
- `/api/test` → Endpoint API (SimpleClass)

## 🔧 Configuration

### web.xml

Le fichier `test/WEB-INF/web.xml` configure le FrontServlet:

```xml
<servlet>
    <servlet-name>FrontServlet</servlet-name>
    <servlet-class>com.framework.FrontServlet</servlet-class>
    <init-param>
        <param-name>controllerPackage</param-name>
        <param-value>test.java</param-value>  <!-- Package à scanner -->
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>FrontServlet</servlet-name>
    <url-pattern>/</url-pattern>  <!-- Intercepte toutes les requêtes -->
</servlet-mapping>
```

## 🎬 Workflow Complet

### Première Installation

1. **Configurer Tomcat** (une seule fois):
```powershell
# Editez config.ps1
notepad config.ps1
# Changez $TOMCAT_HOME avec votre chemin Tomcat
```

2. **Compiler et déployer**:
```powershell
.\build.ps1
```

3. **Démarrer Tomcat** (si pas déjà démarré):
```powershell
# Aller dans le dossier Tomcat
cd C:\apache-tomcat-10.1.28\bin
.\startup.bat
```

4. **Tester l'application**:
- Ouvrir le navigateur: http://localhost:8080/FrameworkResume

### Développement Itératif

1. Modifier le code (ex: ajouter une méthode dans TestController.java)
2. Exécuter `.\build.ps1` - le WAR est automatiquement redéployé
3. Rafraîchir le navigateur pour voir les changements

**C'est tout! Plus besoin de copier manuellement le WAR!** 🎉

## ✨ Différences avec le Projet Original

### Simplifications
1. **Un seul script** : `build.ps1` au lieu de multiples fichiers .bat
2. **Déploiement automatique** : Plus besoin de copier manuellement le WAR
3. **Configuration simple** : Un fichier `config.ps1` pour personnaliser
4. **Pas de README multiples** : Documentation intégrée dans ce guide
5. **Code commenté** : Chaque classe a des commentaires détaillés
6. **Structure claire** : Séparation framework/test évidente
7. **Moins de fichiers** : Seulement l'essentiel

### Fonctionnalités Identiques
- Annotations @Controller et @HandleUrl
- Scan automatique des contrôleurs
- Mapping d'URLs
- Invocation de méthodes par réflexion
- Support des ressources statiques

## 💡 Ajouter un Nouveau Contrôleur

1. Créer une classe dans `test/java/`:
```java
package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;

@Controller("MonNouveauController")
public class MonController {
    
    @HandleUrl("/mapage")
    public String maPage() {
        return "<html><body><h1>Ma nouvelle page</h1></body></html>";
    }
}
```

2. Recompiler:
```powershell
.\build.ps1
```

3. Redéployer le WAR sur Tomcat

4. Accéder à: http://localhost:8080/FrameworkResume/mapage

## 🐛 Debugging

Le FrontServlet affiche des logs au démarrage:
```
[Framework] Démarrage du scan du package: test.java
[Framework] Classes scannées: X
[Framework] Contrôleurs détectés:
  - TestController (TestController)
[Framework] Mapping: /home -> TestController.home()
[Framework] Mapping: /about -> TestController.about()
...
[Framework] Scan terminé. X mappings trouvés.
```

Vérifiez les logs Tomcat pour voir si le scan fonctionne.

## 📝 Notes Importantes

1. **Servlet API** : Le framework utilise Jakarta Servlet API (version 5.0)
2. **Java Version** : Nécessite Java 11+ (pour les text blocks `"""`)
3. **Tomcat** : Testé sur Tomcat 10.x
4. **Encodage** : Fichiers en UTF-8

## 🎓 Apprentissage

Ce projet est idéal pour comprendre:
- Les annotations Java personnalisées
- La réflexion (Reflection API)
- Le pattern Front Controller
- Le scan de classpath
- L'architecture MVC
- Les servlets Jakarta EE

## 📊 Comparaison

| Aspect | Projet Original | Résumé |
|--------|----------------|--------|
| Scripts build | Multiples .bat/.ps1 | 1 seul build.ps1 |
| README | 3+ fichiers | 1 seul GUIDE.md |
| Lignes de code | ~500+ | ~400 |
| Commentaires | Peu | Détaillés |
| Structure | Complexe | Simple et claire |

---

**Projet créé pour démonstration et apprentissage du framework MVC avec annotations.**

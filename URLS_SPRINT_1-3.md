# URLs pour tester les Sprint 1-3

## Configuration de base

**Context Path:** `/FrameworkResume`  
**Base URL:** `http://localhost:8080/FrameworkResume`

## Sprint 1: FrontServlet de base

Le FrontServlet intercepte **toutes les URLs** via le pattern `/*` dans web.xml.

### URLs de test Sprint 1

- `http://localhost:8080/FrameworkResume/` - Page d'accueil (cherche index.html/index.jsp)
- `http://localhost:8080/FrameworkResume/test` - URL non mappee (affiche page d'erreur personnalisee)
- `http://localhost:8080/FrameworkResume/any-url` - Toute URL non mappee est interceptee

**Comportement:**
- Si l'URL correspond a un mapping -> execute la methode
- Si l'URL correspond a une ressource statique (JSP, HTML) -> affiche la ressource
- Sinon -> affiche la page d'erreur personnalisee

## Sprint 2: Annotation @HandleUrl

Les methodes avec `@HandleUrl` sont mappees aux URLs.

### URLs mappees avec @HandleUrl

#### TestController (@Controller + @HandleUrl)

1. **`/home`** - Page d'accueil
   - URL: `http://localhost:8080/FrameworkResume/home`
   - Methode: `TestController.home()`
   - Retour: String (HTML direct)
   - **Teste:** Sprint 2 (@HandleUrl) + Sprint 3 (mapping automatique)

2. **`/mv`** - Test ModelView
   - URL: `http://localhost:8080/FrameworkResume/mv`
   - Methode: `TestController.mv()`
   - Retour: ModelView (forward vers test.jsp)
   - **Teste:** Sprint 2 (@HandleUrl) + Sprint 4-bis (ModelView)

#### UtilisateurController (@Controller + @HandleUrl)

3. **`/utilisateurs`** - Liste des utilisateurs
   - URL: `http://localhost:8080/FrameworkResume/utilisateurs`
   - Methode: `UtilisateurController.liste()`
   - Retour: String (HTML direct)
   - **Teste:** Sprint 2 (@HandleUrl) + Sprint 2-bis (@Controller) + Sprint 3 (scan)

## Sprint 2-bis: Annotation @Controller au niveau classe

Toutes les classes doivent avoir `@Controller` pour etre traitees.

### Classes avec @Controller (verifiees automatiquement)

- `TestController` - `@Controller("TestController")`
- `UtilisateurController` - `@Controller("UtilisateurController")`
- `EtudiantController` - `@Controller("EtudiantController")` (Sprint 6)
- `FormulaireController` - `@Controller("FormulaireController")` (Sprint 7)

**Verification:** Le FrontServlet ignore automatiquement les classes sans `@Controller`.

## Sprint 3: Scan automatique au demarrage

Le scan se fait dans `init()` du FrontServlet.

### Configuration

- **Package a scanner:** `test.java` (defini dans web.xml)
- **Moment du scan:** Au demarrage de l'application (load-on-startup: 1)
- **Resultat:** Mapping automatique URL -> (Classe, Methode)

### URLs qui demontrent le scan automatique

Toutes les URLs ci-dessus demontrent le scan automatique car elles sont:
1. Scannees au demarrage
2. Mappees automatiquement dans `urlMappings`
3. Executees lors de la requete

## Resume des URLs Sprint 1-3

### URLs principales (Sprint 1-3)

| URL | Controller | Methode | Sprint teste |
|-----|-----------|---------|--------------|
| `/` | FrontServlet | service() | Sprint 1 |
| `/home` | TestController | home() | Sprint 2, 3 |
| `/utilisateurs` | UtilisateurController | liste() | Sprint 2, 2-bis, 3 |
| `/mv` | TestController | mv() | Sprint 2, 3, 4-bis |

### URLs de test

**Sprint 1 - Interception de toutes les URLs:**
```
http://localhost:8080/FrameworkResume/
http://localhost:8080/FrameworkResume/test
http://localhost:8080/FrameworkResume/any-url
```

**Sprint 2 - URLs mappees avec @HandleUrl:**
```
http://localhost:8080/FrameworkResume/home
http://localhost:8080/FrameworkResume/utilisateurs
http://localhost:8080/FrameworkResume/mv
```

**Sprint 2-bis - Verification @Controller:**
- Toutes les URLs ci-dessus verifient automatiquement @Controller
- Les classes sans @Controller sont ignorees

**Sprint 3 - Scan automatique:**
- Toutes les URLs ci-dessus sont scannees au demarrage
- Le mapping est cree automatiquement dans `urlMappings`

## Exemple de test complet

### Test 1: Sprint 1 - Interception
```
GET http://localhost:8080/FrameworkResume/test-url
-> FrontServlet intercepte la requete
-> Affiche page d'erreur si non mappee
```

### Test 2: Sprint 2 - @HandleUrl
```
GET http://localhost:8080/FrameworkResume/home
-> FrontServlet trouve le mapping
-> Execute TestController.home()
-> Retourne HTML
```

### Test 3: Sprint 2-bis - @Controller
```
Le FrontServlet scanne test.java
-> Trouve TestController avec @Controller
-> Traite la classe
-> Ignore les classes sans @Controller
```

### Test 4: Sprint 3 - Scan automatique
```
Au demarrage de l'application:
-> FrontServlet.init() est appele
-> scanControllers("test.java") est execute
-> Toutes les classes avec @Controller sont scannees
-> Toutes les methodes avec @HandleUrl sont mappees
-> HashMap urlMappings est rempli
```

## Notes importantes

1. **Toutes les URLs doivent commencer par `/FrameworkResume`** (context path)
2. **Le scan se fait au demarrage** - les erreurs apparaissent dans les logs
3. **Les classes sans @Controller sont ignorees** - pas d'erreur, juste ignore
4. **Les methodes sans @HandleUrl ne sont pas mappees** - elles existent mais ne sont pas accessibles via URL


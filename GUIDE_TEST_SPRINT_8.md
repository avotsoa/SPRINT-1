# Guide de test - Sprint 8: Map<String,String> dans les methodes

## Vue d'ensemble

Le Sprint 8 permet de recevoir automatiquement tous les parametres d'un formulaire dans une `Map<String,String>` en tant que parametre de methode.

## Pre-requis

1. Compiler le projet avec `build.ps1`
2. Deployer le WAR dans Tomcat
3. Demarrer Tomcat

## Methode 1: Test via le navigateur (recommandee)

### Etape 1: Acceder au formulaire

Ouvrez votre navigateur et allez a:
```
http://localhost:8080/FrameworkResume/formulaire-map
```

Vous devriez voir un formulaire avec plusieurs champs:
- Nom
- Prenom
- Email
- Telephone
- Adresse
- Ville

### Etape 2: Remplir le formulaire

Remplissez les champs avec des valeurs de test:
- **Nom:** Dupont
- **Prenom:** Jean
- **Email:** jean.dupont@example.com
- **Telephone:** 0123456789
- **Adresse:** 123 Rue de la Paix
- **Ville:** Paris

### Etape 3: Soumettre le formulaire

Cliquez sur le bouton "Envoyer (POST avec Map)".

### Etape 4: Verifier les resultats

Vous devriez voir une page de resultat qui affiche:
- Un message de succes
- Un tableau avec tous les parametres recus dans la Map
- Le nombre total de parametres

Le tableau devrait contenir:
| Nom du parametre | Valeur |
|------------------|--------|
| nom | Dupont |
| prenom | Jean |
| email | jean.dupont@example.com |
| telephone | 0123456789 |
| adresse | 123 Rue de la Paix |
| ville | Paris |

## Methode 2: Test avec curl (ligne de commande)

### Test POST avec parametres

```bash
curl -X POST http://localhost:8080/FrameworkResume/formulaire-map \
  -d "nom=Dupont" \
  -d "prenom=Jean" \
  -d "email=jean.dupont@example.com" \
  -d "telephone=0123456789" \
  -d "adresse=123 Rue de la Paix" \
  -d "ville=Paris"
```

Cela devrait retourner la page HTML avec les resultats.

## Methode 3: Test avec des outils de developpement

### Utiliser Postman ou Insomnia

1. Creer une nouvelle requete POST
2. URL: `http://localhost:8080/FrameworkResume/formulaire-map`
3. Body type: `x-www-form-urlencoded`
4. Ajouter les parametres:
   - nom: Dupont
   - prenom: Jean
   - email: jean.dupont@example.com
   - telephone: 0123456789
   - adresse: 123 Rue de la Paix
   - ville: Paris
5. Envoyer la requete

## Verification du code

### Code du controleur

Le code dans `FormulaireController.java`:

```java
@PostMapping("/formulaire-map")
public ModelView traiterFormulaireAvecMap(Map<String, String> params) {
    // La Map 'params' contient automatiquement tous les parametres
    ModelView mv = new ModelView("/formulaire-map-resultat.jsp");
    mv.addObject("params", params);
    mv.addObject("paramsCount", params != null ? params.size() : 0);
    return mv;
}
```

### Code du FrontServlet

Le FrontServlet detecte automatiquement les parametres de type `Map<String,String>` et les remplit avec tous les parametres de la requete.

## Tests a effectuer

### Test 1: Formulaire complet
- [ ] Remplir tous les champs
- [ ] Verifier que tous les parametres apparaissent dans la Map
- [ ] Verifier que les valeurs sont correctes

### Test 2: Formulaire partiel
- [ ] Remplir seulement quelques champs (ex: nom et email)
- [ ] Verifier que seuls les champs remplis apparaissent dans la Map
- [ ] Verifier que les champs vides ne sont pas dans la Map

### Test 3: Parametres supplementaires
- [ ] Ajouter des parametres via l'URL: `?extra=value`
- [ ] Verifier que ces parametres apparaissent aussi dans la Map

### Test 4: Verification du type
- [ ] Verifier que la Map est bien de type `Map<String,String>`
- [ ] Verifier que toutes les valeurs sont des String

## Points a verifier

1. **La Map est creee automatiquement** - Pas besoin de l'instancier dans le code
2. **Tous les parametres sont inclus** - Tous les parametres de `request.getParameter()` sont dans la Map
3. **Les valeurs sont correctes** - Les valeurs correspondent aux donnees du formulaire
4. **Le nombre de parametres est correct** - Le compteur affiche le bon nombre

## Debugging

### Si la Map est vide

1. Verifier que la requete est bien en POST
2. Verifier que les champs du formulaire ont bien un attribut `name`
3. Verifier les logs du serveur pour les erreurs

### Si certains parametres manquent

1. Verifier que les champs ont bien un attribut `name`
2. Verifier que les valeurs ne sont pas vides
3. Verifier que le formulaire est bien soumis

### Logs a verifier

Dans les logs de Tomcat, vous devriez voir:
- Le scan des controllers au demarrage
- Les erreurs eventuelles lors de l'invocation des methodes

## Exemple de resultat attendu

Apres avoir soumis le formulaire, la page devrait afficher:

```
Resultat avec Map - Sprint 8

✓ Formulaire traite avec Map<String,String> (Sprint 8)

Sprint 8: Cette page demontre l'utilisation de Map<String,String> dans le controleur.
La Map contient automatiquement tous les parametres de request.getParameter().
Nombre de parametres recus: 6

Parametres recus dans Map<String,String>:

| Nom du parametre | Valeur                    |
|------------------|---------------------------|
| nom              | Dupont                    |
| prenom           | Jean                      |
| email            | jean.dupont@example.com    |
| telephone        | 0123456789                |
| adresse          | 123 Rue de la Paix        |
| ville            | Paris                     |
```

## URLs de test

- **GET (Formulaire):** `http://localhost:8080/FrameworkResume/formulaire-map`
- **POST (Traitement):** `http://localhost:8080/FrameworkResume/formulaire-map`

## Conclusion

Le Sprint 8 fonctionne correctement si:
- ✅ La Map est automatiquement remplie avec tous les parametres
- ✅ Tous les parametres du formulaire sont accessibles via `params.get("nom")`
- ✅ La page de resultat affiche correctement tous les parametres
- ✅ Le nombre de parametres est correct


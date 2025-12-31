Avoir 2 repertoires de travail:
- code du framework 
- code de test du framework

generer un jar et mettre dans la lib du framwork de test
le code source du framework doit etre dans git

creer une branche git (Sprint1)
creer une pull request et l accepter soit meme

```java
DeptController()
@url("/dept/liste")
```

Prerequis:
- Apprendre reflect
- Apprendre annotation

Fichier de config:
- xml
- gradle
- json
- par annotation

annotation attribut, class, method
recuperation d annotation
creer une class annotation

Dans le sprint1:
- tous les url pointent vers une methode
- si le web ne le trouve pas , il pointe vers la methode

Solution: creer un servlet pointant vers /*

### Sprint1
FrontServlet : affiche l url entrer 
mettre dans web.xml FrontServletm (Test)
service(Req, Res)
doGet et doPost appel de service

### Sprint2
Objectifs: 
- Il faut connaitre dans notre framework tous les url dans le projet test
	- Creer une annotation qui prends en parametre une url
	- Tester avec un main , pour savoir si on a obtenue une url

### Sprint2-bis
Dans test:
- Annotation niveau class
- list des class  @controller (verifier nos annotations ) atao ao anatin ny main aloha ny fonction,
- donne la liste des packages

### Sprint3
Avoir les controller et les url dans le controller (Au demarrage)
nos code doivent etre demarrer apres le demarrage de test ->
scan toutes les class et method 
mettre dans init de frontServlet
url class associe avec 
methods dans la class
Mapp

cle : url , value : class Method
mettre dans servlet-context apres le scan 

### Sprint4
Quand on connait la class et l url , on l execute la methode
connaitre la valeur de retour
si de type string afficher avec un printWriter
si de type model and view appel ModelView (Sprint4-bis)
Invocation

```java
public String nom() {
...
return x;
}
```
### Sprint4-bis

Dans le framework creer une class ModelView avec une attribut view(String) 
methode qui retourne ModelView 
dans urlMothod la fonction qui retourne ModelView ou String
creer test.jsp

### Sprint5
Dans modelView mettre map data<String,Object>
addObject, addString
setAttribute(object)

### Sprint6
comment on prend les donnees du formulaire vers un controller
/etudiant/{id}

dans etudiantcontroller aveoir get associe avec une @url("/etudiant/{id}")
get(int id){}  
- pas dans request,getParameter pour prendre les fonction 
- ajouter une verification d accolade , pas d exception , mettre null dabord (Sprint3-ter) 
- si meme nom que les variables dans requestparam , met directement comme valeur (peux utiliser une librairie pour la conversion du String ) valeu null sinon (Sprint6)

### Sprint6-bis
### Sprint6-ter

### Sprint7
Mettre en valeur le requette de la methode http
differencier Post et Get (requestMapping, UrlMapping, getMapping, postMapping)

### Sprint8
Reprise de sprint6 -> donnee du vue vers controller
Si il y a une variable map dans la method map dans le controller <String,String>
La map est la copire du request.getparameter , method pour voir tout les nom de getParameter (Bouclena)

Ex
```java
@Postjika("save")
public void save(Map<String,String>...)
{
}
```


*A voir*
Stresstest

### Sprint8-bis
Passer en argument l objet qu on instancie

```java
@Postjika("save")
public void save(Emp e)
{
	//On peux directement utiliser e.getName() par exemple
}
```

Pour cela on utilise cette convention dans la vue

```html
<form action="/lien" method="POST">
	<input name="e.name" ...>
	<input name="e.departement[0].name">
	<input name="e.departement[1].name">
	<button>Submit</button>
</form>
```

### Sprint 10
Upload de fichier
Dans le frontservlet , verifier si il y a fichier attacher en request comme getParameter -> getParch pour avoir un tableau de Parch (peux devenir un tableau de byte) on peux aussi avoir le nom

on a alors un Map<nom,Byte>

```java
@Postjika("save")
public void save(Map<String,Byte[]>...)
{
}
```


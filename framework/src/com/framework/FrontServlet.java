package com.framework;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import com.annotations.HandleUrl;
import com.annotations.GetMapping;
import com.annotations.PostMapping;
import com.annotations.RequestMapping;
import com.annotations.Controller;
import com.framework.ModelView;

public class FrontServlet extends HttpServlet {
    private RequestDispatcher defaultDispatcher;
    private static final List<String> INDEX_FILES = Arrays.asList(
        "/index.html", "/index.htm", "/index.jsp"
    );
    private HashMap<String, Mapping> urlMappings = new HashMap<>();
    private List<UrlPattern> urlPatterns = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
        
        // Récupérer le package à scanner depuis web.xml
        String packageToScan = getInitParameter("controllerPackage");
        if (packageToScan != null && !packageToScan.isEmpty()) {
            try {
                scanControllers(packageToScan);
            } catch (Exception e) {
                throw new ServletException("Erreur lors du scan des contrôleurs", e);
            }
        }
    }

    private void scanControllers(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);
        
        if (resource == null) {
            return;
        }
        
        File directory = new File(resource.getFile());
        if (!directory.exists()) {
            return;
        }
        
        scanDirectory(directory, packageName);
    }

    private void scanDirectory(File directory, String packageName) throws Exception {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                processClass(className);
            }
        }
    }

    private void processClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        
        // Sprint 2-bis: Vérifier si la classe a l'annotation @Controller
        if (!clazz.isAnnotationPresent(Controller.class)) {
            // Si la classe n'a pas l'annotation @Controller, on l'ignore
            return;
        }
        
        Object instance = clazz.getDeclaredConstructor().newInstance();
        
        for (Method method : clazz.getDeclaredMethods()) {
            String url = null;
            String httpMethod = "GET"; // Par défaut GET pour compatibilité avec @HandleUrl
            
            // Vérifier les nouvelles annotations (Sprint 7) en priorité
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping annotation = method.getAnnotation(GetMapping.class);
                url = annotation.value();
                httpMethod = "GET";
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping annotation = method.getAnnotation(PostMapping.class);
                url = annotation.value();
                httpMethod = "POST";
            } else if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                url = annotation.value();
                httpMethod = annotation.method().toUpperCase();
            } else if (method.isAnnotationPresent(HandleUrl.class)) {
                // Ancienne annotation (Sprint 1-6) - GET par défaut pour compatibilité
                HandleUrl annotation = method.getAnnotation(HandleUrl.class);
                url = annotation.value();
                httpMethod = "GET";
            }
            
            if (url != null && !url.isEmpty()) {
                Mapping mapping = new Mapping(instance, method, httpMethod);
                
                // Vérifier si l'URL contient des paramètres dynamiques {param}
                if (url.contains("{") && url.contains("}")) {
                    // URL avec paramètres dynamiques
                    urlPatterns.add(new UrlPattern(url, mapping, httpMethod));
                } else {
                    // URL statique simple - utiliser une clé composite pour gérer GET/POST
                    String key = httpMethod + ":" + url;
                    urlMappings.put(key, mapping);
                    // Pour compatibilité avec l'ancien code, aussi stocker sans méthode HTTP
                    // (mais seulement si c'est GET pour éviter les conflits)
                    if ("GET".equals(httpMethod)) {
                        urlMappings.put(url, mapping);
                    }
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        String requestMethod = req.getMethod().toUpperCase(); // GET, POST, etc.

        // Vérifier si l'URL correspond à un mapping statique avec la méthode HTTP
        // D'abord essayer avec la clé composite (méthode:url)
        String compositeKey = requestMethod + ":" + path;
        if (urlMappings.containsKey(compositeKey)) {
            Mapping mapping = urlMappings.get(compositeKey);
            if (matchesHttpMethod(mapping, requestMethod)) {
                handleAnnotatedMethod(mapping, req, res);
                return;
            }
        }
        
        // Ensuite essayer sans méthode HTTP (pour compatibilité avec ancien code)
        if (urlMappings.containsKey(path)) {
            Mapping mapping = urlMappings.get(path);
            if (matchesHttpMethod(mapping, requestMethod)) {
                handleAnnotatedMethod(mapping, req, res);
                return;
            }
        }

        // Vérifier si l'URL correspond à un pattern avec paramètres dynamiques
        UrlPattern matchedPattern = findMatchingPattern(path, requestMethod);
        if (matchedPattern != null) {
            handleAnnotatedMethodWithParams(matchedPattern, path, req, res);
            return;
        }

        if (path.equals("/") || path.isEmpty()) {
            String indexPath = findExistingIndex();
            if (indexPath != null) {
                req.getRequestDispatcher(indexPath).forward(req, res);
                return;
            } else {
                customServe(req, res);
                return;
            }
        }

        boolean resourceExists = getServletContext().getResource(path) != null;
        if (resourceExists) {
            defaultServe(req, res);
        } else {
            customServe(req, res);
        }
    }

    /**
     * Vérifie si la méthode HTTP de la requête correspond à celle du mapping
     */
    private boolean matchesHttpMethod(Mapping mapping, String requestMethod) {
        String mappingMethod = mapping.getHttpMethod();
        return mappingMethod != null && mappingMethod.equalsIgnoreCase(requestMethod);
    }

    private void handleAnnotatedMethod(Mapping mapping, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        handleAnnotatedMethod(mapping, req, res, new HashMap<>());
    }

    private void handleAnnotatedMethod(Mapping mapping, HttpServletRequest req, HttpServletResponse res, Map<String, String> urlParams)
            throws ServletException, IOException {
        try {
            Method method = mapping.getMethod();
            Object controller = mapping.getController();
            
            // Préparer les arguments pour la méthode
            Object[] args = prepareMethodArguments(method, urlParams, req);
            
            // Invoquer la méthode avec les arguments
            Object result = method.invoke(controller, args);

            res.setCharacterEncoding("UTF-8");
            if (result instanceof String) {
                res.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = res.getWriter()) {
                    out.print((String) result);
                }
            } else if (result instanceof ModelView) {
                ModelView mv = (ModelView) result;
                String view = mv.getView();
                if (view == null || view.isEmpty()) {
                    throw new ServletException("ModelView sans nom de vue");
                }
                if (mv.getData() != null) {
                    for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                req.getRequestDispatcher(view).forward(req, res);
            } else {
                res.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = res.getWriter()) {
                    out.println("<html><body><h1>Méthode exécutée avec succès</h1></body></html>");
                }
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'invocation de la méthode", e);
        }
    }

    private void handleAnnotatedMethodWithParams(UrlPattern pattern, String requestPath, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Map<String, String> urlParams = extractUrlParameters(pattern.getPattern(), requestPath);
        handleAnnotatedMethod(pattern.getMapping(), req, res, urlParams);
    }

    /**
     * Trouve un pattern d'URL qui correspond au chemin demandé et à la méthode HTTP
     */
    private UrlPattern findMatchingPattern(String requestPath, String requestMethod) {
        for (UrlPattern pattern : urlPatterns) {
            if (matchesPattern(pattern.getPattern(), requestPath) && 
                matchesHttpMethod(pattern.getHttpMethod(), requestMethod)) {
                return pattern;
            }
        }
        return null;
    }

    /**
     * Vérifie si la méthode HTTP correspond
     */
    private boolean matchesHttpMethod(String patternMethod, String requestMethod) {
        return patternMethod != null && patternMethod.equalsIgnoreCase(requestMethod);
    }

    /**
     * Vérifie si le chemin correspond au pattern (avec gestion des {param})
     */
    private boolean matchesPattern(String pattern, String path) {
        // Convertir le pattern en regex
        // Ex: /etudiant/{id} -> /etudiant/([^/]+)
        String regex = pattern.replaceAll("\\{[^}]+\\}", "([^/]+)");
        return Pattern.matches(regex, path);
    }

    /**
     * Extrait les paramètres de l'URL selon le pattern
     * Ex: pattern="/etudiant/{id}", path="/etudiant/123" -> {"id": "123"}
     */
    private Map<String, String> extractUrlParameters(String pattern, String path) {
        Map<String, String> params = new HashMap<>();
        
        // Extraire les noms de paramètres du pattern
        List<String> paramNames = new ArrayList<>();
        Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = paramPattern.matcher(pattern);
        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }
        
        // Convertir le pattern en regex et extraire les valeurs
        String regex = pattern.replaceAll("\\{[^}]+\\}", "([^/]+)");
        Pattern pathPattern = Pattern.compile(regex);
        Matcher pathMatcher = pathPattern.matcher(path);
        
        if (pathMatcher.matches()) {
            for (int i = 0; i < paramNames.size() && i < pathMatcher.groupCount(); i++) {
                String paramName = paramNames.get(i);
                String paramValue = pathMatcher.group(i + 1); // group(0) est le match complet
                params.put(paramName, paramValue);
            }
        }
        
        return params;
    }

    /**
     * Prépare les arguments pour l'invocation de la méthode
     * Gère les méthodes sans paramètres (anciennes fonctionnalités) et avec paramètres (Sprint 6)
     * Sprint 8: Gère les paramètres Map<String,String> qui reçoivent tous les paramètres de la requête
     */
    private Object[] prepareMethodArguments(Method method, Map<String, String> urlParams, HttpServletRequest req) {
        Parameter[] parameters = method.getParameters();
        
        // Si la méthode n'a pas de paramètres, retourner un tableau vide (anciennes fonctionnalités)
        if (parameters.length == 0) {
            return new Object[0];
        }
        
        Object[] args = new Object[parameters.length];
        
        // Sprint 8: Créer une Map avec tous les paramètres de la requête
        Map<String, String> allRequestParams = new HashMap<>();
        
        // Ajouter les paramètres d'URL
        allRequestParams.putAll(urlParams);
        
        // Ajouter tous les paramètres de requête (request.getParameter)
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            allRequestParams.put(paramName, req.getParameter(paramName));
        }
        
        // Sprint 10: Extraire les fichiers uploadés
        Map<String, Byte[]> uploadedFiles = extractUploadedFiles(req);
        
        // Pour chaque paramètre de la méthode
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName = param.getName();
            Class<?> paramType = param.getType();
            
            // Sprint 10: Vérifier si le paramètre est de type Map<String,Byte[]>
            if (isMapStringByteArray(paramType, param)) {
                // Créer une nouvelle Map avec tous les fichiers uploadés
                args[i] = new HashMap<>(uploadedFiles);
                continue;
            }
            
            // Sprint 8: Vérifier si le paramètre est de type Map<String,String>
            if (isMapStringString(paramType, param)) {
                // Créer une nouvelle Map avec tous les paramètres de la requête
                args[i] = new HashMap<>(allRequestParams);
                continue;
            }
            
            // Sprint 8-bis: Vérifier si le paramètre est un objet (pas primitif, pas String, pas Map)
            if (isObjectType(paramType)) {
                // Instancier et remplir l'objet avec les paramètres de la requête
                args[i] = instantiateAndFillObject(paramType, paramName, allRequestParams);
                continue;
            }
            
            // Vérifier si le nom du paramètre est disponible (compilé avec -parameters)
            // Si non disponible, param.getName() retourne "arg0", "arg1", etc.
            // Dans ce cas, on ne peut pas faire de matching par nom, donc on utilise null
            boolean isRealName = paramName != null && !paramName.startsWith("arg");
            
            String stringValue = null;
            if (isRealName) {
                // Chercher la valeur dans les paramètres (URL ou requête) par nom
                stringValue = allRequestParams.get(paramName);
            }
            // Si le nom n'est pas disponible ou la valeur n'est pas trouvée, stringValue reste null
            
            // Convertir en type approprié (retourne valeur par défaut si null)
            args[i] = convertValue(stringValue, paramType);
        }
        
        return args;
    }
    
    /**
     * Sprint 8: Vérifie si un paramètre est de type Map<String,String>
     */
    private boolean isMapStringString(Class<?> paramType, Parameter param) {
        // Vérifier si c'est une Map
        if (!Map.class.isAssignableFrom(paramType)) {
            return false;
        }
        
        // Vérifier les types génériques via Type
        try {
            Type genericType = param.getParameterizedType();
            if (genericType instanceof java.lang.reflect.ParameterizedType) {
                java.lang.reflect.ParameterizedType pType = (java.lang.reflect.ParameterizedType) genericType;
                Type[] actualTypes = pType.getActualTypeArguments();
                
                // Vérifier qu'il y a exactement 2 types génériques (K, V)
                if (actualTypes.length == 2) {
                    // Vérifier que les deux sont String
                    return actualTypes[0] == String.class && actualTypes[1] == String.class;
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, on considère que ce n'est pas Map<String,String>
        }
        
        return false;
    }

    /**
     * Sprint 10: Extrait les fichiers uploadés de la requête
     * Retourne une Map<nom, Byte[]> avec tous les fichiers
     */
    private Map<String, Byte[]> extractUploadedFiles(HttpServletRequest req) {
        Map<String, Byte[]> files = new HashMap<>();
        
        try {
            // Vérifier si la requête est multipart (pour l'upload de fichiers)
            String contentType = req.getContentType();
            if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
                // Obtenir tous les parts (fichiers et paramètres)
                Collection<Part> parts = req.getParts();
                
                for (Part part : parts) {
                    // Vérifier si c'est un fichier (a un nom de fichier)
                    String fileName = getFileName(part);
                    if (fileName != null && !fileName.isEmpty()) {
                        // Lire le contenu du fichier
                        try (java.io.InputStream inputStream = part.getInputStream()) {
                            byte[] fileBytes = inputStream.readAllBytes();
                            // Convertir byte[] en Byte[]
                            Byte[] fileBytesWrapper = new Byte[fileBytes.length];
                            for (int j = 0; j < fileBytes.length; j++) {
                                fileBytesWrapper[j] = fileBytes[j];
                            }
                            // Utiliser le nom du champ (part.getName()) comme clé
                            files.put(part.getName(), fileBytesWrapper);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, retourner une Map vide
        }
        
        return files;
    }
    
    /**
     * Sprint 10: Extrait le nom du fichier d'un Part
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            String[] tokens = contentDisposition.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                    return fileName;
                }
            }
        }
        return null;
    }
    
    /**
     * Sprint 10: Vérifie si un paramètre est de type Map<String,Byte[]>
     */
    private boolean isMapStringByteArray(Class<?> paramType, Parameter param) {
        // Vérifier si c'est une Map
        if (!Map.class.isAssignableFrom(paramType)) {
            return false;
        }
        
        // Vérifier les types génériques via Type
        try {
            Type genericType = param.getParameterizedType();
            if (genericType instanceof java.lang.reflect.ParameterizedType) {
                java.lang.reflect.ParameterizedType pType = (java.lang.reflect.ParameterizedType) genericType;
                Type[] actualTypes = pType.getActualTypeArguments();
                
                // Vérifier qu'il y a exactement 2 types génériques (K, V)
                if (actualTypes.length == 2) {
                    // Vérifier que K est String et V est Byte[]
                    return actualTypes[0] == String.class && 
                           actualTypes[1] == Byte[].class;
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, on considère que ce n'est pas Map<String,Byte[]>
        }
        
        return false;
    }

    /**
     * Sprint 8-bis: Vérifie si un paramètre est un type objet (pas primitif, pas String, pas Map)
     */
    private boolean isObjectType(Class<?> paramType) {
        // Exclure les types primitifs
        if (paramType.isPrimitive()) {
            return false;
        }
        
        // Exclure String
        if (paramType == String.class) {
            return false;
        }
        
        // Exclure Map (déjà géré par Sprint 8)
        if (Map.class.isAssignableFrom(paramType)) {
            return false;
        }
        
        // Exclure les types de base Java
        if (paramType == Integer.class || paramType == Long.class || 
            paramType == Double.class || paramType == Float.class ||
            paramType == Boolean.class || paramType == Byte.class ||
            paramType == Short.class || paramType == Character.class) {
            return false;
        }
        
        // C'est un objet personnalisé
        return true;
    }

    /**
     * Sprint 8-bis: Instancie et remplit un objet avec les paramètres de la requête
     * Utilise la convention: paramName.property (ex: e.name, e.departement[0].name)
     */
    private Object instantiateAndFillObject(Class<?> objectType, String paramName, Map<String, String> allParams) {
        try {
            // Créer une instance de l'objet
            Object instance = objectType.getDeclaredConstructor().newInstance();
            
            // Si le nom du paramètre n'est pas disponible, retourner l'instance vide
            if (paramName == null || paramName.startsWith("arg")) {
                return instance;
            }
            
            // Préfixe pour chercher les paramètres (ex: "e." pour paramètre "e")
            String prefix = paramName + ".";
            
            // Parcourir tous les paramètres qui commencent par le préfixe
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                if (key.startsWith(prefix)) {
                    // Enlever le préfixe pour obtenir le chemin de la propriété
                    String propertyPath = key.substring(prefix.length());
                    setPropertyValue(instance, propertyPath, value);
                }
            }
            
            return instance;
        } catch (Exception e) {
            // En cas d'erreur, retourner une instance vide
            try {
                return objectType.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Sprint 8-bis: Définit la valeur d'une propriété dans un objet
     * Gère les chemins simples (name) et complexes (departement[0].name)
     */
    private void setPropertyValue(Object obj, String propertyPath, String value) {
        try {
            // Gérer les tableaux/listes: departement[0].name
            if (propertyPath.contains("[") && propertyPath.contains("]")) {
                int bracketIndex = propertyPath.indexOf('[');
                String propertyName = propertyPath.substring(0, bracketIndex);
                String rest = propertyPath.substring(bracketIndex);
                
                // Extraire l'index: [0]
                int endBracket = rest.indexOf(']');
                String indexStr = rest.substring(1, endBracket);
                int index = Integer.parseInt(indexStr);
                
                // Récupérer la propriété (tableau ou liste)
                Object arrayOrList = getPropertyValue(obj, propertyName);
                
                if (arrayOrList != null) {
                    // Si c'est un tableau
                    if (arrayOrList.getClass().isArray()) {
                        Object[] array = (Object[]) arrayOrList;
                        if (index < array.length) {
                            if (array[index] == null) {
                                // Créer une instance si nécessaire (pour les objets)
                                Class<?> componentType = arrayOrList.getClass().getComponentType();
                                if (!componentType.isPrimitive() && componentType != String.class) {
                                    try {
                                        array[index] = componentType.getDeclaredConstructor().newInstance();
                                    } catch (Exception e) {
                                        // Ignorer
                                    }
                                }
                            }
                            if (array[index] != null) {
                                // Continuer avec le reste du chemin
                                String remainingPath = rest.substring(endBracket + 1);
                                if (remainingPath.startsWith(".")) {
                                    remainingPath = remainingPath.substring(1);
                                }
                                if (!remainingPath.isEmpty()) {
                                    setPropertyValue(array[index], remainingPath, value);
                                } else {
                                    // C'est la valeur finale
                                    setSimpleProperty(array[index], "value", value);
                                }
                            }
                        }
                    }
                    // Si c'est une List
                    else if (java.util.List.class.isAssignableFrom(arrayOrList.getClass())) {
                        @SuppressWarnings("unchecked")
                        java.util.List<Object> list = (java.util.List<Object>) arrayOrList;
                        
                        // S'assurer que la liste a assez d'éléments
                        while (list.size() <= index) {
                            list.add(null);
                        }
                        
                        if (list.get(index) == null) {
                            // Déterminer le type d'élément de la liste
                            Type genericType = null;
                            try {
                                Field field = obj.getClass().getDeclaredField(propertyName);
                                field.setAccessible(true);
                                Type fieldType = field.getGenericType();
                                if (fieldType instanceof ParameterizedType) {
                                    ParameterizedType pType = (ParameterizedType) fieldType;
                                    Type[] actualTypes = pType.getActualTypeArguments();
                                    if (actualTypes.length > 0 && actualTypes[0] instanceof Class) {
                                        Class<?> elementType = (Class<?>) actualTypes[0];
                                        try {
                                            list.set(index, elementType.getDeclaredConstructor().newInstance());
                                        } catch (Exception e) {
                                            // Ignorer
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // Ignorer
                            }
                        }
                        
                        if (list.get(index) != null) {
                            String remainingPath = rest.substring(endBracket + 1);
                            if (remainingPath.startsWith(".")) {
                                remainingPath = remainingPath.substring(1);
                            }
                            if (!remainingPath.isEmpty()) {
                                setPropertyValue(list.get(index), remainingPath, value);
                            } else {
                                setSimpleProperty(list.get(index), "value", value);
                            }
                        }
                    }
                }
            } else {
                // Propriété simple: name
                setSimpleProperty(obj, propertyPath, value);
            }
        } catch (Exception e) {
            // Ignorer les erreurs de setter
        }
    }

    /**
     * Sprint 8-bis: Définit une propriété simple dans un objet
     */
    private void setSimpleProperty(Object obj, String propertyName, String value) {
        try {
            // Chercher un setter: setName(String)
            String setterName = "set" + capitalize(propertyName);
            Method[] methods = obj.getClass().getMethods();
            
            for (Method method : methods) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Object convertedValue = convertValue(value, paramType);
                    method.invoke(obj, convertedValue);
                    return;
                }
            }
            
            // Si pas de setter, essayer de définir directement un champ
            try {
                Field field = obj.getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object convertedValue = convertValue(value, fieldType);
                field.set(obj, convertedValue);
            } catch (NoSuchFieldException e) {
                // Champ n'existe pas, ignorer
            }
        } catch (Exception e) {
            // Ignorer les erreurs
        }
    }

    /**
     * Sprint 8-bis: Récupère la valeur d'une propriété
     */
    private Object getPropertyValue(Object obj, String propertyName) {
        try {
            // Chercher un getter: getName()
            String getterName = "get" + capitalize(propertyName);
            Method method = obj.getClass().getMethod(getterName);
            return method.invoke(obj);
        } catch (Exception e) {
            try {
                // Essayer directement le champ
                Field field = obj.getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Capitalise la première lettre d'une chaîne
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Convertit une String en type approprié
     */
    private Object convertValue(String stringValue, Class<?> targetType) {
        if (stringValue == null) {
            return getDefaultValue(targetType);
        }
        
        try {
            if (targetType == String.class) {
                return stringValue;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(stringValue);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(stringValue);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(stringValue);
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(stringValue);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(stringValue);
            } else if (targetType == byte.class || targetType == Byte.class) {
                return Byte.parseByte(stringValue);
            } else if (targetType == short.class || targetType == Short.class) {
                return Short.parseShort(stringValue);
            }
        } catch (NumberFormatException e) {
            // En cas d'erreur de conversion, retourner la valeur par défaut
            return getDefaultValue(targetType);
        }
        
        // Type non supporté, retourner null ou valeur par défaut
        return getDefaultValue(targetType);
    }

    /**
     * Retourne la valeur par défaut pour un type primitif ou null pour les objets
     */
    private Object getDefaultValue(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == float.class) return 0.0f;
        if (type == double.class) return 0.0;
        if (type == char.class) return '\u0000';
        return null;
    }

    private String findExistingIndex() {
        for (String index : INDEX_FILES) {
            try {
                if (getServletContext().getResource(index) != null) {
                    return index;
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid path in INDEX_FILES: " + index, e);
            }
        }
        return null;
    }


    private void customServe(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            String uri = req.getRequestURI();
            String responseBody = """
                <html>
                    <head><title>Resource Not Found</title></head>
                    <body style="font-family:sans-serif;">
                        <h1>Page d'accueil non trouvée</h1>
                        <p>Aucun fichier <code>index.html</code> ou <code>index.jsp</code> n'a été trouvé.</p>
                        <p>URL demandée : <strong>%s</strong></p>
                    </body>
                </html>
                """.formatted(uri);
            out.println(responseBody);
        }
    }

    private void defaultServe(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        defaultDispatcher.forward(req, res);
    }

    /**
     * Classe interne pour stocker un pattern d'URL avec son mapping et sa méthode HTTP
     */
    private static class UrlPattern {
        private String pattern;
        private Mapping mapping;
        private String httpMethod;

        public UrlPattern(String pattern, Mapping mapping) {
            this.pattern = pattern;
            this.mapping = mapping;
            this.httpMethod = mapping.getHttpMethod();
        }

        public UrlPattern(String pattern, Mapping mapping, String httpMethod) {
            this.pattern = pattern;
            this.mapping = mapping;
            this.httpMethod = httpMethod != null ? httpMethod.toUpperCase() : "GET";
        }

        public String getPattern() {
            return pattern;
        }

        public Mapping getMapping() {
            return mapping;
        }

        public String getHttpMethod() {
            return httpMethod;
        }
    }
}
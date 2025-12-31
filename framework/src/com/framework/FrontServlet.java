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
     */
    private Object[] prepareMethodArguments(Method method, Map<String, String> urlParams, HttpServletRequest req) {
        Parameter[] parameters = method.getParameters();
        
        // Si la méthode n'a pas de paramètres, retourner un tableau vide (anciennes fonctionnalités)
        if (parameters.length == 0) {
            return new Object[0];
        }
        
        Object[] args = new Object[parameters.length];
        
        // Combiner les paramètres d'URL et de requête
        Map<String, String> allParams = new HashMap<>(urlParams);
        
        // Ajouter les paramètres de requête
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            allParams.put(paramName, req.getParameter(paramName));
        }
        
        // Pour chaque paramètre de la méthode
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName = param.getName();
            Class<?> paramType = param.getType();
            
            // Vérifier si le nom du paramètre est disponible (compilé avec -parameters)
            // Si non disponible, param.getName() retourne "arg0", "arg1", etc.
            // Dans ce cas, on ne peut pas faire de matching par nom, donc on utilise null
            boolean isRealName = paramName != null && !paramName.startsWith("arg");
            
            String stringValue = null;
            if (isRealName) {
                // Chercher la valeur dans les paramètres (URL ou requête) par nom
                stringValue = allParams.get(paramName);
            }
            // Si le nom n'est pas disponible ou la valeur n'est pas trouvée, stringValue reste null
            
            // Convertir en type approprié (retourne valeur par défaut si null)
            args[i] = convertValue(stringValue, paramType);
        }
        
        return args;
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
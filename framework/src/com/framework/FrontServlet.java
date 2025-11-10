package com.framework;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.annotations.HandleUrl;
import com.annotations.Controller;

/**
 * FrontServlet: servlet principal du framework.
 * - Scanne les contrôleurs avec @Controller
 * - Mappe les URLs avec @HandleUrl
 * - Gère les requêtes HTTP
 */
public class FrontServlet extends HttpServlet {
    private RequestDispatcher defaultDispatcher;
    private HashMap<String, Mapping> urlMappings = new HashMap<>();
    private List<Class<?>> controllerClasses = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
        
        // Récupérer le package à scanner depuis web.xml
        String packageToScan = getInitParameter("controllerPackage");
        if (packageToScan != null && !packageToScan.isEmpty()) {
            try {
                System.out.println("[Framework] Démarrage du scan du package: " + packageToScan);
                scanControllersWithAnnotations(packageToScan);
                System.out.println("[Framework] Scan termine. " + urlMappings.size() + " mappings trouvés.");
            } catch (Exception e) {
                throw new ServletException("Erreur lors du scan des controleurs", e);
            }
        }
    }

    /**
     * Scanne le package pour détecter les contrôleurs et leurs méthodes.
     */
    private void scanControllersWithAnnotations(String packageName) throws Exception {
        // 1. Scanner toutes les classes du package
        List<Class<?>> allClasses = ClassScanner.scanPackage(packageName);
        System.out.println("[Framework] Classes scannées: " + allClasses.size());
        
        // 2. Filtrer les classes avec @Controller
        controllerClasses = ClassScanner.filterByAnnotation(allClasses, Controller.class);
        
        if (!controllerClasses.isEmpty()) {
            System.out.println("[Framework] Contrôleurs détectés:");
            for (Class<?> clazz : controllerClasses) {
                Controller annotation = clazz.getAnnotation(Controller.class);
                String value = annotation.value().isEmpty() ? "" : " (" + annotation.value() + ")";
                System.out.println("  - " + clazz.getSimpleName() + value);
            }
        }
        
        // 3. Traiter toutes les classes pour mapper les URLs
        for (Class<?> clazz : allClasses) {
            mapClassMethods(clazz);
        }
    }

    /**
     * Mappe les méthodes d'une classe annotées avec @HandleUrl.
     */
    private void mapClassMethods(Class<?> clazz) {
        try {
            // Créer une instance du contrôleur
            Object instance = clazz.getDeclaredConstructor().newInstance();
            
            // Parcourir toutes les méthodes
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(HandleUrl.class)) {
                    HandleUrl annotation = method.getAnnotation(HandleUrl.class);
                    String url = annotation.value();
                    
                    // Créer le mapping URL -> Méthode
                    urlMappings.put(url, new Mapping(instance, method));
                    System.out.println("[Framework] Mapping: " + url + " -> " + 
                                     clazz.getSimpleName() + "." + method.getName() + "()");
                }
            }
        } catch (Exception e) {
            // Ignorer les classes non instanciables
        }
    }

    /**
     * Retourne les classes avec @Controller (pour debugging).
     */
    public List<Class<?>> getControllerClasses() {
        return new ArrayList<>(controllerClasses);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 1. Vérifier si l'URL a un mapping @HandleUrl
        if (urlMappings.containsKey(path)) {
            handleAnnotatedMethod(urlMappings.get(path), req, res);
            return;
        }

        // 2. Gérer la racine / -> chercher index.html ou index.jsp
        if (path.equals("/") || path.isEmpty()) {
            String indexPath = findIndexFile();
            if (indexPath != null) {
                req.getRequestDispatcher(indexPath).forward(req, res);
                return;
            } else {
                showNoIndexPage(req, res);
                return;
            }
        }

        // 3. Vérifier si la ressource existe (fichier statique)
        boolean resourceExists = getServletContext().getResource(path) != null;
        if (resourceExists) {
            // Laisser le servlet par défaut gérer la ressource
            defaultDispatcher.forward(req, res);
        } else {
            // Afficher page 404
            showNoIndexPage(req, res);
        }
    }

    /**
     * Invoque la méthode annotée avec @HandleUrl.
     */
    private void handleAnnotatedMethod(Mapping mapping, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Method method = mapping.getMethod();
            Object controller = mapping.getController();
            
            // Invoquer la méthode du contrôleur
            Object result = method.invoke(controller);
            
            // Envoyer la réponse HTML
            res.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = res.getWriter()) {
                if (result != null) {
                    out.println(result.toString());
                } else {
                    out.println("<html><body><h1>Méthode exécutée avec succès</h1></body></html>");
                }
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'invocation de la méthode", e);
        }
    }

    /**
     * Cherche un fichier index (index.html, index.htm, index.jsp).
     */
    private String findIndexFile() {
        String[] indexFiles = {"/index.html", "/index.htm", "/index.jsp"};
        for (String indexFile : indexFiles) {
            try {
                if (getServletContext().getResource(indexFile) != null) {
                    return indexFile;
                }
            } catch (MalformedURLException e) {
                // Ignorer
            }
        }
        return null;
    }

    /**
     * Affiche une page d'erreur quand aucune ressource n'est trouvée.
     */
    private void showNoIndexPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            String uri = req.getRequestURI();
            out.println("<html>");
            out.println("<head><title>Ressource non trouvée</title></head>");
            out.println("<body style='font-family:sans-serif;'>");
            out.println("<h1>Page non trouvée</h1>");
            out.println("<p>URL demandée : <strong>" + uri + "</strong></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}

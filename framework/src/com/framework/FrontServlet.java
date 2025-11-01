package com.framework;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.annotations.HandleUrl;

public class FrontServlet extends HttpServlet {
    private RequestDispatcher defaultDispatcher;
    private static final List<String> INDEX_FILES = Arrays.asList(
        "/index.html", "/index.htm", "/index.jsp"
    );
    private HashMap<String, Mapping> urlMappings = new HashMap<>();

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
        Object instance = clazz.getDeclaredConstructor().newInstance();
        
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HandleUrl.class)) {
                HandleUrl annotation = method.getAnnotation(HandleUrl.class);
                String url = annotation.value();
                urlMappings.put(url, new Mapping(instance, method));
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Vérifier si l'URL correspond à un mapping d'annotation
        if (urlMappings.containsKey(path)) {
            handleAnnotatedMethod(urlMappings.get(path), req, res);
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

    private void handleAnnotatedMethod(Mapping mapping, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Method method = mapping.getMethod();
            Object controller = mapping.getController();
            
            // Invoquer la méthode
            Object result = method.invoke(controller);
            
            // Gérer le résultat
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
}
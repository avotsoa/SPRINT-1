package com.framework;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassScanner: scanne le classpath pour trouver les classes d'un package.
 * Permet de détecter les classes avec des annotations spécifiques.
 */
public class ClassScanner {
    
    /**
     * Scanne un package et retourne toutes les classes trouvées.
     * @param packageName Nom du package (ex: "com.controllers")
     * @return Liste des classes trouvées
     */
    public static List<Class<?>> scanPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        
        // Convertir le package en chemin (com.test -> com/test)
        String path = packageName.replace('.', '/');
        
        // Récupérer le ClassLoader et la ressource
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        
        if (resource == null) {
            System.out.println("[Scanner] Package non trouvé: " + packageName);
            return classes;
        }
        
        File directory = new File(resource.getFile());
        
        if (directory.exists() && directory.isDirectory()) {
            scanDirectory(directory, packageName, classes);
        }
        
        return classes;
    }
    
    /**
     * Scanne récursivement un répertoire pour trouver les fichiers .class
     * @param directory Répertoire à scanner
     * @param packageName Package correspondant
     * @param classes Liste où ajouter les classes trouvées
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes) throws Exception {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                // Scanner les sous-packages
                scanDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                // Extraire le nom de la classe (sans .class)
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    System.err.println("[Scanner] Classe non chargeable: " + className);
                }
            }
        }
    }
    
    /**
     * Filtre les classes ayant une annotation spécifique.
     * @param classes Liste de classes à filtrer
     * @param annotationClass Annotation à rechercher
     * @return Classes annotées
     */
    public static List<Class<?>> filterByAnnotation(List<Class<?>> classes, 
                                                     Class<? extends java.lang.annotation.Annotation> annotationClass) {
        List<Class<?>> filteredClasses = new ArrayList<>();
        
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                filteredClasses.add(clazz);
            }
        }
        
        return filteredClasses;
    }
}

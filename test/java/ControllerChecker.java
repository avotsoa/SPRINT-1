package test.java;

import com.annotations.Controller;
// Note: Class est dans java.lang qui est importe automatiquement

/**
 * Classe utilitaire pour verifier si une classe contient l'annotation @Controller.
 * Sprint 2-bis: Verification des annotations au niveau classe
 */
public class ControllerChecker {
    
    /**
     * Verifie si une classe a l'annotation @Controller
     */
    public static boolean hasControllerAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }
    
    /**
     * Obtient la valeur de l'annotation @Controller si elle existe
     */
    public static String getControllerValue(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Controller.class)) {
            Controller annotation = clazz.getAnnotation(Controller.class);
            return annotation.value();
        }
        return null;
    }
    
    /**
     * Affiche les informations sur l'annotation @Controller d'une classe
     */
    public static void printControllerInfo(Class<?> clazz) {
        System.out.println("Classe: " + clazz.getSimpleName());
        if (hasControllerAnnotation(clazz)) {
            String value = getControllerValue(clazz);
            System.out.println("  -> A l'annotation @Controller");
            if (value != null && !value.isEmpty()) {
                System.out.println("  -> Valeur: " + value);
            } else {
                System.out.println("  -> Valeur: (vide)");
            }
        } else {
            System.out.println("  -> N'A PAS l'annotation @Controller");
        }
        System.out.println();
    }
    
    /**
     * Methode main pour tester la verification des annotations
     */
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Verification des annotations @Controller");
        System.out.println("==========================================");
        System.out.println();
        
        // Liste des classes a verifier
        Class<?>[] classes = {
            TestController.class,
            UtilisateurController.class,
            EtudiantController.class,
            FormulaireController.class
        };
        
        System.out.println("Classes avec @Controller:");
        System.out.println("-------------------------");
        for (Class<?> clazz : classes) {
            printControllerInfo(clazz);
        }
        
        // Exemple avec une classe sans annotation
        System.out.println("Exemple de classe SANS @Controller:");
        System.out.println("-------------------------------------");
        printControllerInfo(ControllerChecker.class);
    }
}


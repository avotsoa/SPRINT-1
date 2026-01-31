package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;
import com.framework.ModelView;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur pour afficher les informations sur les annotations @Controller.
 * Sprint 2-bis: Affiche quelles classes ont ou n'ont pas @Controller
 */
@Controller("AnnotationInfoController")
public class AnnotationInfoController {
    
    /**
     * Affiche la liste des classes avec et sans @Controller
     * URL: /annotation-info
     */
    @HandleUrl("/annotation-info")
    public ModelView afficherInfo() {
        ModelView mv = new ModelView("/annotation-info.jsp");
        
        // Liste des classes a verifier
        List<ClassInfo> classes = new ArrayList<>();
        
        // Classes AVEC @Controller
        classes.add(new ClassInfo("TestController", true, "TestController"));
        classes.add(new ClassInfo("UtilisateurController", true, "UtilisateurController"));
        classes.add(new ClassInfo("EtudiantController", true, "EtudiantController"));
        classes.add(new ClassInfo("FormulaireController", true, "FormulaireController"));
        classes.add(new ClassInfo("AnnotationInfoController", true, "AnnotationInfoController"));
        
        // Classes SANS @Controller
        classes.add(new ClassInfo("SimpleService", false, null));
        classes.add(new ClassInfo("ControllerChecker", false, null));
        
        mv.addObject("classes", classes);
        mv.addString("titre", "Information sur les annotations @Controller");
        
        return mv;
    }
    
    /**
     * Classe interne pour stocker les informations sur une classe
     */
    public static class ClassInfo {
        private String className;
        private boolean hasController;
        private String controllerValue;
        
        public ClassInfo(String className, boolean hasController, String controllerValue) {
            this.className = className;
            this.hasController = hasController;
            this.controllerValue = controllerValue;
        }
        
        public String getClassName() {
            return className;
        }
        
        public boolean hasController() {
            return hasController;
        }
        
        public String getControllerValue() {
            return controllerValue;
        }
    }
}

